package com.destrostudios.cards.backend.application.services;

import com.destrostudios.cards.backend.application.services.internal.PackOpening;
import com.destrostudios.cards.shared.model.*;
import com.destrostudios.cards.shared.model.internal.PackResult;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameConstants;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class PackService {

    private CardService cardService;
    private FoilService foilService;
    private EntityService entityService;

    public PackResult createPackResult(CardList collectionCardList) {
        List<Card> availableCards = cardService.getCards_NonCore();
        Foil foilArtwork = foilService.getFoil(GameConstants.FOIL_NAME_ARTWORK);
        Foil foilNone = foilService.getFoil(GameConstants.FOIL_NAME_NONE);

        PackOpening packOpening = new PackOpening(availableCards, collectionCardList, foilArtwork, foilNone, card -> entityService.hasComponent(card.getId(), Components.LEGENDARY) ? 1 : 2);
        return packOpening.getPackResult();
    }
}
