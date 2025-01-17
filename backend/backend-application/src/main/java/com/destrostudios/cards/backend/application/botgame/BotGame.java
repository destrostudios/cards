package com.destrostudios.cards.backend.application.botgame;

import com.destrostudios.cards.backend.application.modules.bot.CardsBotEval;
import com.destrostudios.cards.backend.application.modules.bot.CardsBotService;
import com.destrostudios.cards.backend.application.modules.bot.CardsBotState;
import com.destrostudios.cards.backend.application.modules.bot.SimpleRandom;
import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.model.Card;
import com.destrostudios.cards.shared.rules.*;
import com.destrostudios.cards.shared.rules.actions.*;
import com.destrostudios.cards.shared.rules.util.ArrayUtil;
import com.destrostudios.cards.shared.rules.util.DebugUtil;
import com.destrostudios.cards.shared.rules.util.SpellUtil;
import com.destrostudios.cards.shared.rules.util.StatsUtil;
import com.destrostudios.gametools.bot.BotActionReplay;
import com.destrostudios.gametools.bot.mcts.MctsBot;
import com.destrostudios.gametools.bot.mcts.MctsBotSettings;
import com.destrostudios.gametools.bot.mcts.TerminationType;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;

public class BotGame {

    public BotGame(List<Card> cards, StartGameInfo startGameInfo, long seed, boolean verbose, boolean botPerPlayer, BiConsumer<MctsBotSettings<CardsBotState, Action>, Integer> modifyBotSettings) {
        this.cards = cards;
        this.startGameInfo = startGameInfo;
        this.seed = seed;
        this.verbose = verbose;
        this.botPerPlayer = botPerPlayer;
        this.modifyBotSettings = modifyBotSettings;
    }
    private List<Card> cards;
    private StartGameInfo startGameInfo;
    private long seed;
    private boolean verbose;
    private boolean botPerPlayer;
    private BiConsumer<MctsBotSettings<CardsBotState, Action>, Integer> modifyBotSettings;
    protected GameContext gameContext;
    private MctsBotSettings[] botSettings;
    private MctsBot[] bots;
    private CardsBotState botState;

    public void play() {
        Random _random = new Random(seed);
        NetworkRandom random = new SimpleRandom(_random);
        Random _botRandomInternal = new Random(seed + 1);
        Random _botRandomGame = new Random(seed + 2);

        gameContext = new GameContext(startGameInfo, GameEventHandling.GLOBAL_INSTANCE) {

            private HashSet<Event> triggeredEvents = new HashSet<>();

            @Override
            protected void resolveEvents() {
                while (hasPendingEventHandler()) {
                    Event event = getEvents().getNextPendingEventHandler().event();
                    if (triggeredEvents.add(event)) {
                        onEventTrigger(event);
                    }
                    triggerNextEventHandler();
                }
            }
        };
        GameSetup.apply(gameContext.getData(), startGameInfo, cards, _random);

        applyAction(new GameStartAction(), random);

        int playerCount = (botPerPlayer ? 2 : 1);
        bots = new MctsBot[playerCount];
        botSettings = new MctsBotSettings[playerCount];
        for (int i = 0; i < bots.length; i++) {
            MctsBotSettings<CardsBotState, Action> settings = new MctsBotSettings<>();
            settings.termination = TerminationType.NODE_COUNT;
            settings.strength = 100;
            settings.evaluation = CardsBotEval::eval;
            settings.random = _botRandomInternal;
            modifyBotSettings.accept(settings, i);
            botSettings[i] = settings;
            MctsBot bot = new MctsBot<>(new CardsBotService(), settings);
            bots[i] = bot;
        }
        botState = new CardsBotState(gameContext, _botRandomGame);

        long gameStartNanos = System.nanoTime();
        int actionIndex = 0;
        while (!gameContext.isGameOver()) {
            long actionStartNanos = System.nanoTime();
            List<Action> actions = bots[getActiveBotIndex()].sortedActions(botState, botState.activeTeam());
            long actionDurationNanos = (System.nanoTime() - actionStartNanos);
            Action action = actions.get(0);
            if (verbose) {
                System.out.println(getActionSummaryDebugText(actionIndex, action, actions.size(), actionDurationNanos));
            }
            applyAction(action, random);
            for (MctsBot bot : bots) {
                bot.stepRoot(new BotActionReplay<>(action, ArrayUtil.EMPTY)); // TODO: Randomness?
            }
            actionIndex++;
        }
        if (verbose) {
            long gameDurationNanos = (System.nanoTime() - gameStartNanos);
            System.out.println("Game over, total duration = " + (gameDurationNanos / 1_000_000) + "ms, winner = " + gameContext.getWinner() + ".");
        }
    }

    protected void onEventTrigger(Event event) {

    }

    protected float getActiveBotActivePlayerEval() {
        MctsBotSettings<CardsBotState, Action> settings = botSettings[getActiveBotIndex()];
        return settings.evaluation.apply(botState)[botState.activeTeam()];
    }

    private int getActiveBotIndex() {
        return (botPerPlayer ? botState.activeTeam() : 0);
    }

    private String getActionSummaryDebugText(int actionIndex, Action action, int actionsCount, long actionDurationNanos) {
        int player = botState.activeTeam();
        String text = "Action #" + actionIndex + ": Player #" + player;
        text += " { health = " + StatsUtil.getEffectiveHealth(gameContext.getData(), player);
        text += ", library = " + gameContext.getData().getComponent(player, Components.Player.LIBRARY_CARDS).size() + "}";
        text += " => " + getActionDebugText(action);
        text += " (" + actionsCount + " possible actions";
        text += ", in " + (actionDurationNanos / 1_000_000) + "ms)";
        return text;
    }

    private String getActionDebugText(Action action) {
        if (action instanceof CastSpellAction castSpellAction) {
            return "CastSpellEvent { source = " + getEntityDebugText(castSpellAction.getSource()) + ", spell = " + getEntityDebugText_Spell(castSpellAction.getSpell()) + ", targets = " + getEntityDebugText(castSpellAction.getTargets()) + ", options = " + castSpellAction.getOptions() + " }";
        } else if (action instanceof EndTurnAction) {
            return "EndTurnEvent";
        } else if (action instanceof MulliganAction mulliganAction) {
            return "MulliganEvent { cards = " + getEntityDebugText(mulliganAction.getCards()) + " }";
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

    protected void applyAction(Action action, NetworkRandom random) {
        gameContext.fireAndResolveAction(action, random);
    }

    public PlayerInfo getWinner() {
        return gameContext.getStartGameInfo().getPlayers()[gameContext.getWinner()];
    }
}
