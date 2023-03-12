package com.destrostudios.cards.backend.application.services;

import com.destrostudios.cards.shared.model.*;
import com.destrostudios.cards.shared.model.internal.PackResult;
import com.destrostudios.cards.shared.rules.GameConstants;
import lombok.AllArgsConstructor;

import java.util.LinkedList;
import java.util.List;

@AllArgsConstructor
public class PackService {

    private ModeService modeService;
    private CardService cardService;
    private FoilService foilService;
    private CardListService cardListService;
    private UserService userService;

    public PackResult openPack(int userId) {
        User user = userService.getUser(userId);
        if (user.getPacks() <= 0) {
            throw new RuntimeException("User has no packs.");
        }
        userService.addPacks(userId, -1);
        PackResult packResult = createPackResult();
        Mode modeClassic = modeService.getMode(GameConstants.MODE_NAME_CLASSIC);
        UserCardList library = userService.getLibrary(userId, modeClassic.getId());
        for (CardListCard cardListCard : packResult.getCards()) {
            cardListService.addCard(library.getCardList().getId(), cardListCard.getCard().getId(), cardListCard.getFoil().getId(), cardListCard.getAmount());
        }
        return packResult;
    }

    private PackResult createPackResult() {
        List<Card> availableCards = cardService.getCards_Pack();
        Foil foilArtwork = foilService.getFoil(GameConstants.FOIL_NAME_ARTWORK);
        Foil foilNone = foilService.getFoil(GameConstants.FOIL_NAME_NONE);
        LinkedList<CardListCard> cards = new LinkedList<>();
        for (int i = 0; i < GameConstants.CARDS_PER_PACK; i++) {
            int cardIndex = (int) (Math.random() * availableCards.size());
            Card card = availableCards.get(cardIndex);
            Foil foil = ((i == 0) ? foilArtwork : foilNone);
            cards.add(new CardListCard(0, card, foil, 1));
        }
        return new PackResult(cards);
    }
}
