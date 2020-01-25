package com.destrostudios.cards.frontend.application.appstates.services;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.rules.Components;

import java.util.*;

public class SpellDescriptionGenerator {

    private static LinkedHashMap<ComponentDefinition, ComponentTextProvider> actionTextProvider = new LinkedHashMap<>();
    private static LinkedHashMap<ComponentDefinition, ComponentTextProvider> targetCardAttributeTextProviders = new LinkedHashMap<>();
    private static LinkedHashMap<ComponentDefinition, ComponentTextProvider> targetCardTypeTextProviders = new LinkedHashMap<>();
    static {
        addComponentTextProvider(actionTextProvider, Components.Spell.Effect.GAIN_MANA, (entityData, manaEntity) -> getGainManaAction(entityData, manaEntity));
        addComponentTextProvider(actionTextProvider, Components.Spell.Effect.TAP, (entityData, myVoid) -> "tap");
        addComponentTextProvider(actionTextProvider, Components.Spell.Effect.UNTAP, (entityData, myVoid) -> "untap");
        addComponentTextProvider(actionTextProvider, Components.Spell.Effect.DAMAGE, (entityData, damage) -> "deal " + damage + " damage to");

        addComponentTextProvider(targetCardAttributeTextProviders, Components.TAPPED, (entityData, myVoid) -> "tapped");
        addComponentTextProvider(targetCardAttributeTextProviders, Components.Color.NEUTRAL, (entityData, myVoid) -> "neutral");
        addComponentTextProvider(targetCardAttributeTextProviders, Components.Color.WHITE, (entityData, myVoid) -> "white");
        addComponentTextProvider(targetCardAttributeTextProviders, Components.Color.RED, (entityData, myVoid) -> "red");
        addComponentTextProvider(targetCardAttributeTextProviders, Components.Color.GREEN, (entityData, myVoid) -> "green");
        addComponentTextProvider(targetCardAttributeTextProviders, Components.Color.BLUE, (entityData, myVoid) -> "blue");
        addComponentTextProvider(targetCardAttributeTextProviders, Components.Color.BLACK, (entityData, myVoid) -> "black");

        addComponentTextProvider(targetCardTypeTextProviders, Components.CREATURE_CARD, (entityData, myVoid) -> "creature");
        addComponentTextProvider(targetCardTypeTextProviders, Components.ENCHANTMENT_CARD, (entityData, myVoid) -> "enchantment");
        addComponentTextProvider(targetCardTypeTextProviders, Components.LAND_CARD, (entityData, myVoid) -> "land");
        addComponentTextProvider(targetCardTypeTextProviders, Components.SPELL_CARD, (entityData, myVoid) -> "spell");
    }

    public static String generateDescription(EntityData entityData, int spellEntity) {
        String description = "";

        // Actions
        description += generateText(entityData, spellEntity, actionTextProvider, " and ");

        // Target
        Integer targetRuleEntity = entityData.getComponent(spellEntity, Components.Spell.TARGET_RULE);
        if (targetRuleEntity != null) {
            description += " " + getTargetsOwned(entityData, targetRuleEntity);
            String targetAttributesText = generateText(entityData, targetRuleEntity, targetCardAttributeTextProviders, " ");
            if (!targetAttributesText.isEmpty()) {
                description += " " + targetAttributesText;
            }
            description += " " + generateText(entityData, targetRuleEntity, targetCardTypeTextProviders, " or ");
        }

        if (description.length() > 0) {
            return description.substring(0, 1).toUpperCase() + description.substring(1);
        }
        return null;
    }

    private static String generateText(EntityData entityData, int entity, HashMap<ComponentDefinition, ComponentTextProvider> componentTextProviders, String joinText) {
        String resultText = "";
        LinkedList<String> texts = generateTexts(entityData, entity, componentTextProviders);
        Iterator<String> textsIterator = texts.iterator();
        int i = 0;
        while (textsIterator.hasNext()) {
            String text = textsIterator.next();
            if (i > 0) {
                resultText += joinText;
            }
            resultText += text;
            i++;
        }
        return resultText;
    }

    private static LinkedList<String> generateTexts(EntityData entityData, int entity, HashMap<ComponentDefinition, ComponentTextProvider> componentTextProviders) {
        LinkedList<String> texts = new LinkedList<>();
        for (Map.Entry<ComponentDefinition, ComponentTextProvider> entry : componentTextProviders.entrySet()) {
            if (entityData.hasComponent(entity, entry.getKey())) {
                Object componentValue = entityData.getComponent(entity, entry.getKey());
                String text = entry.getValue().generateText(entityData, componentValue);
                texts.add(text);
            }
        }
        return texts;
    }

    private static <T> void addComponentTextProvider(HashMap<ComponentDefinition, ComponentTextProvider> componentTextProviders, ComponentDefinition<T> componentDefinition, ComponentTextProvider<T> componentTextProvider) {
        componentTextProviders.put(componentDefinition, componentTextProvider);
    }

    private static String getGainManaAction(EntityData entityData, int manaEntity) {
        // TODO: Generate text based on the manaEntity
        return "Gain ? mana";
    }

    private static String getTargetsOwned(EntityData entityData, int targetRuleEntity) {
        boolean targetsAlly = entityData.hasComponent(targetRuleEntity, Components.Spell.TargetRules.ALLY);
        boolean targetsOpponent = entityData.hasComponent(targetRuleEntity, Components.Spell.TargetRules.OPPONENT);
        if (targetsAlly) {
            if (!targetsOpponent) {
                return "ally";
            }
        }
        else if (targetsOpponent) {
            return "opponent";
        }
        return "target";
    }

    private interface ComponentTextProvider<ComponentType> {
        String generateText(EntityData entityData, ComponentType componentValue);
    }
}
