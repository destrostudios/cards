package com.destrostudios.cards.frontend.application.appstates.services;

import com.destrostudios.cards.frontend.application.appstates.services.cardpainter.model.*;
import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.model.CardListCard;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.util.CostUtil;
import com.destrostudios.cards.shared.rules.util.ModelUtil;
import com.destrostudios.cards.shared.rules.util.SpellUtil;
import com.destrostudios.cards.shared.rules.util.StatsUtil;

import java.util.*;

public class CardGuiMapper {

    private static Map<ComponentDefinition, String> keywordComponents = new LinkedHashMap<>();
    private static Map<ComponentDefinition, String> tribeComponents = new LinkedHashMap<>();
    static {
        keywordComponents.put(Components.Ability.DIVINE_SHIELD, "Divine Shield");
        keywordComponents.put(Components.Ability.TAUNT, "Taunt");

        tribeComponents.put(Components.Tribe.BEAST, "Beast");
        tribeComponents.put(Components.Tribe.DRAGON, "Dragon");
        tribeComponents.put(Components.Tribe.GOBLIN, "Goblin");
    }

    public static CardModel createModel(EntityData data, CardListCard cardListCard) {
        int card = ModelUtil.createCard(data, cardListCard);
        CardModel cardModel = new CardModel();
        updateModel(data, card, cardModel, true);
        return cardModel;
    }

    public static void updateModel(EntityData data, int card, CardModel cardModel, boolean isFront) {
        cardModel.setFront(isFront);

        // Will be set when applying the possibleActions
        cardModel.setPlayable(false);

        String type = (data.hasComponent(card, Components.CREATURE_CARD) ? "creature" : "spell");
        cardModel.setType(type);

        cardModel.setTitle(data.getComponent(card, Components.NAME));
        cardModel.setKeywords(createListBasedOnComponents(data, card, keywordComponents));
        cardModel.setDescription(data.getComponent(card, Components.DESCRIPTION));

        boolean checkedDefaultCastFromHandSpell = false;
        Integer baseManaCost = null;
        Integer manaCostFullArt = null;
        Integer manaCostDetails = null;
        List<Spell> spells = new LinkedList<>();
        int[] spellEntities = data.getComponent(card, Components.SPELLS);
        if (spellEntities != null) {
            for (int spellEntity : spellEntities) {
                Integer manaCost = CostUtil.getEffectiveManaCost(data, spellEntity);
                if ((!checkedDefaultCastFromHandSpell) && SpellUtil.isDefaultCastFromHandSpell(data, spellEntity)) {
                    baseManaCost = data.getComponent(spellEntity, Components.Cost.MANA_COST);
                    manaCostDetails = manaCost;
                    if (data.hasComponent(card, Components.HAND)) {
                        manaCostFullArt = manaCostDetails;
                    }
                    checkedDefaultCastFromHandSpell = true;
                } else if (!SpellUtil.isDefaultAttackSpell(data, spellEntity)) {
                    String spellDescription = SpellDescriptionGenerator.generateDescription(data, spellEntity);
                    Spell spell = new Spell();
                    spell.setCost(createCost(manaCost));
                    spell.setDescription(spellDescription);
                    spells.add(spell);
                }
            }
        }
        cardModel.setManaCostFullArt(manaCostFullArt);
        cardModel.setManaCostDetails(manaCostDetails);
        cardModel.setManaCostModification(getStatModification(baseManaCost, manaCostDetails));
        cardModel.setSpells(spells);

        Integer baseAttack = data.getComponent(card, Components.Stats.ATTACK);
        Integer attack = StatsUtil.getEffectiveAttack(data, card);
        Integer baseHealth = data.getComponent(card, Components.Stats.HEALTH);
        Integer health = StatsUtil.getEffectiveHealth(data, card);
        cardModel.setAttack(attack);
        cardModel.setAttackModification(getStatModification(baseAttack, attack));
        cardModel.setHealth(health);
        cardModel.setHealthModification(getStatModification(baseHealth, health));
        cardModel.setDamaged(StatsUtil.isDamaged(data, card));

        cardModel.setDivineShield(data.getOptionalComponent(card, Components.Ability.DIVINE_SHIELD).orElse(false));
        cardModel.setTaunt(data.hasComponent(card, Components.Ability.TAUNT));
        cardModel.setFlavourText(data.getComponent(card, Components.FLAVOUR_TEXT));
        cardModel.setLegendary(data.hasComponent(card, Components.LEGENDARY));
        cardModel.setTribes(createListBasedOnComponents(data, card, tribeComponents));
        cardModel.setFoil(data.getComponent(card, Components.FOIL));
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

    private static StatModification getStatModification(Integer baseValue, Integer effectiveValue) {
        if (effectiveValue != null) {
            if (effectiveValue < baseValue) {
                return StatModification.DECREASED;
            } else if (effectiveValue > baseValue) {
                return StatModification.INCREASED;
            }
        }
        return null;
    }
}
