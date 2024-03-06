package com.destrostudios.cards.backend.application.services;

import com.destrostudios.cards.backend.application.botgame.BotGame;
import com.destrostudios.cards.backend.application.services.internal.BotDeck;
import com.destrostudios.cards.backend.database.databases.Database;
import com.destrostudios.cards.backend.database.databases.QueryResult;
import com.destrostudios.cards.shared.model.*;
import com.destrostudios.cards.shared.model.internal.NewCardListCard;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameConstants;
import com.destrostudios.cards.shared.rules.PlayerInfo;
import com.destrostudios.cards.shared.rules.StartGameInfo;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@AllArgsConstructor
public class BotDeckService {

    private static final String INTRODUCTION_DECK_NAME = "Bot Introduction Deck";

    private Database database;
    private CardService cardService;
    private FoilService foilService;
    private CardListService cardListService;
    private ModeService modeService;
    private QueueService queueService;
    private EntityService entityService;

    public void generateDecks(long seed) {
        Random random = new Random(seed);
        database.transaction(() -> {
            deleteAllDecks();
            ArrayList<BotDeck> botDecks = new ArrayList<>();
            botDecks.add(generateIntroductionDeck());
            for (int i = 0; i < 2000; i++) {
                botDecks.add(generateDeck(random));
            }
            evaluate(botDecks, random);
        });
    }

    private void deleteAllDecks() {
        ArrayList<Integer> deckCardListIds = new ArrayList<>();
        try (QueryResult result = database.select("SELECT deck_card_list_id FROM bot_deck")) {
            while (result.next()) {
                deckCardListIds.add(result.getInteger("deck_card_list_id"));
            }
        }
        database.execute("DELETE FROM bot_deck");
        for (int deckCardListId : deckCardListIds) {
            cardListService.deleteCardList(deckCardListId);
        }
    }

    private BotDeck generateIntroductionDeck() {
        Foil foilNone = foilService.getFoil(GameConstants.FOIL_NAME_NONE);

        int deckCardListId = cardListService.createCardList();
        List<NewCardListCard> cards = cardService.getCards_Core().stream()
                .map(card -> new NewCardListCard(card.getId(), foilNone.getId(), 2))
                .collect(Collectors.toList());
        cardListService.updateCardList(deckCardListId, INTRODUCTION_DECK_NAME, cards);
        return createDeck(deckCardListId);
    }

    private BotDeck generateDeck(Random random) {
        List<Card> allCards = cardService.getCards();
        Foil foilNone = foilService.getFoil(GameConstants.FOIL_NAME_NONE);

        int deckCardListId = cardListService.createCardList();
        ArrayList<NewCardListCard> cards = new ArrayList<>();
        int cardsCount = 0;
        while (cardsCount < GameConstants.MAXIMUM_DECK_SIZE) {
            Card card = allCards.get(random.nextInt(allCards.size()));
            NewCardListCard newCardListCard = cards.stream()
                .filter(c -> c.getCardId() == card.getId())
                .findAny()
                .orElseGet(() -> {
                    NewCardListCard c = new NewCardListCard(card.getId(), foilNone.getId(), 0);
                    cards.add(c);
                    return c;
                });
            if (entityService.hasComponent(card.getId(), Components.LEGENDARY) && (newCardListCard.getAmount() > 0)) {
                continue;
            }
            newCardListCard.setAmount(newCardListCard.getAmount() + 1);
            cardsCount++;
        }
        cardListService.updateCardList(deckCardListId, "Bot Deck", cards);

        return createDeck(deckCardListId);
    }

    private BotDeck createDeck(int deckCardListId) {
        double elo = 1200;
        int evaluationGames = 0;
        try (QueryResult result = database.insert("INSERT INTO bot_deck (deck_card_list_id, elo, evaluation_games) VALUES (" + deckCardListId + ", " + elo + ", " + evaluationGames + ")")) {
            result.next();
            int id = result.getInteger(1);
            return new BotDeck(id, cardListService.getCardList(deckCardListId), elo, evaluationGames);
        }
    }

    private void evaluate(ArrayList<BotDeck> botDecks, Random random) {
        Mode modeClassic = modeService.getMode(GameConstants.MODE_NAME_CLASSIC);
        Queue queueBot = queueService.getQueue(GameConstants.QUEUE_NAME_BOT);
        for (int i = 0; i < 400000; i++) {
            if (((i + 1) % 10) == 0) {
                System.out.println("Playing round " + (i + 1) + "...");
            }

            BotDeck botDeck1 = botDecks.get(random.nextInt(botDecks.size()));
            BotDeck botDeck2;
            do {
                botDeck2 = botDecks.get(random.nextInt(botDecks.size()));
            } while (botDeck2 == botDeck1);

            StartGameInfo startGameInfo = new StartGameInfo(
                modeClassic,
                queueBot,
                "forest",
                new PlayerInfo[] {
                    new PlayerInfo(1, "Bot1", botDeck1.getDeckCardList()),
                    new PlayerInfo(2, "Bot2", botDeck2.getDeckCardList())
                }
            );
            long seed = random.nextLong();
            BotGame botGame = new BotGame(cardService.getCards(), startGameInfo, seed, false, false, (botSettings, player) -> {});
            botGame.play();

            int k = 20;

            double S_A;
            double S_B;
            if (botGame.getWinner().getId() == 1) {
                S_A = 1;
                S_B = 0;
            } else {
                S_A = 0;
                S_B = 1;
            }

            double elo1 = botDeck1.getElo();
            double elo2 = botDeck2.getElo();
            double winProbability1 = getEloWinProbability(elo1, elo2);
            double winProbability2 = (1 - winProbability1);
            double newElo1 = elo1 + (k * (S_A - winProbability1));
            double newElo2 = elo2 + (k * (S_B - winProbability2));
            botDeck1.setElo(newElo1);
            botDeck2.setElo(newElo2);
            botDeck1.setEvaluationGames(botDeck1.getEvaluationGames() + 1);
            botDeck2.setEvaluationGames(botDeck2.getEvaluationGames() + 1);
        }
        for (BotDeck botDeck : botDecks) {
            if (botDeck.getDeckCardList().getName().equals(INTRODUCTION_DECK_NAME)) {
                // TODO: SAVE
                cardListService.deleteCardList(botDeck.getDeckCardList().getId());
                database.execute("DELETE FROM bot_deck WHERE id = " + botDeck.getId());
            } else {
                database.execute("UPDATE bot_deck SET elo = " + botDeck.getElo() + ", evaluation_games = " + botDeck.getEvaluationGames() + " WHERE id = " + botDeck.getId());
                database.execute("UPDATE card_list SET name = 'Bot Deck (" + ((int) botDeck.getElo()) + ")' WHERE id = " + botDeck.getDeckCardList().getId());
            }
        }
    }

    private double getEloWinProbability(double elo1, double elo2) {
        return (1 / (1 + Math.pow(10, (elo2 - elo1) / 400)));
    }
}
