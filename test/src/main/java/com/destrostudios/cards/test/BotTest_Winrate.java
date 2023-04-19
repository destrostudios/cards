package com.destrostudios.cards.test;

import com.destrostudios.cards.backend.application.modules.bot.CardsBotEval;

import java.util.HashMap;
import java.util.Random;

public class BotTest_Winrate extends BotTest {

    public static void main(String[] args) {
        new BotTest_Winrate().run();
    }

    @Override
    public void run() {
        super.run();
        int games = 0;
        HashMap<String, Integer> playerWins = new HashMap<>();
        Random actualRandom = new Random();
        while (true) {
            long seed = actualRandom.nextLong();
            System.out.println("Playing game " + (games + 1) + "... (seed = " + seed + ")");
            BotGame botGame = new BotGame(allCards, mode, queue, seed, false, true, (botSettings, player) -> {
                CardsBotEval.Weights weights = CardsBotEval.getDefaultWeights();
                if (player == 1) {
                    // Modify weights to compare
                }
                botSettings.evaluation = state -> CardsBotEval.eval(state, weights);
            });
            botGame.play();
            String winnerName = botGame.getWinnerName();
            playerWins.put(winnerName, playerWins.computeIfAbsent(winnerName, wn -> 0) + 1);
            games++;
            if ((games % 10) == 0) {
                System.out.println("---Stats---");
                int _games = games;
                playerWins.forEach((name, wins) -> {
                    System.out.println(name + ": " + wins + "/" + _games + " games won (" + Math.round((((float) wins) / _games) * 100) + "%)");
                });
                System.out.println("-----------");
            }
        }
    }
}
