package com.destrostudios.cards.test;

import com.destrostudios.cards.backend.application.BackendApplication;
import com.destrostudios.cards.backend.application.services.*;
import com.destrostudios.cards.backend.database.databases.Database;
import com.destrostudios.cards.shared.application.ApplicationSetup;
import com.destrostudios.cards.shared.files.FileManager;
import com.destrostudios.cards.shared.model.*;
import com.destrostudios.cards.shared.rules.GameConstants;
import com.destrostudios.cards.shared.rules.PlayerInfo;
import com.destrostudios.cards.shared.rules.StartGameInfo;

import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public class BotTest {

    protected Database database;
    protected CardService cardService;
    protected PowerService powerService;
    protected FoilService foilService;
    protected CardListService cardListService;
    protected ModeService modeService;
    protected QueueService queueService;

    protected List<Card> allCards;
    private Mode defaultMode;
    private Queue defaultQueue;

    public void run() {
        ApplicationSetup.setup();
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "ERROR");

        database = BackendApplication.getDatabase();
        cardService = new CardService(database);
        powerService = new PowerService(database);
        foilService = new FoilService(database);
        cardListService = new CardListService(database, cardService, foilService);
        modeService = new ModeService(database, powerService, cardListService);
        queueService = new QueueService(database);

        allCards = cardService.getCards();
        defaultMode = modeService.getMode(GameConstants.MODE_NAME_CLASSIC);
        defaultQueue = queueService.getQueue(GameConstants.QUEUE_NAME_BOT);
    }

    protected StartGameInfo getStartGameInfo_CompletelyRandom(Random random) {
        return getDefaultStartGameInfo(null, null, getRandomPower(random), getRandomPower(random));
    }

    protected StartGameInfo getStartGameInfo_Brawl(Random random) {
        return getStartGameInfo(getRandomBrawlDeck(random), getRandomBrawlDeck(random));
    }

    protected StartGameInfo getStartGameInfo_Brawl(String deckName1, String deckName2) {
        return getStartGameInfo(getBrawlDeck(deckName1), getBrawlDeck(deckName2));
    }

    protected StartGameInfo getStartGameInfo(ModeDeck modeDeck1, ModeDeck modeDeck2) {
        return getDefaultStartGameInfo(
            modeDeck1.getDeckCardList(),
            modeDeck2.getDeckCardList(),
            modeDeck1.getPower(),
            modeDeck2.getPower()
        );
    }

    protected StartGameInfo getDefaultStartGameInfo(CardList deck1, CardList deck2, Power power1, Power power2) {
        return new StartGameInfo(
            defaultMode,
            defaultQueue,
            "forest",
            new PlayerInfo[]{
                new PlayerInfo(1, "Bot1", new Power[] { power1 }, deck1),
                new PlayerInfo(2, "Bot2", new Power[] { power2 }, deck2)
            }
        );
    }

    protected ModeDeck getRandomBrawlDeck(Random random) {
        Mode mode = modeService.getMode(GameConstants.MODE_NAME_BRAWL);
        return mode.getDecks().get(random.nextInt(mode.getDecks().size()));
    }

    protected ModeDeck getBrawlDeck(String deckName) {
        Mode mode = modeService.getMode(GameConstants.MODE_NAME_BRAWL);
        return mode.getDecks().stream().filter(deck -> deck.getDeckCardList().getName().equals(deckName)).findAny().orElseThrow();
    }

    protected Power getRandomPower(Random random) {
        List<Power> powers = powerService.getPowers();
        return powers.get(random.nextInt(powers.size()));
    }

    protected void continuouslyWriteFile(String fileName, Supplier<String> getContent) {
        TestUtil.runInterval(() -> writeFile(fileName, getContent.get()), 5000);
    }

    protected void writeFile(String fileName, String content) {
        String filePath = System.getenv("TARGET_DIRECTORY") + fileName;
        FileManager.putFileContent(filePath, content);
    }
}
