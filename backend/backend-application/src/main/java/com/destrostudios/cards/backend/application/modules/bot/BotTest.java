package com.destrostudios.cards.backend.application.modules.bot;

import com.destrostudios.cards.backend.application.TestGameSetup;
import com.destrostudios.cards.shared.application.ApplicationSetup;
import com.destrostudios.cards.shared.entities.SimpleEntityData;
import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameContext;
import com.destrostudios.cards.shared.rules.PlayerInfo;
import com.destrostudios.cards.shared.rules.StartGameInfo;
import com.destrostudios.cards.shared.rules.game.GameStartEvent;
import com.destrostudios.gametools.bot.BotActionReplay;
import com.destrostudios.gametools.bot.mcts.MctsBot;
import com.destrostudios.gametools.bot.mcts.MctsBotSettings;
import com.destrostudios.gametools.bot.mcts.TerminationType;
import com.destrostudios.gametools.network.server.modules.game.MasterRandom;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import org.slf4j.impl.SimpleLogger;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class BotTest {

    public static void main(String[] args) {
        ApplicationSetup.setup();

        System.setProperty(SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "ERROR");

        StartGameInfo startGameInfo = new StartGameInfo("forest", new PlayerInfo(2, "Bot1", new LinkedList<>()), new PlayerInfo(2, "Bot2", new LinkedList<>()));
        SimpleEntityData data = new SimpleEntityData(Components.ALL);
        TestGameSetup testGameSetup = new TestGameSetup(data, startGameInfo);
        testGameSetup.apply();
        GameContext gameContext = new GameContext(startGameInfo, data);

        MasterRandom random = new MasterRandom(new Random());
        applyAction(gameContext, new GameStartEvent(), random);

        MctsBotSettings<CardsBotState, Event> botSettings = new MctsBotSettings<>();
        botSettings.verbose = true;
        botSettings.maxThreads = 1;
        botSettings.termination = TerminationType.NODE_COUNT;
        botSettings.strength = 100;
        botSettings.evaluation = CardsBotModule::eval;
        MctsBot bot = new MctsBot<>(new CardsBotService(), botSettings);
        CardsBotState botState = new CardsBotState(gameContext);

        while (!gameContext.isGameOver()) {
            long startNanos = System.nanoTime();
            List<Event> actions = bot.sortedActions(botState, botState.activeTeam());
            long durationNanos = (System.nanoTime() - startNanos);
            Event action = actions.get(0);
            System.out.println("Node Count " + botSettings.strength + " in " + (durationNanos / 1_000_000) + "ms => " + action);
            applyAction(gameContext, action, random);
            bot.stepRoot(new BotActionReplay<>(action, new int[0])); // TODO: Randomness?
        }
        System.out.println("Game over.");
    }

    private static void applyAction(GameContext gameContext, Event action, NetworkRandom random) {
        gameContext.getEvents().fire(action, random);
        while (gameContext.getEvents().hasNextTriggeredHandler()) {
            gameContext.getEvents().triggerNextHandler();
        }
    }
}
