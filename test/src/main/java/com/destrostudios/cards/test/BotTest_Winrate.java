package com.destrostudios.cards.test;

import com.destrostudios.cards.backend.application.modules.bot.CardsBotState;
import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.PlayerInfo;
import com.destrostudios.cards.shared.rules.StartGameInfo;
import com.destrostudios.gametools.bot.mcts.MctsBotSettings;
import lombok.AllArgsConstructor;

import java.util.HashMap;
import java.util.Random;

@AllArgsConstructor
public abstract class BotTest_Winrate extends BotTest {

    private boolean botPerPlayer;

    @Override
    public void run() {
        super.run();
        int games = 0;
        HashMap<String, Integer> winsByName = new HashMap<>();
        Random actualRandom = new Random();
        while (true) {
            long seed = actualRandom.nextLong();
            System.out.println("Playing game " + (games + 1) + "... (seed = " + seed + ")");
            BotGame botGame = new BotGame(allCards, getStartGameInfo(actualRandom), seed, false, botPerPlayer, this::modifyBotSettings);
            botGame.play();
            String winnerName = getWinnerName(botGame.getWinner());
            winsByName.put(winnerName, winsByName.computeIfAbsent(winnerName, wn -> 0) + 1);
            games++;
            if ((games % 50) == 0) {
                System.out.println("---Stats---");
                int _games = games;
                winsByName.forEach((name, wins) -> {
                    System.out.println(name + ": " + wins + "/" + _games + " games won (" + Math.round((((float) wins) / _games) * 100) + "%)");
                });
                System.out.println("-----------");
            }
        }
    }

    protected StartGameInfo getStartGameInfo(Random random) {
        return getDefaultStartGameInfo();
    }

    protected void modifyBotSettings(MctsBotSettings<CardsBotState, Event> settings, int player) {

    }

    protected String getWinnerName(PlayerInfo winner) {
        return winner.getLogin();
    }
}
