package com.destrostudios.cards.backend.application.services.internal;

import com.destrostudios.cards.shared.model.Card;
import com.destrostudios.cards.shared.model.CardList;
import com.destrostudios.cards.shared.model.Foil;
import com.destrostudios.cards.shared.model.internal.BaseCardIdentifier;
import com.destrostudios.cards.shared.model.internal.PackResult;
import com.destrostudios.cards.shared.rules.GameConstants;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

public class PackOpening {

    public PackOpening(List<Card> availableCards, CardList collectionCardList, Foil foilArtwork, Foil foilNone, Function<Card, Integer> getMaximumCollectionAmount) {
        this.availableCards = availableCards;
        this.foilArtwork = foilArtwork;
        this.foilNone = foilNone;
        init(collectionCardList, getMaximumCollectionAmount);
        createPackResult();
    }
    private List<Card> availableCards;
    private Foil foilArtwork;
    private Foil foilNone;
    private HashMap<Foil, ArrayList<BaseCardIdentifier>> possibleCardsByFoil = new HashMap<>();
    @Getter
    private PackResult packResult;

    private void init(CardList collectionCardList, Function<Card, Integer> getMaximumCollectionAmount) {
        Foil[] foils = new Foil[] { foilNone, foilArtwork };
        for (Foil foil : foils) {
            ArrayList<BaseCardIdentifier> possibleCards = new ArrayList<>();
            for (Card card : availableCards) {
                int maximumAmount = getMaximumCollectionAmount.apply(card);
                // TODO: This can be optimized by caching the current amounts instead of looping every time
                int currentAmount = collectionCardList.getCardAmount(card.getId(), foil.getId());
                int remainingAmount = (maximumAmount - currentAmount);
                for (int i = 0; i < remainingAmount; i++){
                    possibleCards.add(new BaseCardIdentifier(card, foil));
                }
            }
            possibleCardsByFoil.put(foil, possibleCards);
        }
    }

    private void createPackResult() {
        ArrayList<BaseCardIdentifier> packCards = new ArrayList<>();
        packCards.add(openCard(foilArtwork));
        for (int i = 0; i < (GameConstants.CARDS_PER_PACK - 1); i++) {
            packCards.add(openCard(foilNone));
        }
        packResult = new PackResult(packCards);
    }

    private BaseCardIdentifier openCard(Foil preferredFoil) {
        ArrayList<BaseCardIdentifier> possiblePreferredFoilCards = possibleCardsByFoil.get(preferredFoil);
        if (possiblePreferredFoilCards.size() > 0) {
            return removeRandomCard(possiblePreferredFoilCards);
        } else {
            Foil otherFoil = ((preferredFoil == foilArtwork) ? foilNone : foilArtwork);
            ArrayList<BaseCardIdentifier> possibleOtherFoilCards = possibleCardsByFoil.get(otherFoil);
            if (possibleOtherFoilCards.size() > 0) {
                return removeRandomCard(possibleOtherFoilCards);
            } else {
                Foil[] allFoils = possibleCardsByFoil.keySet().toArray(Foil[]::new);
                Foil randomFoil = allFoils[(int) (Math.random() * allFoils.length)];
                Card randomCard = availableCards.get((int) (Math.random() * availableCards.size()));
                return new BaseCardIdentifier(randomCard, randomFoil);
            }
        }
    }

    private BaseCardIdentifier removeRandomCard(ArrayList<BaseCardIdentifier> possibleCards) {
        return possibleCards.remove((int) (Math.random() * possibleCards.size()));
    }
}
