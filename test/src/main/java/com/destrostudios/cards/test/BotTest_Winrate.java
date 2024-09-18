package com.destrostudios.cards.test;

import com.destrostudios.cards.backend.application.botgame.BotGame;
import com.destrostudios.cards.backend.application.modules.bot.CardsBotState;
import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.files.FileManager;
import com.destrostudios.cards.shared.rules.PlayerInfo;
import com.destrostudios.cards.shared.rules.StartGameInfo;
import com.destrostudios.gametools.bot.mcts.MctsBotSettings;
import lombok.AllArgsConstructor;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@AllArgsConstructor
public abstract class BotTest_Winrate extends BotTest {

    class Matchup {
        private int games;
        private int wins;
    }

    private boolean botPerPlayer;

    @Override
    public void run() {
        super.run();
        AtomicInteger games = new AtomicInteger();
        ConcurrentHashMap<String, ConcurrentHashMap<String, Matchup>> matchups = new ConcurrentHashMap<>();
        TestUtil.runOnManyProcessors(() -> {
            Random actualRandom = new Random();
            while (true) {
                long seed = actualRandom.nextLong();
                StartGameInfo startGameInfo = getStartGameInfo(actualRandom);
                String player1Key = getPlayerKey(startGameInfo.getPlayers()[0]);
                String player2Key = getPlayerKey(startGameInfo.getPlayers()[1]);

                BotGame botGame = new BotGame(allCards, startGameInfo, seed, false, botPerPlayer, this::modifyBotSettings);
                botGame.play();

                Matchup matchupForPlayer1 = matchups.computeIfAbsent(player1Key, (_) -> new ConcurrentHashMap<>()).computeIfAbsent(player2Key, (_) ->  new Matchup());
                Matchup matchupForPlayer2 = matchups.computeIfAbsent(player2Key, (_) -> new ConcurrentHashMap<>()).computeIfAbsent(player1Key, (_) ->  new Matchup());
                matchupForPlayer1.games += 1;
                matchupForPlayer2.games += 1;
                String winnerKey = getPlayerKey(botGame.getWinner());
                if (winnerKey.equals(player1Key)) {
                    matchupForPlayer1.wins += 1;
                } else {
                    matchupForPlayer2.wins += 1;
                }

                games.incrementAndGet();
            }
        });
        TestUtil.runInterval(() -> {
            System.out.println("Games: " + games.get());
            FileManager.putFileContent("./winrates.csv", getCsv(matchups));
        }, 5000);
    }

    protected StartGameInfo getStartGameInfo(Random random) {
        return getDefaultStartGameInfo();
    }

    protected String getPlayerKey(PlayerInfo playerInfo) {
        return playerInfo.getLogin();
    }

    protected void modifyBotSettings(MctsBotSettings<CardsBotState, Event> settings, int player) {

    }

    private String getCsv(ConcurrentHashMap<String, ConcurrentHashMap<String, Matchup>> matchups) {
        String csv = "player-1";
        csv += ",player-2";
        csv += ",games";
        csv += ",winrate-player-1";
        csv += ",winrate-player-2";
        for (Map.Entry<String, ConcurrentHashMap<String, Matchup>> player1Entry : matchups.entrySet()) {
            String player1Key = player1Entry.getKey();
            int totalGames = 0;
            int totalWins = 0;
            // Specific
            for (Map.Entry<String, Matchup> player2Entry : player1Entry.getValue().entrySet()) {
                String player2Key = player2Entry.getKey();
                Matchup matchup = player2Entry.getValue();
                float winratePlayer1 = (((float) matchup.wins) / matchup.games);
                float winratePlayer2 = (1 - winratePlayer1);
                csv += "\n\"" + player1Key + "\"";
                csv += "," + player2Key;
                csv += "," + matchup.games;
                csv += "," + winratePlayer1;
                csv += "," + winratePlayer2;

                totalGames += matchup.games;
                totalWins += matchup.wins;
            }
            // Total
            float totalWinratePlayer1 = (((float) totalWins) / totalGames);
            csv += "\n\"" + player1Key + "\"";
            csv += "," + "[All]";
            csv += "," + totalGames;
            csv += "," + totalWinratePlayer1;
            csv += ",";
        }
        return csv;
    }
}
