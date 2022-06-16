package com.destrostudios.cards.backend.application.modules.bot;

import com.destrostudios.cards.shared.entities.SimpleEntityData;
import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameContext;
import com.destrostudios.cards.shared.rules.game.turn.EndTurnEvent;
import com.destrostudios.gametools.bot.BotActionReplay;
import com.destrostudios.gametools.bot.mcts.MctsBot;
import com.destrostudios.gametools.bot.mcts.MctsBotSettings;
import com.destrostudios.gametools.bot.mcts.TerminationType;
import com.destrostudios.gametools.network.server.modules.game.GameServerModule;
import com.destrostudios.gametools.network.server.modules.game.ServerGameData;
import com.destrostudios.gametools.network.shared.modules.NetworkModule;
import com.destrostudios.gametools.network.shared.modules.game.messages.GameActionRequest;
import com.destrostudios.gametools.network.shared.modules.game.messages.GameStartRequest;
import com.esotericsoftware.kryonet.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CardsBotModule extends NetworkModule {

    private static final Logger LOG = LoggerFactory.getLogger(CardsBotModule.class);

    private GameServerModule<GameContext, Event> gameModule;
    private MctsBot bot;

    public CardsBotModule(GameServerModule<GameContext, Event> gameModule) {
        this.gameModule = gameModule;
        MctsBotSettings<CardsBotState, Event> botSettings = new MctsBotSettings<>();
        botSettings.verbose = true;
        botSettings.maxThreads = 1;
        botSettings.termination = TerminationType.MILLIS_ELAPSED;
        botSettings.strength = 2000;
        botSettings.evaluation = CardsBotModule::eval;
        bot = new MctsBot<>(new CardsBotService(), botSettings);
    }

    @Override
    public void received(Connection connection, Object object) {
        if (object instanceof GameActionRequest request) {
            if ((request.action() instanceof GameStartRequest) || (request.action() instanceof EndTurnEvent)) {
                ServerGameData<GameContext> game = gameModule.getGame(request.game());
                SimpleEntityData data = game.state.getData();
                while (true) {
                    Integer activePlayer = data.query(Components.Game.ACTIVE_PLAYER).list().get(0);
                    // Yeah... good enough for now
                    if (!"Bot".equals(data.getComponent(activePlayer, Components.NAME)) || game.state.isGameOver()) {
                        break;
                    }
                    CardsBotState botState = new CardsBotState(game.state);
                    LOG.info("Bot started calculating...");
                    long startNanos = System.nanoTime();
                    List<Event> actions = bot.sortedActions(botState, botState.activeTeam());
                    LOG.info("Bot finished calculating after {}", (System.nanoTime() - startNanos));
                    gameModule.applyAction(game.id, actions.get(0));
                    bot.stepRoot(new BotActionReplay<>(actions.get(0), new int[0])); // TODO: Randomness?
                }
            }
        }
    }

    public static float[] eval(CardsBotState botState) {
        SimpleEntityData data = botState.getGameContext().getData();
        List<Integer> players = data.query(Components.NEXT_PLAYER).list();
        List<Integer> cardsOnBoard = data.query(Components.BOARD).list();
        List<Integer> cardsInHand = data.query(Components.HAND).list();
        float[] scores = new float[players.size()];
        int i = 0;
        for (int player : players) {
            float score;
            if (botState.getGameContext().isGameOver()) {
                score = ((botState.getGameContext().getWinner() == player) ? 1 : 0);
            } else {
                int opponent = data.getComponent(player, Components.NEXT_PLAYER);
                int ownPlayerHealth = data.getComponent(player, Components.Stats.HEALTH);
                int opponentPlayerHealth = data.getComponent(opponent, Components.Stats.HEALTH);
                int ownCreaturesAttack = 0;
                int opponentCreaturesAttack = 0;
                int ownCreaturesHealth = 0;
                int opponentCreaturesHealth = 0;
                int ownCardsInHand = 0;
                int opponentCardsInHand = 0;
                for (int card : cardsOnBoard) {
                    int owner = data.getComponent(card, Components.OWNED_BY);
                    Integer attack = data.getComponent(card, Components.Stats.ATTACK);
                    Integer health = data.getComponent(card, Components.Stats.HEALTH);
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
                            opponentCreaturesAttack += health;
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
                float playerHealthWeight = 2;
                float creaturesAttackWeight = 1;
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
