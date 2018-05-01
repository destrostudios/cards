package com.destrostudios.cards.frontend.application;

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

        ManaCost manaCost = new ManaCost(cardModel);
        manaCost.set(Color.RED, 1);
        manaCost.set(Color.GREEN, 1);
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
        Spell spell = new Spell(cardModel);
        Cost cost = new Cost(spell);
        cost.setTap(true);
        ManaCost spellManaCost = new ManaCost(cost);
        spellManaCost.set(Color.RED, 2);
        cost.setManaCost(spellManaCost);
        spell.setCost(cost);
        spell.setDescription("woop de doop");
        spells.add(spell);
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
}
