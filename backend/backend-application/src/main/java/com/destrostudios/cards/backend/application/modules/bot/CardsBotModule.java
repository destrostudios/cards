package com.destrostudios.cards.backend.application.modules.bot;

import com.destrostudios.cards.backend.application.modules.QueueServerModule;
import com.destrostudios.cards.shared.entities.SimpleEntityData;
import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameContext;
import com.destrostudios.cards.shared.rules.util.StatsUtil;
import com.destrostudios.gametools.bot.BotActionReplay;
import com.destrostudios.gametools.bot.mcts.MctsBot;
import com.destrostudios.gametools.bot.mcts.MctsBotSettings;
import com.destrostudios.gametools.bot.mcts.TerminationType;
import com.destrostudios.gametools.network.server.modules.game.GameServerModule;
import com.destrostudios.gametools.network.server.modules.game.MasterRandom;
import com.destrostudios.gametools.network.server.modules.game.ServerGameData;
import com.destrostudios.gametools.network.shared.modules.NetworkModule;
import com.destrostudios.gametools.network.shared.modules.game.messages.GameActionRequest;
import com.esotericsoftware.kryonet.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class CardsBotModule extends NetworkModule {

    private static final Logger LOG = LoggerFactory.getLogger(CardsBotModule.class);

    public CardsBotModule(GameServerModule<GameContext, Event> gameModule) {
        this.gameModule = gameModule;
        bots = new HashMap<>();
    }
    private GameServerModule<GameContext, Event> gameModule;
    private HashMap<UUID, MctsBot> bots;

    @Override
    public void received(Connection connection, Object object) {
        if (object instanceof GameActionRequest request) {
            onAction(request.game(), request.action());
            checkBotTurn(request.game());
        }
    }

    public void checkBotTurn(UUID gameId) {
        ServerGameData<GameContext> game = gameModule.getGame(gameId);
        SimpleEntityData data = game.state.getData();
        while (true) {
            Integer activePlayer = data.query(Components.Game.ACTIVE_PLAYER).list().get(0);
            // Yeah... good enough for now
            if (!data.getComponent(activePlayer, Components.NAME).equals(QueueServerModule.BOT_USER_NAME) || game.state.isGameOver()) {
                break;
            }
            MctsBot bot = bots.computeIfAbsent(gameId, gid -> {
                MctsBotSettings<CardsBotState, Event> botSettings = new MctsBotSettings<>();
                botSettings.verbose = true;
                botSettings.maxThreads = 3;
                botSettings.termination = TerminationType.MILLIS_ELAPSED;
                botSettings.strength = 3000;
                botSettings.evaluation = CardsBotModule::eval;
                return new MctsBot<>(new CardsBotService(), botSettings);
            });
            CardsBotState botState = new CardsBotState(game.state, new MasterRandom(new Random()));
            LOG.info("Bot started calculating...");
            long startNanos = System.nanoTime();
            List<Event> actions = bot.sortedActions(botState, botState.activeTeam());
            LOG.info("Bot finished calculating after {}", (System.nanoTime() - startNanos));
            Event action = actions.get(0);
            gameModule.applyAction(game.id, action);
            onAction(gameId, action);
        }
    }

    private void onAction(UUID gameId, Object action) {
        MctsBot bot = bots.get(gameId);
        if (bot != null) {
            bot.stepRoot(new BotActionReplay<>(action, new int[0])); // TODO: Randomness?
            if (gameModule.getGame(gameId).state.isGameOver()) {
                bots.remove(gameId);
            }
        }
    }

    public static float[] eval(CardsBotState botState) {
        SimpleEntityData data = botState.getGameContext().getData();
        List<Integer> players = data.query(Components.NEXT_PLAYER).list();
        List<Integer> cardsOnBoard = data.query(Components.BOARD).list(entity -> data.hasComponent(entity, Components.CREATURE_CARD));
        List<Integer> cardsInHand = data.query(Components.HAND).list();
        float[] scores = new float[players.size()];
        int i = 0;
        for (int player : players) {
            float score;
            if (botState.getGameContext().isGameOver()) {
                score = ((botState.getGameContext().getWinner() == player) ? 1 : 0);
            } else {
                int opponent = data.getComponent(player, Components.NEXT_PLAYER);
                int ownPlayerHealth = StatsUtil.getEffectiveHealth(data, player);
                int opponentPlayerHealth = StatsUtil.getEffectiveHealth(data, opponent);
                int ownCreaturesAttack = 0;
                int opponentCreaturesAttack = 0;
                int ownCreaturesHealth = 0;
                int opponentCreaturesHealth = 0;
                int ownCardsInHand = 0;
                int opponentCardsInHand = 0;
                for (int card : cardsOnBoard) {
                    int owner = data.getComponent(card, Components.OWNED_BY);
                    Integer attack = StatsUtil.getEffectiveAttack(data, card);
                    Integer health = StatsUtil.getEffectiveHealth(data, card);
                    if (owner == player) {
                        if (attack != null) {
                            ownCreaturesAttack += attack;
                        }
                        if (health != null) {
                            ownCreaturesHealth += health;
                        }
                    } else {
                        if (attack != null) {
                            opponentCreaturesAttack += attack;
                        }
                        if (health != null) {
                            opponentCreaturesHealth += health;
                        }
                    }
                }
                for (int card : cardsInHand) {
                    int owner = data.getComponent(card, Components.OWNED_BY);
                    if (owner == player) {
                        ownCardsInHand++;
                    } else {
                        opponentCardsInHand++;
                    }
                }
                float playerHealthWeight = 1;
                float creaturesAttackWeight = 2;
                float creaturesHealthWeight = 3;
                float cardsInHandWeight = 0.25f;
                float sumWeights = (playerHealthWeight + creaturesAttackWeight + creaturesHealthWeight + cardsInHandWeight);

                float playerHealthScore = getWeightedScore(playerHealthWeight, ownPlayerHealth, ownPlayerHealth + opponentPlayerHealth);
                float creaturesAttackScore = getWeightedScore(creaturesAttackWeight, ownCreaturesAttack, ownCreaturesAttack + opponentCreaturesAttack);
                float creaturesHealthScore = getWeightedScore(creaturesHealthWeight, ownCreaturesHealth, ownCreaturesHealth + opponentCreaturesHealth);
                float cardsInHandScore = getWeightedScore(cardsInHandWeight, ownCardsInHand, ownCardsInHand + opponentCardsInHand);
                float sumScores = (playerHealthScore + creaturesAttackScore + creaturesHealthScore + cardsInHandScore);

                score = (sumScores / sumWeights);
            }
            scores[i] = score;
            i++;
        }
        return scores;
    }

    private static float getWeightedScore(float weight, float value, float sum) {
        return ((sum != 0) ? weight * (value / sum) : 0.5f);
    }
}
