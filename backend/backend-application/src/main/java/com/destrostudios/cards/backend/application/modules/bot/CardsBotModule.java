package com.destrostudios.cards.backend.application.modules.bot;

import com.destrostudios.cards.backend.application.modules.QueueServerModule;
import com.destrostudios.cards.shared.entities.SimpleEntityData;
import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameContext;
import com.destrostudios.gametools.bot.BotActionReplay;
import com.destrostudios.gametools.bot.mcts.MctsBot;
import com.destrostudios.gametools.bot.mcts.MctsBotSettings;
import com.destrostudios.gametools.bot.mcts.TerminationType;
import com.destrostudios.gametools.network.server.modules.game.GameServerModule;
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
            int activePlayer = data.unique(Components.Player.ACTIVE_PLAYER);
            // Yeah... good enough for now
            if (!data.getComponent(activePlayer, Components.NAME).equals(QueueServerModule.BOT_USER_NAME) || game.state.isGameOver()) {
                break;
            }
            MctsBot bot = bots.computeIfAbsent(gameId, gid -> {
                MctsBotSettings<CardsBotState, Event> botSettings = new MctsBotSettings<>();
                botSettings.maxThreads = 3;
                botSettings.termination = TerminationType.MILLIS_ELAPSED;
                botSettings.strength = 3000;
                botSettings.evaluation = CardsBotEval::eval;
                return new MctsBot<>(new CardsBotService(), botSettings);
            });
            CardsBotState botState = new CardsBotState(game.state, new Random());
            LOG.debug("Bot started calculating... (gameId = {})", game.id);
            long startNanos = System.nanoTime();
            List<Event> actions = bot.sortedActions(botState, botState.activeTeam());
            LOG.debug("Bot finished calculating after {} ns. (gameId = {})", (System.nanoTime() - startNanos), game.id);
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
}
