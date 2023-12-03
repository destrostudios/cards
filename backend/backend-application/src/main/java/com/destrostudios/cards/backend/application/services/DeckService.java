package com.destrostudios.cards.backend.application.services;

import com.destrostudios.cards.shared.model.*;
import com.destrostudios.cards.shared.model.internal.NewCardListCard;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class DeckService {

    private ModeService modeService;
    private UserService userService;

    public Deck getDeck(int modeId, int deckId) {
        Mode mode = modeService.getMode(modeId);
        if (mode.getDecks().size() > 0) {
            return mode.getDecks().stream().filter(modeDeck -> modeDeck.getId() == deckId).findAny().orElseThrow();
        } else {
            return userService.getUserModeDeck(deckId);
        }
    }

    public void createDeck(int userId, int modeId) {
        Mode mode = modeService.getMode(modeId);
        if (mode.isHasFreeUserDecks()) {
            userService.createUserModeDeck(userId, modeId);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public void updateDeck(int modeId, int deckId, String name,List<NewCardListCard> cards) {
        Mode mode = modeService.getMode(modeId);
        if (mode.isHasFreeUserDecks()) {
            userService.updateUserModeDeck(deckId, name, cards);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public void deleteDeck(int modeId, int deckId) {
        Mode mode = modeService.getMode(modeId);
        if (mode.isHasFreeUserDecks()) {
            userService.deleteUserModeDeck(deckId);
        } else {
            throw new UnsupportedOperationException();
        }
    }
}
