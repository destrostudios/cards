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
        float[] scores = new float[players.size()];
        int i = 0;
        for (int player : players) {
            int ownHealth = data.getComponent(player, Components.Stats.HEALTH);
            int opponent = data.getComponent(player, Components.NEXT_PLAYER);
            int opponentHealth = data.getComponent(opponent, Components.Stats.HEALTH);
            for (int card : data.query(Components.Stats.HEALTH).list(card -> data.hasComponent(card, Components.BOARD))) {
                int health = data.getComponent(card, Components.Stats.HEALTH);
                int owner = data.getComponent(card, Components.OWNED_BY);
                if (owner == player) {
                    ownHealth += health;
                } else {
                    opponentHealth += health;
                }
            }
            scores[i] = (((float) ownHealth) / (ownHealth + opponentHealth));
            i++;
        }
        return scores;
    }
}
