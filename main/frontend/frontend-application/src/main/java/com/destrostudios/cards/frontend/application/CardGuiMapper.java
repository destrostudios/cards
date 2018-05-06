package com.destrostudios.cards.frontend.application;

import com.destrostudios.cards.frontend.cardgui.BoardObjectModel;
import com.destrostudios.cards.frontend.cardgui.Card;
import com.destrostudios.cards.frontend.cardpainter.model.*;
import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.rules.Components;

import java.util.*;

public class CardGuiMapper {

    public static void updateModel(Card<CardModel> card, EntityData entityData, int cardEntity) {
        CardModel cardModel = card.getModel();

        boolean isFront = true;
        cardModel.setFront(isFront);

        Integer costEntity = entityData.getComponent(cardEntity, Components.COST_ENTITY);
        ManaCost manaCost = createManaCost(cardModel, entityData, costEntity);
        cardModel.setManaCost(manaCost);

        String castDescription = "Battlecry";
        cardModel.setCastDescription(castDescription);

        String description = "Description";
        cardModel.setDescription(description);

        List<Spell> spells = new LinkedList<>();
        int[] spellEntities = entityData.getComponent(cardEntity, Components.SPELL_ENTITIES);
        if (spellEntities != null) {
            for (int spellEntity : spellEntities) {
                Spell spell = new Spell(cardModel);
                Integer spellCostEntity = entityData.getComponent(spellEntity, Components.COST_ENTITY);
                Cost cost = createCost(spell, entityData, spellCostEntity);
                spell.setCost(cost);
                spell.setDescription("Spell Description");
                spells.add(spell);
            }
        }
        cardModel.setSpells(spells);
    }

    private static Cost createCost(BoardObjectModel parentModel, EntityData entityData, Integer costEntity) {
        if (costEntity != null) {
            Cost cost = new Cost(parentModel);
            cost.setTap(entityData.hasComponent(costEntity, Components.Cost.TAP));
            cost.setManaCost(createManaCost(cost, entityData, costEntity));
            return cost;
        }
        return null;
    }

    private static ManaCost createManaCost(BoardObjectModel parentModel, EntityData entityData, Integer costEntity) {
        if (costEntity != null) {
            ManaCost manaCost = new ManaCost(parentModel);
            Integer neutralManaAmount = entityData.getComponent(costEntity, Components.ManaAmount.NEUTRAL);
            if (neutralManaAmount != null) {
                manaCost.set(Color.NEUTRAL, neutralManaAmount);
            }
            Integer whiteManaAmount = entityData.getComponent(costEntity, Components.ManaAmount.WHITE);
            if (whiteManaAmount != null) {
                manaCost.set(Color.WHITE, whiteManaAmount);
            }
            Integer redManaAmount = entityData.getComponent(costEntity, Components.ManaAmount.RED);
            if (redManaAmount != null) {
                manaCost.set(Color.RED, redManaAmount);
            }
            Integer greenManaAmount = entityData.getComponent(costEntity, Components.ManaAmount.GREEN);
            if (greenManaAmount != null) {
                manaCost.set(Color.GREEN, greenManaAmount);
            }
            Integer blueManaAmount = entityData.getComponent(costEntity, Components.ManaAmount.BLUE);
            if (blueManaAmount != null) {
                manaCost.set(Color.BLUE, blueManaAmount);
            }
            Integer blackManaAmount = entityData.getComponent(costEntity, Components.ManaAmount.BLACK);
            if (blackManaAmount != null) {
                manaCost.set(Color.BLACK, blackManaAmount);
            }
            return manaCost;
        }
        return null;
    }
}
