package com.destrostudios.cards.test;

import com.destrostudios.cards.backend.application.BackendApplication;
import com.destrostudios.cards.backend.application.services.*;
import com.destrostudios.cards.backend.database.databases.Database;
import com.destrostudios.cards.shared.application.ApplicationSetup;
import com.destrostudios.cards.shared.model.Card;
import com.destrostudios.cards.shared.model.Mode;
import com.destrostudios.cards.shared.model.Queue;
import com.destrostudios.cards.shared.rules.GameConstants;
import org.slf4j.impl.SimpleLogger;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class BotTest {

    public static void main(String[] args) {
        ApplicationSetup.setup();

        System.setProperty(SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "ERROR");

        Database database = BackendApplication.getDatabase();
        CardService cardService = new CardService(database);
        FoilService foilService = new FoilService(database);
        CardListService cardListService = new CardListService(database, cardService, foilService);
        ModeService modeService = new ModeService(database, cardListService);
        QueueService queueService = new QueueService(database);

        List<Card> cards = cardService.getCards();
        Mode mode = modeService.getMode(GameConstants.MODE_NAME_CLASSIC);
        Queue queue = queueService.getQueue(GameConstants.QUEUE_NAME_BOT);

        int games = 0;
        HashMap<String, Integer> playerWins = new HashMap<>();
        Random actualRandom = new Random();
        while (true) {
            long seed = actualRandom.nextLong();
            System.out.println("Playing game " + (games + 1) + "... (seed = " + seed + ")");
            BotGame botGame = new BotGame(cards, mode, queue, seed);
            botGame.play();
            String winnerName = botGame.getWinnerName();
            playerWins.put(winnerName, playerWins.computeIfAbsent(winnerName, wn -> 0) + 1);
            games++;
            System.out.println("---Stats---");
            int _games = games;
            playerWins.forEach((name, wins) -> {
                System.out.println(name + ": " + wins + "/" + _games + " games won (" + Math.round((((float) wins) / _games) * 100) + "%)");
            });
            System.out.println("-----------");
        }
    }
}
