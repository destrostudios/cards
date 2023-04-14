package com.destrostudios.cards.test;

import com.destrostudios.cards.backend.application.BackendApplication;
import com.destrostudios.cards.backend.application.services.*;
import com.destrostudios.cards.backend.database.databases.Database;
import com.destrostudios.cards.shared.application.ApplicationSetup;
import com.destrostudios.cards.shared.model.Card;
import com.destrostudios.cards.shared.model.Mode;
import com.destrostudios.cards.shared.model.Queue;
import com.destrostudios.cards.shared.rules.GameConstants;
import com.destrostudios.cards.shared.rules.expressions.Expressions;
import org.slf4j.impl.SimpleLogger;

import java.util.List;

public class BotTest {

    protected Database database;
    protected CardService cardService;
    protected FoilService foilService;
    protected CardListService cardListService;
    protected ModeService modeService;
    protected QueueService queueService;

    protected List<Card> allCards;
    protected Mode mode;
    protected Queue queue;

    public void run() {
        ApplicationSetup.setup();
        Expressions.setup();
        System.setProperty(SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "ERROR");

        database = BackendApplication.getDatabase();
        cardService = new CardService(database);
        foilService = new FoilService(database);
        cardListService = new CardListService(database, cardService, foilService);
        modeService = new ModeService(database, cardListService);
        queueService = new QueueService(database);

        allCards = cardService.getCards();
        mode = modeService.getMode(GameConstants.MODE_NAME_CLASSIC);
        queue = queueService.getQueue(GameConstants.QUEUE_NAME_BOT);
    }
}
