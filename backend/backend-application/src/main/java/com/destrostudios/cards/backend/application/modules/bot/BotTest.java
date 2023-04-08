package com.destrostudios.cards.backend.application.modules.bot;

import com.destrostudios.cards.backend.application.BackendApplication;
import com.destrostudios.cards.backend.application.services.*;
import com.destrostudios.cards.backend.database.databases.Database;
import com.destrostudios.cards.shared.application.ApplicationSetup;
import com.destrostudios.cards.shared.entities.SimpleEntityData;
import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.*;
import com.destrostudios.cards.shared.rules.game.GameStartEvent;
import com.destrostudios.gametools.bot.BotActionReplay;
import com.destrostudios.gametools.bot.mcts.MctsBot;
import com.destrostudios.gametools.bot.mcts.MctsBotSettings;
import com.destrostudios.gametools.bot.mcts.TerminationType;
import com.destrostudios.gametools.network.server.modules.game.MasterRandom;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import org.slf4j.impl.SimpleLogger;

import java.util.List;
import java.util.Random;

public class BotTest {

    public static void main(String[] args) {
        ApplicationSetup.setup();

        System.setProperty(SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "ERROR");

        Database database = BackendApplication.getDatabase();
        CardService cardService = new CardService(database);
        FoilService foilService = new FoilService(database);
        CardListService cardListService = new CardListService(database, cardService, foilService);
        ModeService modeService = new ModeService(database, cardListService);
        QueueService queueService = new QueueService(database);

        StartGameInfo startGameInfo = new StartGameInfo(
            modeService.getMode(GameConstants.MODE_NAME_CLASSIC),
            queueService.getQueue(GameConstants.QUEUE_NAME_BOT),
            "forest",
            new PlayerInfo[] {
                new PlayerInfo(1, "Bot1", null),
                new PlayerInfo(2, "Bot2", null)
            }
        );
        SimpleEntityData data = new SimpleEntityData(Components.ALL);
        GameSetup gameSetup = new GameSetup(cardService.getCards(), data, startGameInfo);
        gameSetup.apply();
        GameContext gameContext = new GameContext(startGameInfo, data);

        MasterRandom random = new MasterRandom(new Random());
        applyAction(gameContext, new GameStartEvent(), random);

        MctsBotSettings<CardsBotState, Event> botSettings = new MctsBotSettings<>();
        botSettings.verbose = true;
        botSettings.maxThreads = 3;
        botSettings.termination = TerminationType.NODE_COUNT;
        botSettings.strength = 1000;
        botSettings.evaluation = CardsBotModule::eval;
        MctsBot bot = new MctsBot<>(new CardsBotService(), botSettings);
        CardsBotState botState = new CardsBotState(gameContext, random);

        long gameStartNanos = System.nanoTime();
        int actionIndex = 0;
        while (!gameContext.isGameOver()) {
            long actionStartNanos = System.nanoTime();
            List<Event> actions = bot.sortedActions(botState, botState.activeTeam());
            long actionDurationNanos = (System.nanoTime() - actionStartNanos);
            Event action = actions.get(0);
            System.out.println("Action #" + (actionIndex + 1) + " => " + action + "\t(from " + actions.size() + " possible actions, node count " + botSettings.strength + ", in " + (actionDurationNanos / 1_000_000) + "ms)");
            applyAction(gameContext, action, random);
            bot.stepRoot(new BotActionReplay<>(action, new int[0])); // TODO: Randomness?
            actionIndex++;
        }
        long gameDurationNanos = (System.nanoTime() - gameStartNanos);
        System.out.println("Game over, total duration = " + (gameDurationNanos / 1_000_000) + "ms.");
    }

    private static void applyAction(GameContext gameContext, Event action, NetworkRandom random) {
        gameContext.getEvents().fire(action, random);
        while (gameContext.getEvents().hasPendingEventHandler()) {
            gameContext.getEvents().triggerNextEventHandler();
        }
    }
}
