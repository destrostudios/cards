package com.destrostudios.cards.frontend.application;

import com.destrostudios.cards.frontend.cardgui.Card;
import com.destrostudios.cards.frontend.cardgui.JMonkeyUtil;
import com.destrostudios.cards.frontend.cardgui.transformations.SimpleTargetRotationTransformation;
import com.destrostudios.cards.frontend.cardgui.transformations.relative.samples.HoveringTransformation;
import com.destrostudios.cards.frontend.cardpainter.model.CardModel;

import java.util.HashMap;

public class CardGuiMap {

    private HashMap<Integer, Card<CardModel>> visualCards = new HashMap<>();
    private HashMap<Card<CardModel>, Integer> gameEntities = new HashMap<>();

    public Card<CardModel> getOrCreateCard(int cardEntity) {
        Card<CardModel> card = visualCards.get(cardEntity);
        if (card == null) {
            card = new Card<>(new CardModel());
            initialConfiguration(card);
            visualCards.put(cardEntity, card);
            gameEntities.put(card, cardEntity);
        }
        return card;
    }

    public int getEntity(Card<CardModel> card) {
        return gameEntities.get(card);
    }

    // TODO: Extract to better place. Pass as initializer interface?
    private void initialConfiguration(final Card<CardModel> card) {
        card.position().addRelativeTransformation(new HoveringTransformation(0.3f, 2), () -> card.getModel().isDamaged());
        card.rotation().addRelativeTransformation(new SimpleTargetRotationTransformation(JMonkeyUtil.getQuaternion_Y(-90)), () -> ((card.getModel().getLifepoints() != null) && (card.getModel().getLifepoints() < 0)));
    }
}
