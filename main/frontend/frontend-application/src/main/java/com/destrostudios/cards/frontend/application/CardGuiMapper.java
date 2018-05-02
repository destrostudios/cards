package com.destrostudios.cards.frontend.application;

import com.destrostudios.cards.frontend.cardgui.BoardObjectModel;
import com.destrostudios.cards.frontend.cardgui.Card;
import com.destrostudios.cards.frontend.cardpainter.model.*;
import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.rules.Components;

import java.util.*;

public class CardGuiMapper {

    private static Map<ComponentDefinition, Color> colorComponents = new LinkedHashMap<>();
    private static Map<ComponentDefinition, String> keywordComponents = new LinkedHashMap<>();
    private static Map<ComponentDefinition, String> tribeComponents = new LinkedHashMap<>();
    static {
        colorComponents.put(Components.Color.NEUTRAL, Color.NEUTRAL);
        colorComponents.put(Components.Color.WHITE, Color.WHITE);
        colorComponents.put(Components.Color.RED, Color.RED);
        colorComponents.put(Components.Color.GREEN, Color.GREEN);
        colorComponents.put(Components.Color.BLUE, Color.BLUE);
        colorComponents.put(Components.Color.BLACK, Color.BLACK);

        keywordComponents.put(Components.Ability.CHARGE, "Charge");
        keywordComponents.put(Components.Ability.DIVINE_SHIELD, "Divine Shield");
        keywordComponents.put(Components.Ability.HEXPROOF, "Hexproof");
        keywordComponents.put(Components.Ability.IMMUNE, "Immune");
        keywordComponents.put(Components.Ability.TAUNT, "Taunt");

        tribeComponents.put(Components.Tribe.BEAST, "Beast");
        tribeComponents.put(Components.Tribe.DRAGON, "Dragon");
        tribeComponents.put(Components.Tribe.FISH, "Fish");
        tribeComponents.put(Components.Tribe.GOD, "God");
        tribeComponents.put(Components.Tribe.HUMAN, "Human");
    }

    public static void updateModel(Card<CardModel> card, EntityData entityData, int cardEntity) {
        CardModel cardModel = card.getModel();

        boolean isFront = true;
        cardModel.setFront(isFront);

        List<Color> colors = createListBasedOnComponents(entityData, cardEntity, colorComponents);
        cardModel.setColors(colors);

        String title = entityData.get(cardEntity, Components.DISPLAY_NAME);
        cardModel.setTitle(title);

        Integer costEntity = entityData.get(cardEntity, Components.COST_ENTITY);
        ManaCost manaCost = createManaCost(cardModel, entityData, costEntity);
        cardModel.setManaCost(manaCost);

        List<String> tribes = createListBasedOnComponents(entityData, cardEntity, tribeComponents);
        cardModel.setTribes(tribes);

        List<String> keywords = createListBasedOnComponents(entityData, cardEntity, keywordComponents);
        cardModel.setKeywords(keywords);

        String castDescription = "Battlecry";
        cardModel.setCastDescription(castDescription);

        String description = "Description";
        cardModel.setDescription(description);

        List<Spell> spells = new LinkedList<>();
        Integer[] spellEntities = entityData.get(cardEntity, Components.SPELL_ENTITIES);
        if (spellEntities != null) {
            for (int spellEntity : spellEntities) {
                Spell spell = new Spell(cardModel);
                Integer spellCostEntity = entityData.get(spellEntity, Components.COST_ENTITY);
                Cost cost = createCost(spell, entityData, spellCostEntity);
                spell.setCost(cost);
                spell.setDescription("Spell Description");
                spells.add(spell);
            }
        }
        cardModel.setSpells(spells);

        Integer attackDamage = entityData.get(cardEntity, Components.ATTACK);
        cardModel.setAttackDamage(attackDamage);

        Integer lifepoints = entityData.get(cardEntity, Components.HEALTH);
        cardModel.setLifepoints(lifepoints);

        boolean isDamaged = entityData.has(cardEntity, Components.DAMAGED);
        cardModel.setDamaged(isDamaged);

        String flavourText = entityData.get(cardEntity, Components.FLAVOUR_TEXT);
        cardModel.setFlavourText(flavourText);
    }

    private static <T> List<T> createListBasedOnComponents(EntityData entityData, int entity, Map<ComponentDefinition, T> componentValueMap) {
        List<T> list = new LinkedList<>();
        for (Map.Entry<ComponentDefinition, T> componentValueEntry : componentValueMap.entrySet()) {
            if (entityData.has(entity, componentValueEntry.getKey())) {
                list.add(componentValueEntry.getValue());
            }
        }
        return list;
    }

    private static Cost createCost(BoardObjectModel parentModel, EntityData entityData, Integer costEntity) {
        if (costEntity != null) {
            Cost cost = new Cost(parentModel);
            cost.setTap(entityData.has(costEntity, Components.Cost.TAP));
            cost.setManaCost(createManaCost(cost, entityData, costEntity));
            return cost;
        }
        return null;
    }

    private static ManaCost createManaCost(BoardObjectModel parentModel, EntityData entityData, Integer costEntity) {
        if (costEntity != null) {
            ManaCost manaCost = new ManaCost(parentModel);
            Integer neutralManaAmount = entityData.get(costEntity, Components.ManaAmount.NEUTRAL);
            if (neutralManaAmount != null) {
                manaCost.set(Color.NEUTRAL, neutralManaAmount);
            }
            Integer whiteManaAmount = entityData.get(costEntity, Components.ManaAmount.WHITE);
            if (whiteManaAmount != null) {
                manaCost.set(Color.WHITE, whiteManaAmount);
            }
            Integer redManaAmount = entityData.get(costEntity, Components.ManaAmount.RED);
            if (redManaAmount != null) {
                manaCost.set(Color.RED, redManaAmount);
            }
            Integer greenManaAmount = entityData.get(costEntity, Components.ManaAmount.GREEN);
            if (greenManaAmount != null) {
                manaCost.set(Color.GREEN, greenManaAmount);
            }
            Integer blueManaAmount = entityData.get(costEntity, Components.ManaAmount.BLUE);
            if (blueManaAmount != null) {
                manaCost.set(Color.BLUE, blueManaAmount);
            }
            Integer blackManaAmount = entityData.get(costEntity, Components.ManaAmount.BLACK);
            if (blackManaAmount != null) {
                manaCost.set(Color.BLACK, blackManaAmount);
            }
            return manaCost;
        }
        return null;
    }
}
