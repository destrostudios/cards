package com.destrostudios.cards.frontend.application.appstates.services;

import com.destrostudios.cardgui.Card;
import com.destrostudios.cards.frontend.application.appstates.services.cardpainter.model.*;
import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.util.HealthUtil;
import com.destrostudios.cards.shared.rules.util.SpellUtil;

import java.util.*;

public class CardGuiMapper {

    private static Map<ComponentDefinition, String> keywordComponents = new LinkedHashMap<>();
    private static Map<ComponentDefinition, String> tribeComponents = new LinkedHashMap<>();
    static {
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

        boolean isFront = !entityData.hasComponent(cardEntity, Components.LIBRARY);
        cardModel.setFront(isFront);

        // Will be set when applying the possibleActions
        cardModel.setPlayable(false);

        String type = (entityData.hasComponent(cardEntity, Components.CREATURE_CARD) ? "creature" : "spell");
        cardModel.setType(type);

        String title = entityData.getComponent(cardEntity, Components.NAME);
        cardModel.setTitle(title);

        List<String> tribes = createListBasedOnComponents(entityData, cardEntity, tribeComponents);
        cardModel.setTribes(tribes);

        List<String> keywords = createListBasedOnComponents(entityData, cardEntity, keywordComponents);
        cardModel.setKeywords(keywords);

        String castDescription = null;
        cardModel.setCastDescription(castDescription);

        boolean checkedDefaultPlaySpell = false;
        Integer manaCostFullArt = null;
        Integer manaCostDetails = null;
        String description = null;
        List<Spell> spells = new LinkedList<>();
        int[] spellEntities = entityData.getComponent(cardEntity, Components.SPELL_ENTITIES);
        if (spellEntities != null) {
            for (int spellEntity : spellEntities) {
                Integer spellCost = entityData.getComponent(spellEntity, Components.COST);
                String spellDescription = SpellDescriptionGenerator.generateDescription(entityData, spellEntity);
                if ((!checkedDefaultPlaySpell) && SpellUtil.isDefaultCastFromHandSpell(entityData, spellEntity)) {
                    manaCostDetails = entityData.getComponent(spellCost, Components.MANA);
                    if (entityData.hasComponent(cardEntity, Components.HAND)) {
                        manaCostFullArt = manaCostDetails;
                    }
                    description = spellDescription;
                    checkedDefaultPlaySpell = true;
                } else if (!SpellUtil.isDefaultAttackSpell(entityData, spellEntity)) {
                    Spell spell = new Spell();
                    Cost cost = createCost(entityData, spellCost);
                    spell.setCost(cost);
                    spell.setDescription(spellDescription);
                    spells.add(spell);
                }
            }
        }
        cardModel.setManaCostFullArt(manaCostFullArt);
        cardModel.setManaCostDetails(manaCostDetails);
        cardModel.setDescription(description);
        cardModel.setSpells(spells);

        Integer attackDamage = entityData.getComponent(cardEntity, Components.ATTACK);
        cardModel.setAttackDamage(attackDamage);

        Integer lifepoints = HealthUtil.getEffectiveHealth(entityData, cardEntity);
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
