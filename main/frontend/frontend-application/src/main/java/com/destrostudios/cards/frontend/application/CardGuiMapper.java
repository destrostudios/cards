package com.destrostudios.cards.frontend.application;

import com.destrostudios.cards.frontend.cardgui.Card;
import com.destrostudios.cards.frontend.cardpainter.model.CardModel;
import com.destrostudios.cards.frontend.cardpainter.model.Color;
import com.destrostudios.cards.frontend.cardpainter.model.Cost;
import com.destrostudios.cards.frontend.cardpainter.model.Spell;
import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.rules.Components;

import java.util.*;

public class CardGuiMapper {

    private static Map<ComponentDefinition, Color> colorComponents = new LinkedHashMap<>();
    private static Map<ComponentDefinition, String> keywordComponents = new LinkedHashMap<>();
    private static Map<ComponentDefinition, String> tribeComponents = new LinkedHashMap<>();
    static {
        colorComponents.put(Components.DISPLAY_NAME, Color.NEUTRAL);
        colorComponents.put(Components.DISPLAY_NAME, Color.WHITE);
        colorComponents.put(Components.DISPLAY_NAME, Color.RED);
        colorComponents.put(Components.DISPLAY_NAME, Color.GREEN);
        colorComponents.put(Components.DISPLAY_NAME, Color.BLUE);
        colorComponents.put(Components.DISPLAY_NAME, Color.BLACK);

        keywordComponents.put(Components.DISPLAY_NAME, "Charge");
        keywordComponents.put(Components.DISPLAY_NAME, "Taunt");
        keywordComponents.put(Components.DISPLAY_NAME, "Divine Shield");
        keywordComponents.put(Components.DISPLAY_NAME, "Immune");

        tribeComponents.put(Components.DISPLAY_NAME, "Human");
        tribeComponents.put(Components.DISPLAY_NAME, "Dragon");
        tribeComponents.put(Components.DISPLAY_NAME, "Beast");
        tribeComponents.put(Components.DISPLAY_NAME, "Fish");
        tribeComponents.put(Components.DISPLAY_NAME, "God");
    }

    public static void updateModel(Card<CardModel> card, EntityData entityData, int cardEntity) {
        CardModel cardModel = card.getModel();

        boolean isFront = entityData.has(cardEntity, Components.OWNED_BY);
        cardModel.setFront(isFront);

        List<Color> colors = createListBasedOnComponents(entityData, cardEntity, colorComponents);
        cardModel.setColors(colors);

        String displayName = entityData.get(cardEntity, Components.DISPLAY_NAME);
        cardModel.setTitle(displayName);

        String description = entityData.get(cardEntity, Components.DISPLAY_NAME);
        cardModel.setDescription(description);

        List<String> tribes = createListBasedOnComponents(entityData, cardEntity, tribeComponents);
        cardModel.setTribes(tribes);

        List<String> keywords = createListBasedOnComponents(entityData, cardEntity, keywordComponents);
        cardModel.setKeywords(keywords);

        String flavourText = entityData.get(cardEntity, Components.DISPLAY_NAME);
        cardModel.setFlavourText(flavourText);

        Integer attackDamage = entityData.get(cardEntity, Components.ATTACK);
        cardModel.setAttackDamage(attackDamage);

        Integer lifepoints = entityData.get(cardEntity, Components.HEALTH);
        cardModel.setLifepoints(lifepoints);

        boolean isDamaged = entityData.has(cardEntity, Components.HEALTH);
        cardModel.setDamaged(isDamaged);

        List<Spell> spells = new LinkedList<>();
        Spell spell = new Spell(cardModel);
        Cost cost = new Cost(spell);
        cost.setTap(true);
        cost.setManaCost(Color.RED, 2);
        spell.setCost(cost);
        spell.setDescription("woop de doop");
        spells.add(spell);
        cardModel.setSpells(spells);

        String castDescription = entityData.get(cardEntity, Components.DISPLAY_NAME);
        cardModel.setCastDescription(castDescription);
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
