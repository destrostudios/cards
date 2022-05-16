package com.destrostudios.cards.frontend.application.appstates.services;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.rules.Components;

import java.util.*;

public class SpellDescriptionGenerator {

    private static LinkedHashMap<ComponentDefinition, ComponentTextProvider> effectTextProvider = new LinkedHashMap<>();
    private static LinkedHashMap<ComponentDefinition, ComponentTextProvider> conditionTextProviders = new LinkedHashMap<>();
    private static LinkedHashMap<ComponentDefinition, ComponentTextProvider> targetCardTypeTextProviders = new LinkedHashMap<>();
    static {
        addComponentTextProvider(effectTextProvider, Components.Effect.DAMAGE, (entityData, damage) -> "deal " + damage + " damage to");
        addComponentTextProvider(effectTextProvider, Components.Effect.DRAW, (entityData, draw) -> "draw " + draw + " " + ((draw == 1) ? "card" : "cards"));
        addComponentTextProvider(effectTextProvider, Components.Effect.GAIN_MANA, (entityDate, mana) -> "gain " + mana + " mana");

        addComponentTextProvider(conditionTextProviders, Components.Condition.ALLY, (entityData, myVoid) -> "ally");
        addComponentTextProvider(conditionTextProviders, Components.Condition.OPPONENT, (entityData, myVoid) -> "opponent");
        addComponentTextProvider(conditionTextProviders, Components.Ability.SLOW, (entityData, myVoid) -> "slow");
        addComponentTextProvider(conditionTextProviders, Components.Ability.DIVINE_SHIELD, (entityData, myVoid) -> "divine shield");
        addComponentTextProvider(conditionTextProviders, Components.Ability.HEXPROOF, (entityData, myVoid) -> "hexproof");
        addComponentTextProvider(conditionTextProviders, Components.Ability.IMMUNE, (entityData, myVoid) -> "immune");
        addComponentTextProvider(conditionTextProviders, Components.Ability.TAUNT, (entityData, myVoid) -> "taunt");
        addComponentTextProvider(conditionTextProviders, Components.CREATURE_CARD, (entityData, myVoid) -> "creature");
        addComponentTextProvider(conditionTextProviders, Components.SPELL_CARD, (entityData, myVoid) -> "spell");
        addComponentTextProvider(conditionTextProviders, Components.Condition.IN_HAND, (entityData, myVoid) -> "in hand");
        addComponentTextProvider(conditionTextProviders, Components.Condition.ON_BOARD, (entityData, myVoid) -> "on board");
    }

    public static String generateDescription(EntityData data, int spell) {
        String description = "";

        String targetText = "target";
        int[] conditions = data.getComponent(spell, Components.CONDITIONS);
        if (conditions != null) {
            for (int condition : conditions) {
                if (data.hasComponent(condition, Components.Target.TARGET_TARGET)) {
                    targetText += " " + generateText(data, condition, conditionTextProviders, " ");
                    break;
                }
            }
        }

        int[] instantEffectTriggers = data.getComponent(spell, Components.Spell.INSTANT_EFFECT_TRIGGERS);
        if (instantEffectTriggers != null) {
            for (int effectTrigger : instantEffectTriggers) {
                int[] effects = data.getComponent(effectTrigger, Components.EffectTrigger.EFFECTS);
                for (int effect : effects) {
                    String effectText = generateText(data, effect, effectTextProvider, " and ");
                    if (effectText.length() > 0) {
                        if (!description.isEmpty()) {
                            description += " and ";
                        }
                        description += effectText;
                        if (data.hasComponent(effect, Components.Target.TARGET_TARGET)) {
                            description += " " + targetText;
                        }
                    }
                }
            }
        }

        if (description.length() > 0) {
            return description.substring(0, 1).toUpperCase() + description.substring(1) + ".";
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

    private interface ComponentTextProvider<ComponentType> {
        String generateText(EntityData entityData, ComponentType componentValue);
    }
}
