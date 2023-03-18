package com.destrostudios.cards.backend.application;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.entities.templates.EntityTemplate;
import com.destrostudios.cards.shared.model.Card;
import com.destrostudios.cards.shared.model.CardListCard;
import com.destrostudios.cards.shared.model.UserModeDeck;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameConstants;
import com.destrostudios.cards.shared.rules.PlayerInfo;
import com.destrostudios.cards.shared.rules.StartGameInfo;
import com.destrostudios.cards.shared.rules.cards.Foil;
import com.destrostudios.cards.shared.rules.util.ModelUtil;
import lombok.AllArgsConstructor;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class GameSetup {

    private List<Card> cards;
    private EntityData data;
    private StartGameInfo startGameInfo;

    public void apply() {
        int player1 = data.createEntity();
        int player2 = data.createEntity();
        initPlayer(player1, player2, startGameInfo.getPlayers()[0]);
        initPlayer(player2, player1, startGameInfo.getPlayers()[1]);
    }

    private void initPlayer(int player, int opponent, PlayerInfo playerInfo) {
        data.setComponent(player, Components.NAME, playerInfo.getLogin());
        data.setComponent(player, Components.NEXT_PLAYER, opponent);
        data.setComponent(player, Components.Stats.HEALTH, GameConstants.PLAYER_HEALTH);
        data.setComponent(player, Components.BOARD);
        List<Integer> library;
        if (playerInfo.getDeck() != null) {
            library = createLibrary(playerInfo.getDeck());
        } else {
            library = createTestLibrary();
        }
        int i = 0;
        for (int card : library) {
            data.setComponent(card, Components.OWNED_BY, player);
            data.setComponent(card, Components.LIBRARY, i);
            i++;
        }
    }

    private List<Integer> createLibrary(UserModeDeck deck) {
        LinkedList<Integer> library = new LinkedList<>();
        for (CardListCard cardListCard : deck.getDeckCardList().getCards()) {
            for (int i = 0; i < cardListCard.getAmount(); i++) {
                int card = ModelUtil.createCard(data, cardListCard);
                library.add(card);
            }
        }
        return library;
    }

    private List<Integer> createTestLibrary() {
        return cards.stream().map(card -> {
            int cardEntity = EntityTemplate.createFromTemplate(data, card.getPath());
            setRandomFoil(cardEntity);
            return cardEntity;
        }).collect(Collectors.toList());
    }

    private void setRandomFoil(int card) {
        switch ((int) (Math.random() * 7)) {
            case 0: data.setComponent(card, Components.FOIL, Foil.ARTWORK); break;
            case 1: data.setComponent(card, Components.FOIL, Foil.FULL); break;
        }
    }
}
