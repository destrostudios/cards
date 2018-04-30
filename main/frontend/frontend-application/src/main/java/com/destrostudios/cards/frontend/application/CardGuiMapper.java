package com.destrostudios.cards.frontend.application;

import com.destrostudios.cards.frontend.cardgui.Card;
import com.destrostudios.cards.frontend.cardpainter.model.CardModel;
import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.rules.Components;

import java.util.LinkedList;
import java.util.List;

public class CardGuiMapper {

    public static void updateModel(Card<CardModel> card, EntityData entityData, int cardEntity) {
        CardModel cardModel = card.getModel();
        cardModel.setFront(true);

        String displayName = "" + entityData.get(cardEntity, Components.DISPLAY_NAME);
        cardModel.setTitle(displayName);

        List<Integer> manaTypes = new LinkedList<>();
        manaTypes.add(2);
        manaTypes.add(3);
        cardModel.setManaTypes(manaTypes);
        List<String> keywords = new LinkedList<>();
        keywords.add("Charge");
        keywords.add("Immune");
        cardModel.setKeywords(keywords);
        cardModel.setFlavourText("I am stupid.");


        Integer attackDamage = entityData.get(cardEntity, Components.ATTACK);
        cardModel.setAttackDamage(attackDamage);

        Integer lifepoints = entityData.get(cardEntity, Components.HEALTH);
        cardModel.setLifepoints(lifepoints);

        cardModel.setDamaged(true);
        List<String> tribes = new LinkedList<>();
        tribes.add("Human");
        tribes.add("Dragon");
        cardModel.setTribes(tribes);
    }
}
