package com.destrostudios.cards.shared.rules;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.entities.IntList;
import com.destrostudios.cards.shared.entities.templates.EntityTemplate;
import com.destrostudios.cards.shared.model.Card;
import com.destrostudios.cards.shared.model.CardList;
import com.destrostudios.cards.shared.model.CardListCard;
import com.destrostudios.cards.shared.rules.cards.Foil;
import com.destrostudios.cards.shared.rules.util.ModelUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GameSetup {

    public static void apply(EntityData data, StartGameInfo startGameInfo, List<Card> allCards, Random random) {
        int player1 = data.createEntity();
        int player2 = data.createEntity();
        initPlayer(data, player1, player2, startGameInfo.getPlayers()[0], allCards, random);
        initPlayer(data, player2, player1, startGameInfo.getPlayers()[1], allCards, random);
    }

    private static void initPlayer(EntityData data, int player, int opponent, PlayerInfo playerInfo, List<Card> allCards, Random random) {
        IntList libraryCards = new IntList(GameConstants.MAXIMUM_DECK_SIZE);
        if (playerInfo.getDeck() != null) {
            fillLibrary(data, libraryCards, playerInfo.getDeck());
        } else {
            fillTestLibrary(data, libraryCards, allCards, random);
        }
        initPlayer(data, player, opponent, playerInfo.getLogin(), libraryCards);
    }

    private static void fillLibrary(EntityData data, IntList libraryCards, CardList deck) {
        for (CardListCard cardListCard : deck.getCards()) {
            for (int i = 0; i < cardListCard.getAmount(); i++) {
                int card = ModelUtil.createCard(data, cardListCard);
                libraryCards.add(card);
            }
        }
    }

    private static void fillTestLibrary(EntityData data, IntList libraryCards, List<Card> allCards, Random random) {
        ArrayList<Card> randomSortedCards = new ArrayList<>(allCards);
        Collections.shuffle(randomSortedCards, random);
        randomSortedCards.stream().limit(GameConstants.MAXIMUM_DECK_SIZE).forEach(card -> {
            int cardEntity = data.createEntity();
            EntityTemplate.loadTemplate(data, cardEntity, card.getPath());
            setRandomFoil(data, cardEntity);
            libraryCards.add(cardEntity);
        });
    }

    private static void setRandomFoil(EntityData data, int card) {
        switch ((int) (Math.random() * 7)) {
            case 0 -> data.setComponent(card, Components.FOIL, Foil.ARTWORK);
            case 1 -> data.setComponent(card, Components.FOIL, Foil.FULL);
        }
    }

    public static void initPlayer(EntityData data, int player, int opponent, String name, IntList libraryCards) {
        data.setComponent(player, Components.NAME, name);
        data.setComponent(player, Components.NEXT_PLAYER, opponent);
        data.setComponent(player, Components.Stats.HEALTH, GameConstants.PLAYER_HEALTH);
        data.setComponent(player, Components.BOARD);
        data.setComponent(player, Components.Player.LIBRARY_CARDS, libraryCards);
        data.setComponent(player, Components.Player.HAND_CARDS, new IntList());
        data.setComponent(player, Components.Player.CREATURE_ZONE_CARDS, new IntList());
        data.setComponent(player, Components.Player.GRAVEYARD_CARDS, new IntList());
        data.setComponent(player, Components.Player.MULLIGAN);
        for (int card : libraryCards) {
            data.setComponent(card, Components.OWNED_BY, player);
            data.setComponent(card, Components.Zone.LIBRARY);
            data.setComponent(card, Components.Zone.PLAYER_LIBRARY[player]);
        }
    }
}
