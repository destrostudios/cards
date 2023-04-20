package com.destrostudios.cards.test;

import com.destrostudios.cards.backend.application.modules.bot.CardsBotState;
import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.entities.IntList;
import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.model.Card;
import com.destrostudios.cards.shared.model.Mode;
import com.destrostudios.cards.shared.model.Queue;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.battle.DamageEvent;
import com.destrostudios.cards.shared.rules.battle.HealEvent;
import com.destrostudios.cards.shared.rules.cards.CastSpellEvent;
import com.destrostudios.cards.shared.rules.game.turn.EndTurnEvent;
import com.destrostudios.cards.shared.rules.util.SpellUtil;
import com.destrostudios.gametools.bot.mcts.MctsBotSettings;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class BotGame_WithCardStats extends BotGame {

    public BotGame_WithCardStats(List<Card> cards, Mode mode, Queue queue, long seed, boolean verbose, boolean botPerPlayer, BiConsumer<MctsBotSettings<CardsBotState, Event>, Integer> modifyBotSettings) {
        super(cards, mode, queue, seed, verbose, botPerPlayer, modifyBotSettings);
    }
    private HashMap<Integer, CardStatsGame> cardStats = new HashMap<>();
    private ArrayList<PlayedCardThisTurn> playedCardsThisTurn = new ArrayList<>();

    @Override
    protected void onEventTrigger(Event event) {
        super.onEventTrigger(event);
        if (event instanceof DamageEvent damageEvent) {
            CardStatsGame stats = getStats(damageEvent.source);
            stats.damageDealt += damageEvent.damage;
        } else if (event instanceof HealEvent healEvent) {
            CardStatsGame stats = getStats(healEvent.source);
            stats.healthHealed += healEvent.heal;
        }
    }

    @Override
    protected void applyAction(Event action, NetworkRandom random) {
        EntityData data = gameContext.getData();
        if (action instanceof CastSpellEvent castSpellEvent) {
            if (SpellUtil.isDefaultCastFromHandSpell(data, castSpellEvent.spell)) {
                PlayedCardThisTurn playedCardThisTurn = new PlayedCardThisTurn();
                playedCardThisTurn.card = castSpellEvent.source;
                playedCardThisTurn.evalBeforePlayed = getActiveBotActivePlayerEval();
                playedCardsThisTurn.add(playedCardThisTurn);
            }
        } else if (action instanceof EndTurnEvent) {
            playedCardsThisTurn.forEach(playedCard -> {
                CardStatsGame stats = getStats(playedCard.card);
                CardStatsAction actionStats = new CardStatsAction();
                float eval = getActiveBotActivePlayerEval();
                actionStats.evalAtTurnEndAfterPlayed = eval;
                actionStats.deltaEvalAtTurnEndAfterPlayed = (eval - playedCard.evalBeforePlayed);
                int owner = data.getComponent(playedCard.card, Components.OWNED_BY);
                actionStats.ownerAvailableMana = data.getComponent(owner, Components.AVAILABLE_MANA);
                stats.playActions.add(actionStats);
            });
            playedCardsThisTurn.clear();
        }
        IntList handCards = data.list(Components.Zone.HAND);
        for (int handCard : handCards) {
            CardStatsGame stats = getStats(handCard);
            stats.inHand = true;
            if (action instanceof EndTurnEvent endTurnEvent) {
                int owner = data.getComponent(handCard, Components.OWNED_BY);
                if (endTurnEvent.player == owner) {
                    stats.endOfTurnsInHand++;
                }
            }
        }
        super.applyAction(action, random);
    }

    @Override
    public void play() {
        super.play();
        EntityData data = gameContext.getData();
        int winner = gameContext.getWinner();
        for (int card : data.list(Components.OWNED_BY)) {
            int owner = data.getComponent(card, Components.OWNED_BY);
            CardStatsGame stats = getStats(card);
            stats.win = (owner == winner);
        }
    }

    private CardStatsGame getStats(int card) {
        return cardStats.computeIfAbsent(card, c -> new CardStatsGame());
    }

    public void addToTotalStats(HashMap<String, CardStatsTotal> totalCardStats) {
        cardStats.forEach((card, stats) -> {
            String name =  gameContext.getData().getComponent(card, Components.NAME);
            CardStatsTotal totalStats = totalCardStats.computeIfAbsent(name, n -> new CardStatsTotal());
            totalStats.games.add(stats);
        });
    }

    public static class PlayedCardThisTurn {
        private int card;
        private float evalBeforePlayed;
    }

    public static class CardStatsAction {
        private int ownerAvailableMana;
        private float evalAtTurnEndAfterPlayed;
        private float deltaEvalAtTurnEndAfterPlayed;
    }

    public static class CardStatsGame {
        private boolean inHand;
        private int endOfTurnsInHand;
        private List<CardStatsAction> playActions = new ArrayList<>();
        private int damageDealt;
        private int healthHealed;
        private boolean win;

        public boolean wasPlayed() {
            return playActions.size() > 0;
        }
    }

    public static class CardStatsTotal {
        private List<CardStatsGame> games = new ArrayList<>();

        public Float getWinrateWhenDrawn() {
            return getRatio(getGamesCount(game -> game.inHand && game.win), getGamesWhereDrawn());
        }

        public int getGamesWhereDrawn() {
            return getGamesCount(game -> game.inHand);
        }

        public Float getWinrateWhenPlayed() {
            return getRatio(getGamesCount(game -> game.wasPlayed() && game.win), getGamesWherePlayed());
        }

        public int getGamesWherePlayed() {
            return getGamesCount(CardStatsGame::wasPlayed);
        }

        public List<Integer> getEndOfTurnsInHand() {
            return getGameStat(game -> game.endOfTurnsInHand);
        }

        public List<Integer> getDamageDealt() {
            return getGameStat(game -> game.damageDealt);
        }

        public List<Integer> getHealthHealed() {
            return getGameStat(game -> game.healthHealed);
        }

        public List<Float> getEvalAtTurnEndAfterPlayed() {
            return getActionStat(action -> action.evalAtTurnEndAfterPlayed);
        }

        public List<Float> getDeltaEvalAtTurnEndAfterPlayed() {
            return getActionStat(action -> action.deltaEvalAtTurnEndAfterPlayed);
        }

        public List<Integer> getOwnerAvailableManaWhenPlayed() {
            return getActionStat(action -> action.ownerAvailableMana);
        }

        private <T> List<T> getGameStat(Function<CardStatsGame, T> getValue) {
            return games.stream().map(getValue).toList();
        }

        private <T> List<T> getActionStat(Function<CardStatsAction, T> getValue) {
            return games.stream().flatMap(game -> game.playActions.stream()).map(getValue).toList();
        }

        private int getGamesCount(Predicate<CardStatsGame> predicate) {
            return getGames(predicate).size();
        }

        private List<CardStatsGame> getGames(Predicate<CardStatsGame> predicate) {
            return games.stream().filter(predicate).toList();
        }

        private Float getRatio(int value1, int value2) {
            return ((value2 > 0) ? (((float) value1) / value2) : null);
        }
    }
}
