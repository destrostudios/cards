package com.destrostudios.cards.test;

import com.destrostudios.cards.backend.application.modules.bot.CardsBotEval;
import com.destrostudios.cards.backend.application.modules.bot.CardsBotService;
import com.destrostudios.cards.backend.application.modules.bot.CardsBotState;
import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.model.Card;
import com.destrostudios.cards.shared.model.Mode;
import com.destrostudios.cards.shared.model.Queue;
import com.destrostudios.cards.shared.rules.*;
import com.destrostudios.cards.shared.rules.cards.CastSpellEvent;
import com.destrostudios.cards.shared.rules.cards.MulliganEvent;
import com.destrostudios.cards.shared.rules.game.GameStartEvent;
import com.destrostudios.cards.shared.rules.game.turn.EndTurnEvent;
import com.destrostudios.cards.shared.rules.util.DebugUtil;
import com.destrostudios.cards.shared.rules.util.SpellUtil;
import com.destrostudios.gametools.bot.BotActionReplay;
import com.destrostudios.gametools.bot.mcts.MctsBot;
import com.destrostudios.gametools.bot.mcts.MctsBotSettings;
import com.destrostudios.gametools.bot.mcts.TerminationType;
import com.destrostudios.gametools.network.server.modules.game.MasterRandom;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;

import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;

public class BotGame {

    public BotGame(List<Card> cards, Mode mode, Queue queue, long seed, boolean verbose, BiConsumer<MctsBotSettings<CardsBotState, Event>, Integer> modifyBotSettings) {
        this.cards = cards;
        this.mode = mode;
        this.queue = queue;
        this.seed = seed;
        this.verbose = verbose;
        this.modifyBotSettings = modifyBotSettings;
    }
    private List<Card> cards;
    private Mode mode;
    private Queue queue;
    private long seed;
    private boolean verbose;
    private BiConsumer<MctsBotSettings<CardsBotState, Event>, Integer> modifyBotSettings;
    protected GameContext gameContext;
    private MctsBot[] bots;

    public void play() {
        Random _random = new Random(seed);
        MasterRandom random = new MasterRandom(_random);

        StartGameInfo startGameInfo = new StartGameInfo(
            mode,
            queue,
            "forest",
            new PlayerInfo[] {
                new PlayerInfo(1, "Bot1", null),
                new PlayerInfo(2, "Bot2", null)
            }
        );
        gameContext = new GameContext(startGameInfo, GameEventHandling.GLOBAL_INSTANCE);
        GameSetup.apply(gameContext.getData(), startGameInfo, cards, _random);

        applyAction(new GameStartEvent(), random);

        bots = new MctsBot[2];
        for (int i = 0; i < bots.length; i++) {
            MctsBotSettings<CardsBotState, Event> botSettings = new MctsBotSettings<>();
            botSettings.maxThreads = 8;
            botSettings.termination = TerminationType.NODE_COUNT;
            botSettings.strength = 100;
            botSettings.evaluation = CardsBotEval::eval;
            botSettings.random = _random;
            modifyBotSettings.accept(botSettings, i);
            MctsBot bot = new MctsBot<>(new CardsBotService(), botSettings);
            bots[i] = bot;
        }
        Random botRandom = new Random(seed + 1);
        CardsBotState botState = new CardsBotState(gameContext, botRandom);

        long gameStartNanos = System.nanoTime();
        int actionIndex = 0;
        while (!gameContext.isGameOver()) {
            int activePlayer = botState.activeTeam();
            long actionStartNanos = System.nanoTime();
            MctsBot activeBot = bots[activePlayer];
            List<Event> actions = activeBot.sortedActions(botState, activePlayer);
            long actionDurationNanos = (System.nanoTime() - actionStartNanos);
            Event action = actions.get(0);
            if (verbose) {
                System.out.println("Player #" + activePlayer + " Action #" + (actionIndex + 1) + " => " + getActionDebugText(action) + "\t(from " + actions.size() + " possible actions, in " + (actionDurationNanos / 1_000_000) + "ms)");
            }
            applyAction(action, random);
            for (MctsBot bot : bots) {
                bot.stepRoot(new BotActionReplay<>(action, new int[0])); // TODO: Randomness?
            }
            actionIndex++;
        }
        if (verbose) {
            long gameDurationNanos = (System.nanoTime() - gameStartNanos);
            System.out.println("Game over, total duration = " + (gameDurationNanos / 1_000_000) + "ms, winner = " + gameContext.getWinner() + ".");
        }
    }

    private String getActionDebugText(Event action) {
        if (action instanceof CastSpellEvent castSpellEvent) {
            return "CastSpellEvent { source = " + getEntityDebugText(castSpellEvent.source) + ", spell = " + getEntityDebugText_Spell(castSpellEvent.spell) + ", targets = " + getEntityDebugText(castSpellEvent.targets) + " }";
        } else if (action instanceof EndTurnEvent) {
            return "EndTurnEvent";
        } else if (action instanceof MulliganEvent mulliganEvent){
            return "MulliganEvent { cards = " + getEntityDebugText(mulliganEvent.cards) + " }";
        }
        return action.toString();
    }

    private String getEntityDebugText_Spell(int spell) {
        if (SpellUtil.isDefaultCastFromHandSpell(gameContext.getData(), spell)) {
            return "[PlayFromHand]";
        } else if (SpellUtil.isDefaultAttackSpell(gameContext.getData(), spell)) {
            return "[Attack]";
        }
        return getEntityDebugText(spell);
    }

    private String getEntityDebugText(int... entities) {
        return new DebugUtil.EntityDebugText_Array(gameContext.getData(), entities).toString();
    }

    protected void applyAction(Event action, NetworkRandom random) {
        gameContext.fireAndResolveEvent(action, random);
    }

    public String getWinnerName() {
        return gameContext.getData().getComponent(gameContext.getWinner(), Components.NAME);
    }
}
