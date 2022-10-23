package com.destrostudios.cards.frontend.application.appstates.services;

import com.destrostudios.cards.frontend.application.appstates.services.cardpainter.model.*;
import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.util.CostUtil;
import com.destrostudios.cards.shared.rules.util.SpellUtil;
import com.destrostudios.cards.shared.rules.util.StatsUtil;

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

    public static void updateModel(EntityData entityData, int card, CardModel cardModel, boolean isFront) {
        cardModel.setFront(isFront);

        // Will be set when applying the possibleActions
        cardModel.setPlayable(false);

        String type = (entityData.hasComponent(card, Components.CREATURE_CARD) ? "creature" : "spell");
        cardModel.setType(type);

        cardModel.setTitle(entityData.getComponent(card, Components.NAME));
        cardModel.setKeywords(createListBasedOnComponents(entityData, card, keywordComponents));
        cardModel.setDescription(entityData.getComponent(card, Components.DESCRIPTION));

        boolean checkedDefaultPlaySpell = false;
        Integer manaCostFullArt = null;
        Integer manaCostDetails = null;
        List<Spell> spells = new LinkedList<>();
        int[] spellEntities = entityData.getComponent(card, Components.SPELLS);
        if (spellEntities != null) {
            for (int spellEntity : spellEntities) {
                Integer manaCost = CostUtil.getEffectiveManaCost(entityData, spellEntity);
                if ((!checkedDefaultPlaySpell) && SpellUtil.isDefaultCastFromHandSpell(entityData, spellEntity)) {
                    manaCostDetails = manaCost;
                    if (entityData.hasComponent(card, Components.HAND)) {
                        manaCostFullArt = manaCostDetails;
                    }
                    checkedDefaultPlaySpell = true;
                } else if (!SpellUtil.isDefaultAttackSpell(entityData, spellEntity)) {
                    String spellDescription = SpellDescriptionGenerator.generateDescription(entityData, spellEntity);
                    Spell spell = new Spell();
                    spell.setCost(createCost(manaCost));
                    spell.setDescription(spellDescription);
                    spells.add(spell);
                }
            }
        }
        cardModel.setManaCostFullArt(manaCostFullArt);
        cardModel.setManaCostDetails(manaCostDetails);
        cardModel.setSpells(spells);

        Integer baseAttack = entityData.getComponent(card, Components.Stats.ATTACK);
        Integer attack = StatsUtil.getEffectiveAttack(entityData, card);
        Integer baseHealth = entityData.getComponent(card, Components.Stats.HEALTH);
        Integer health = StatsUtil.getEffectiveHealth(entityData, card);
        cardModel.setAttackDamage(attack);
        cardModel.setLifepoints(health);
        cardModel.setAttackBuffed((attack != null) && (attack > baseAttack));
        cardModel.setHealthBuffed((health != null) && (health > baseHealth));
        cardModel.setDamaged(entityData.hasComponent(card, Components.Stats.DAMAGED) || entityData.hasComponent(card, Components.Stats.BONUS_DAMAGED));

        cardModel.setDivineShield(entityData.getOptionalComponent(card, Components.Ability.DIVINE_SHIELD).orElse(false));
        cardModel.setTaunt(entityData.hasComponent(card, Components.Ability.TAUNT));
        cardModel.setFlavourText(entityData.getComponent(card, Components.FLAVOUR_TEXT));
        cardModel.setTribes(createListBasedOnComponents(entityData, card, tribeComponents));
        cardModel.setFoil(entityData.getComponent(card, Components.FOIL));
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

    private static Cost createCost(Integer manaCost) {
        if (manaCost != null) {
            Cost cost = new Cost();
            cost.setManaCost(manaCost);
            return cost;
        }
        return null;
    }
}
