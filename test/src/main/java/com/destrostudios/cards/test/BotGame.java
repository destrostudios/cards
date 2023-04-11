package com.destrostudios.cards.test;

import com.destrostudios.cards.backend.application.modules.bot.CardsBotEval;
import com.destrostudios.cards.backend.application.modules.bot.CardsBotService;
import com.destrostudios.cards.backend.application.modules.bot.CardsBotState;
import com.destrostudios.cards.shared.entities.SimpleEntityData;
import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.model.Card;
import com.destrostudios.cards.shared.model.Mode;
import com.destrostudios.cards.shared.model.Queue;
import com.destrostudios.cards.shared.rules.*;
import com.destrostudios.cards.shared.rules.game.GameStartEvent;
import com.destrostudios.gametools.bot.BotActionReplay;
import com.destrostudios.gametools.bot.mcts.MctsBot;
import com.destrostudios.gametools.bot.mcts.MctsBotSettings;
import com.destrostudios.gametools.bot.mcts.TerminationType;
import com.destrostudios.gametools.network.server.modules.game.MasterRandom;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;

import java.util.List;
import java.util.Random;

public class BotGame {

    public BotGame(List<Card> cards, Mode mode, Queue queue, long seed, boolean verbose) {
        this.cards = cards;
        this.mode = mode;
        this.queue = queue;
        this.seed = seed;
        this.verbose = verbose;
    }
    private List<Card> cards;
    private Mode mode;
    private Queue queue;
    private long seed;
    private boolean verbose;
    private GameContext gameContext;

    public void play() {
        StartGameInfo startGameInfo = new StartGameInfo(
            mode,
            queue,
            "forest",
            new PlayerInfo[] {
                new PlayerInfo(1, "Bot1", null),
                new PlayerInfo(2, "Bot2", null)
            }
        );
        SimpleEntityData data = new SimpleEntityData(Components.ALL);
        GameSetup gameSetup = new GameSetup(cards, data, startGameInfo);
        gameSetup.apply();
        gameContext = new GameContext(startGameInfo, data);

        Random _random = new Random(seed);
        MasterRandom random = new MasterRandom(_random);

        applyAction(new GameStartEvent(), random);

        MctsBotSettings<CardsBotState, Event> botSettings = new MctsBotSettings<>();
        botSettings.maxThreads = 1;
        botSettings.termination = TerminationType.NODE_COUNT;
        botSettings.strength = 100;
        botSettings.evaluation = CardsBotEval::eval;
        botSettings.random = _random;
        MctsBot bot = new MctsBot<>(new CardsBotService(), botSettings);
        CardsBotState botState = new CardsBotState(gameContext, random);

        long gameStartNanos = System.nanoTime();
        int actionIndex = 0;
        while (!gameContext.isGameOver()) {
            long actionStartNanos = System.nanoTime();
            List<Event> actions = bot.sortedActions(botState, botState.activeTeam());
            long actionDurationNanos = (System.nanoTime() - actionStartNanos);
            Event action = actions.get(0);
            if (verbose) {
                System.out.println("Action #" + (actionIndex + 1) + " => " + action + "\t(from " + actions.size() + " possible actions, node count " + botSettings.strength + ", in " + (actionDurationNanos / 1_000_000) + "ms)");
            }
            applyAction(action, random);
            bot.stepRoot(new BotActionReplay<>(action, new int[0])); // TODO: Randomness?
            actionIndex++;
        }
        if (verbose) {
            long gameDurationNanos = (System.nanoTime() - gameStartNanos);
            System.out.println("Game over, total duration = " + (gameDurationNanos / 1_000_000) + "ms, winner = " + gameContext.getWinner() + ".");
        }
    }

    private void applyAction(Event action, NetworkRandom random) {
        gameContext.getEvents().fire(action, random);
        while (gameContext.getEvents().hasPendingEventHandler()) {
            gameContext.getEvents().triggerNextEventHandler();
        }
    }

    public String getWinnerName() {
        return gameContext.getData().getComponent(gameContext.getWinner(), Components.NAME);
    }
}
