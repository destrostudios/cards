package com.destrostudios.cards.test;

import com.destrostudios.cards.backend.application.botgame.BotGame;
import com.destrostudios.cards.shared.model.Card;
import com.destrostudios.cards.shared.model.CardList;
import com.destrostudios.cards.shared.model.CardListCard;
import com.destrostudios.cards.shared.rules.GameConstants;
import com.destrostudios.cards.shared.rules.StartGameInfo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class BotTest_DeckGenerator extends BotTest {

    public static void main(String[] args) {
        new BotTest_DeckGenerator().run();
    }

    private CardList deck;
    private int round = 0;
    private int mutations = 0;

    @Override
    public void run() {
        super.run();
        deck = createRandomDeck();
        Random randomSeed = new Random(123);
        while (true) {
            if ((round % 10) == 0) {
                System.out.println("-----------");
                for (CardListCard cardListCard : deck.getCards()) {
                    System.out.println(cardListCard.getCard().getPath());
                }
                System.out.println("Round " + (round + 1) + " (" + mutations + " mutations)");
                System.out.println("Play against random deck...");
                changeDeckIfLosing(createRandomDeck(), randomSeed);
                System.out.println("-----------");
            }
            System.out.println("Playing round " + (round + 1) + "...");
            changeDeckIfLosing(mutateDeck(), randomSeed);
            round++;
        }
    }

    private void changeDeckIfLosing(CardList otherDeck, Random randomSeed) {
        float winrate = getWinrate(deck, otherDeck, randomSeed);
        System.out.println("Winrate: " + winrate);
        if (winrate < 0.5f) {
            deck = otherDeck;
            mutations++;
        }
    }

    private CardList createRandomDeck() {
        ArrayList<Card> cards = new ArrayList<>();
        for (int i = 0; i < GameConstants.MAXIMUM_DECK_SIZE; i++) {
            cards.add(getRandomCard());
        }
        return createDeck(cards);
    }

    private CardList mutateDeck() {
        List<Card> cards = deck.getCards().stream()
                .map(CardListCard::getCard)
                .collect(Collectors.toList());
        Card removedCard = cards.remove((int) (Math.random() * cards.size()));
        Card newCard;
        do {
            newCard = getRandomCard();
        } while (newCard.getId() == removedCard.getId());
        cards.add(newCard);
        System.out.println("Replacing '" + removedCard.getPath() + "' with '" + newCard.getPath() + "'...");
        return createDeck(cards);
    }

    private Card getRandomCard() {
        return allCards.get((int) (Math.random() * allCards.size()));
    }

    private CardList createDeck(List<Card> cards) {
        LocalDateTime now = LocalDateTime.now();
        List<CardListCard> cardListCards = cards.stream()
                .map(card -> new CardListCard(0, card, foilService.getFoil("none"), 1))
                .collect(Collectors.toList());
        return new CardList(0, "myDeck", now, now, cardListCards);
    }

    private float getWinrate(CardList deck1, CardList deck2, Random randomSeed) {
        int gamesPerRound = 2000;
        int wins = 0;
        for (int i = 0; i < gamesPerRound; i++) {
            StartGameInfo startGameInfo = getDefaultStartGameInfo(deck1, deck2);
            BotGame botGame = new BotGame(allCards, startGameInfo, randomSeed.nextLong(), false, true, (botSettings, player) -> {});
            botGame.play();
            if (botGame.getWinner().getId() == 1) {
                wins++;
            }
            int games = (i + 1);
            if ((games % 100) == 0) {
                System.out.println((((float) wins) / games) + " in " + games + " games...");
            }
        }
        return ((float) wins) / gamesPerRound;
    }
}
