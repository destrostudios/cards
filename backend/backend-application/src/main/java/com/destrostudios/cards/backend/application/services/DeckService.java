package com.destrostudios.cards.backend.application.services;

import com.destrostudios.cards.shared.model.*;
import com.destrostudios.cards.shared.model.internal.NewCardListCard;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class DeckService {

    private ModeService modeService;
    private UserService userService;

    public CardList getCardList(int modeId, int deckId) {
        Object deck = getDeck(modeId, deckId);
        if (deck instanceof ModeDeck modeDeck) {
            return modeDeck.getDeckCardList();
        } else {
            return ((UserModeDeck) deck).getDeckCardList();
        }
    }

    public void createDeck(int userId, int modeId) {
        Mode mode = modeService.getMode(modeId);
        if (mode.isHasUserLibrary()) {
            int userModeId = userService.getUserModeId(userId, modeId);
            userService.createUserModeDeck(userModeId);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public void updateDeck(int modeId, int deckId, String name,List<NewCardListCard> cards) {
        Mode mode = modeService.getMode(modeId);
        if (mode.isHasUserLibrary()) {
            userService.updateUserModeDeck(deckId, name, cards);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public void deleteDeck(int modeId, int deckId) {
        Mode mode = modeService.getMode(modeId);
        if (mode.isHasUserLibrary()) {
            userService.deleteUserModeDeck(deckId);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    private Object getDeck(int modeId, int deckId) {
        Mode mode = modeService.getMode(modeId);
        if (mode.isHasUserLibrary()) {
            return userService.getUserModeDeck(deckId);
        } else {
            return mode.getDecks().stream().filter(modeDeck -> modeDeck.getId() == deckId).findAny().orElseThrow();
        }
    }
}
