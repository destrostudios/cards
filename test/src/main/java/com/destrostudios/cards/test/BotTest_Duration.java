package com.destrostudios.cards.test;

import com.destrostudios.cards.backend.application.botgame.BotGame;

import java.util.*;

public class BotTest_Duration extends BotTest {

    public static void main(String[] args) {
        new BotTest_Duration().run();
    }

    @Override
    public void run() {
        super.run();
        int games = 0;
        Random random = new Random(123);
        ArrayList<Long> durations = new ArrayList<>();
        while (true) {
            long seed = random.nextLong();
            System.out.println("Playing game " + (games + 1) + "... (seed = " + seed + ")");
            BotGame botGame = new BotGame(allCards, getDefaultStartGameInfo(), seed, false, false, (botSettings, player) -> {});
            long start = System.nanoTime();
            botGame.play();
            long duration = (System.nanoTime() - start);
            games++;
            // Wait until warm
            if (games > 5) {
                durations.add(duration);
                System.out.println("Duration: " + TestUtil.getMedian_Long(durations) + " median, " + TestUtil.getAverage_Long(durations) + " average");
            }
        }
    }
}
