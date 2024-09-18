package com.destrostudios.cards.test;

import com.destrostudios.cards.backend.application.botgame.BotGame_WithCardStats;
import com.destrostudios.cards.shared.files.FileManager;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static com.destrostudios.cards.test.TestUtil.*;

public class BotTest_CardStats extends BotTest {

    public static void main(String[] args) {
        new BotTest_CardStats().run();
    }

    @Override
    public void run() {
        super.run();
        AtomicInteger games = new AtomicInteger();
        ConcurrentHashMap<String, BotGame_WithCardStats.CardStatsTotal> totalCardStats = new ConcurrentHashMap<>();
        TestUtil.runOnManyProcessors(() -> {
            Random actualRandom = new Random();
            while (true) {
                long seed = actualRandom.nextLong();
                System.out.println("Playing game " + games.incrementAndGet() + "... (seed = " + seed + ")");
                BotGame_WithCardStats botGame = new BotGame_WithCardStats(allCards, getDefaultStartGameInfo(), seed, false, false, (botSettings, player) -> {});
                botGame.play();
                botGame.addToTotalStats(totalCardStats);
            }
        });
        TestUtil.runInterval(() -> {
            FileManager.putFileContent("./stats.csv", getCsv(totalCardStats));
        }, 5000);
    }

    private String getCsv(Map<String, BotGame_WithCardStats.CardStatsTotal> cardStats) {
        String csv = "card";
        csv += ",wr-when-drawn";
        csv += ",games-where-drawn";
        csv += ",wr-when-played";
        csv += ",games-where-played";
        csv += ",avg-end-of-turns-in-hand";
        csv += ",med-end-of-turns-in-hand";
        csv += ",avg-damage-dealt";
        csv += ",med-damage-dealt";
        csv += ",avg-health-healed";
        csv += ",med-health-healed";
        csv += ",avg-available-mana-when-played";
        csv += ",med-available-mana-when-played";
        csv += ",avg-eval-at-turn-end-after-played";
        csv += ",med-eval-at-turn-end-after-played";
        csv += ",avg-delta-eval-at-turn-end-after-played";
        csv += ",med-delta-eval-at-turn-end-after-played";
        for (Map.Entry<String, BotGame_WithCardStats.CardStatsTotal> entry : cardStats.entrySet()) {
            BotGame_WithCardStats.CardStatsTotal stats = entry.getValue();
            csv += "\n\"" + entry.getKey() + "\"";
            csv += "," + toStringNullable(stats.getWinrateWhenDrawn());
            csv += "," + stats.getGamesWhereDrawn();
            csv += "," + toStringNullable(stats.getWinrateWhenPlayed());
            csv += "," + stats.getGamesWherePlayed();
            List<Integer> endOfTurnsInHand = stats.getEndOfTurnsInHand();
            csv += "," + toStringNullable(getAverage_Int(endOfTurnsInHand));
            csv += "," + toStringNullable(getMedian_Int(endOfTurnsInHand));
            List<Integer> damageDealt = stats.getDamageDealt();
            csv += "," + toStringNullable(getAverage_Int(damageDealt));
            csv += "," + toStringNullable(getMedian_Int(damageDealt));
            List<Integer> healthHealed = stats.getHealthHealed();
            csv += "," + toStringNullable(getAverage_Int(healthHealed));
            csv += "," + toStringNullable(getMedian_Int(healthHealed));
            List<Integer> ownerAvailableManaWhenPlayed = stats.getOwnerAvailableManaWhenPlayed();
            csv += "," + toStringNullable(getAverage_Int(ownerAvailableManaWhenPlayed));
            csv += "," + toStringNullable(getMedian_Int(ownerAvailableManaWhenPlayed));
            List<Float> evalTurnAtEndAfterPlayed = stats.getEvalAtTurnEndAfterPlayed();
            csv += "," + toStringNullable(getAverage_Float(evalTurnAtEndAfterPlayed));
            csv += "," + toStringNullable(getMedian_Float(evalTurnAtEndAfterPlayed));
            List<Float> deltaEvalTurnAtEndAfterPlayed = stats.getDeltaEvalAtTurnEndAfterPlayed();
            csv += "," + toStringNullable(getAverage_Float(deltaEvalTurnAtEndAfterPlayed));
            csv += "," + toStringNullable(getMedian_Float(deltaEvalTurnAtEndAfterPlayed));
        }
        return csv;
    }

    private String toStringNullable(Object object) {
        return ((object != null) ? object.toString() : "");
    }
}
