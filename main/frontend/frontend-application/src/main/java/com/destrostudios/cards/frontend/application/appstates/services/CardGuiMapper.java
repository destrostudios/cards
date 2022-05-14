package com.destrostudios.cards.frontend.application.appstates.services;

import com.destrostudios.cardgui.Card;
import com.destrostudios.cards.frontend.application.appstates.services.cardpainter.model.*;
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

        keywordComponents.put(Components.Ability.SLOW, "Slow");
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

        // Will be set when applying the possibleActions
        cardModel.setPlayable(false);

        List<Color> colors = createListBasedOnComponents(entityData, cardEntity, colorComponents);
        cardModel.setColors(colors);

        String title = entityData.getComponent(cardEntity, Components.DISPLAY_NAME);
        cardModel.setTitle(title);

        List<String> tribes = createListBasedOnComponents(entityData, cardEntity, tribeComponents);
        cardModel.setTribes(tribes);

        List<String> keywords = createListBasedOnComponents(entityData, cardEntity, keywordComponents);
        cardModel.setKeywords(keywords);

        String castDescription = "CastDescription";
        cardModel.setCastDescription(castDescription);

        boolean checkedDefaultPlaySpell = false;
        Integer manaCost = null;
        String description = null;
        List<Spell> spells = new LinkedList<>();
        int[] spellEntities = entityData.getComponent(cardEntity, Components.SPELL_ENTITIES);
        if (spellEntities != null) {
            for (int spellEntity : spellEntities) {
                Integer spellCostEntity = entityData.getComponent(spellEntity, Components.Spell.COST_ENTITY);
                String spellDescription = SpellDescriptionGenerator.generateDescription(entityData, spellEntity);
                if ((!checkedDefaultPlaySpell) && entityData.hasComponent(spellEntity, Components.Spell.CastCondition.FROM_HAND)) {
                    if (entityData.hasComponent(cardEntity, Components.HAND_CARDS)) {
                        manaCost = entityData.getComponent(spellCostEntity, Components.MANA);
                    }
                    description = spellDescription;
                    checkedDefaultPlaySpell = true;
                }
                else {
                    Spell spell = new Spell();
                    Cost cost = createCost(entityData, spellCostEntity);
                    spell.setCost(cost);
                    spell.setDescription(spellDescription);
                    spells.add(spell);
                }
            }
        }
        cardModel.setManaCost(manaCost);
        cardModel.setDescription(description);
        cardModel.setSpells(spells);

        Integer attackDamage = entityData.getComponent(cardEntity, Components.ATTACK);
        cardModel.setAttackDamage(attackDamage);

        Integer lifepoints = entityData.getComponent(cardEntity, Components.HEALTH);
        cardModel.setLifepoints(lifepoints);

        boolean isDamaged = entityData.hasComponent(cardEntity, Components.DAMAGED);
        cardModel.setDamaged(isDamaged);

        String flavourText = entityData.getComponent(cardEntity, Components.FLAVOUR_TEXT);
        cardModel.setFlavourText(flavourText);

        cardModel.setFoil(entityData.getComponent(cardEntity, Components.FOIL));
    }

    private static <T> List<T> createListBasedOnComponents(EntityData entityData, int entity, Map<ComponentDefinition, T> componentValueMap) {
        List<T> list = new LinkedList<>();
        for (Map.Entry<ComponentDefinition, T> componentValueEntry : componentValueMap.entrySet()) {
            if (entityData.hasComponent(entity, componentValueEntry.getKey())) {
                list.add(componentValueEntry.getValue());
            }
        }
        return list;
    }

    private static Cost createCost(EntityData entityData, Integer costEntity) {
        if (costEntity != null) {
            Cost cost = new Cost();
            cost.setManaCost(entityData.getComponent(costEntity, Components.MANA));
            return cost;
        }
        return null;
    }
}
