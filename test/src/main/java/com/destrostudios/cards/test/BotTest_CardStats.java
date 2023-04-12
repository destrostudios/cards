package com.destrostudios.cards.test;

import com.destrostudios.cards.shared.entities.IntList;
import com.destrostudios.cards.shared.entities.SimpleEntityData;
import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameContext;
import com.destrostudios.cards.shared.rules.cards.CastSpellEvent;
import com.destrostudios.cards.shared.rules.game.turn.EndTurnEvent;
import com.destrostudios.cards.shared.rules.util.SpellUtil;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import lombok.Setter;

import java.util.*;

public class BotTest_CardStats extends BotTest {

    public static void main(String[] args) {
        new BotTest_CardStats().run();
    }

    @Setter
    class CardStats {
        int gamesWhereInHand;
        int gamesWherePlayed;
        int gamesWhereInHandWon;
        int gamesWherePlayedWon;
        int totalEndOfTurnsInHand;

        public float getWinrateWhenDrawn() {
            return ((((float) gamesWhereInHandWon) / gamesWhereInHand) * 100);
        }

        public float getWinrateWhenPlayed() {
            return ((((float) gamesWherePlayedWon) / gamesWherePlayed) * 100);
        }

        public float getAverageEndOfTurnsInHand() {
            return (((float) totalEndOfTurnsInHand) / gamesWhereInHand);
        }
    }

    private HashMap<String, CardStats> cardStats;

    @Override
    public void run() {
        super.run();
        int games = 0;
        Random actualRandom = new Random();
        cardStats = new HashMap<>();
        while (true) {
            long seed = actualRandom.nextLong();
            System.out.println("Playing game " + (games + 1) + "... (seed = " + seed + ")");
            BotGame botGame = new BotGame(allCards, mode, queue, seed, false, (botSettings, player) -> {}) {

                private HashSet<Integer> cardsInHand = new HashSet<>();
                private HashSet<Integer> cardsPlayed = new HashSet<>();
                private HashMap<Integer, Integer> endOfTurnsInHand = new HashMap<>();

                @Override
                public void play() {
                    super.play();
                    SimpleEntityData data = gameContext.getData();
                    int winner = gameContext.getWinner();
                    for (int card : cardsInHand) {
                        CardStats stats = getStats(gameContext, card);
                        stats.gamesWhereInHand++;
                        if (data.getComponent(card, Components.OWNED_BY) == winner) {
                            stats.gamesWhereInHandWon++;
                        }
                    }
                    for (int card : cardsPlayed) {
                        CardStats stats = getStats(gameContext, card);
                        stats.gamesWherePlayed++;
                        if (data.getComponent(card, Components.OWNED_BY) == winner) {
                            stats.gamesWherePlayedWon++;
                        }
                    }
                    endOfTurnsInHand.forEach((card, endOfTurns) -> {
                        CardStats stats = getStats(gameContext, card);
                        stats.totalEndOfTurnsInHand += endOfTurns;
                    });
                }

                @Override
                protected void applyAction(Event action, NetworkRandom random) {
                    super.applyAction(action, random);
                    SimpleEntityData data = gameContext.getData();
                    IntList handCards = data.query(Components.HAND).list();
                    for (int handCard : handCards) {
                        cardsInHand.add(handCard);
                        if (action instanceof EndTurnEvent endTurnEvent) {
                            int owner = data.getComponent(handCard, Components.OWNED_BY);
                            if (endTurnEvent.player == owner) {
                                endOfTurnsInHand.put(handCard, endOfTurnsInHand.computeIfAbsent(handCard, hc -> 0) + 1);
                            }
                        }
                    }
                    if (action instanceof CastSpellEvent castSpellEvent) {
                        if (SpellUtil.isDefaultCastFromHandSpell(data, castSpellEvent.spell)) {
                            cardsPlayed.add(castSpellEvent.source);
                        }
                    }
                }
            };
            botGame.play();
            games++;
            System.out.println("---Stats---");
            List<Map.Entry<String, CardStats>> sortedStatsEntries = cardStats.entrySet().stream()
                    .sorted(Comparator.comparing(entry -> -1 * entry.getValue().getWinrateWhenDrawn()))
                    .toList();
            for (Map.Entry<String, CardStats> entry : sortedStatsEntries) {
                CardStats stats = entry.getValue();
                System.out.println(entry.getKey() + "\t\t\t" + stats.getWinrateWhenDrawn() + "% wr-drawn (" + stats.gamesWhereInHand + " games)\t\t\t" + stats.getWinrateWhenPlayed() + "% wr-played (" + stats.gamesWherePlayed + " games)\t\t\t" + stats.getAverageEndOfTurnsInHand() + " avg-eot-hands (" + stats.gamesWhereInHand + " games)");
            }
            System.out.println("-----------");
        }
    }

    private CardStats getStats(GameContext gameContext, int entity) {
        String name =  gameContext.getData().getComponent(entity, Components.NAME);
        return cardStats.computeIfAbsent(name, n -> new CardStats());
    }
}
