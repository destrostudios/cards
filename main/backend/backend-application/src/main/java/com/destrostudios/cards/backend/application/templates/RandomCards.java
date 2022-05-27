package com.destrostudios.cards.backend.application.templates;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.cards.Foil;

import java.util.LinkedList;
import java.util.List;

public class RandomCards {

    public static int randomCard(EntityData data) {
        int card = data.createEntity();
        // Foil
        int foil = random(0, 5);
        if (foil == 0) {
            data.setComponent(card, Components.FOIL, Foil.ARTWORK);
        } else if (foil == 1) {
            data.setComponent(card, Components.FOIL, Foil.FULL);
        }
        int manaCost = random(0, 10);
        boolean targeted = random();
        LinkedList<Integer> spells = new LinkedList<>();
        int fromHandSpell = randomSpell(data, manaCost, targeted, Components.Condition.IN_HAND);
        int fromHandInstantEffectTrigger = data.getComponent(fromHandSpell, Components.Spell.INSTANT_EFFECT_TRIGGERS)[0];
        int fromHandSourceEffect = data.getComponent(fromHandInstantEffectTrigger, Components.EffectTrigger.EFFECTS)[0];
        spells.add(fromHandSpell);
        if (random()) {
            data.setComponent(card, Components.NAME, "Random Creature");
            data.setComponent(card, Components.CREATURE_CARD);
            if (targeted) {
                data.setComponent(fromHandSpell, Components.Spell.TARGET_OPTIONAL);
            }
            data.setComponent(fromHandSourceEffect, Components.Effect.Zones.ADD_TO_BOARD);
            int attack = random(1, (int) (1.5f * manaCost));
            data.setComponent(card, Components.ATTACK, attack);
            int health = random(1, (int) (2 + (1.5f * manaCost)));
            data.setComponent(card, Components.HEALTH, health);
            // Board spell
            if (random()) {
                int boardSpell = randomBoardSpell(data, manaCost);
                spells.add(boardSpell);
            }
        } else {
            data.setComponent(card, Components.SPELL_CARD);
            if (random(0, 4) == 0) {
                data.setComponent(card, Components.NAME, "Random Permanent Spell");
                data.setComponent(fromHandSourceEffect, Components.Effect.Zones.ADD_TO_BOARD);
                // Board spell
                int boardSpell = randomBoardSpell(data, manaCost);
                spells.add(boardSpell);
            } else {
                data.setComponent(card, Components.NAME, "Random Spell");
                data.setComponent(fromHandSourceEffect, Components.Effect.Zones.ADD_TO_GRAVEYARD);
            }
        }
        data.setComponent(card, Components.SPELL_ENTITIES, toArray(spells));
        return card;
    }

    private static int randomBoardSpell(EntityData data, int cardManaCost) {
        int spellManaCost = random(1, cardManaCost / 3);
        boolean targeted = random();
        return randomSpell(data, spellManaCost, targeted, Components.Condition.ON_BOARD);
    }

    private static int randomSpell(EntityData data, int manaCost, boolean targeted, ComponentDefinition<Void> sourceFromZoneComponent) {
        int spell = data.createEntity();
        // Cost
        int cost = data.createEntity();
        data.setComponent(cost, Components.MANA, manaCost);
        data.setComponent(spell, Components.COST, cost);
        // Conditions
        LinkedList<Integer> conditions = new LinkedList<>();
        int sourceFromZoneCondition = data.createEntity();
        data.setComponent(sourceFromZoneCondition, Components.Target.SOURCE_TARGET);
        data.setComponent(sourceFromZoneCondition, sourceFromZoneComponent);
        conditions.add(sourceFromZoneCondition);
        // Targeted
        if (targeted) {
            int targetCondition = data.createEntity();
            data.setComponent(targetCondition, Components.Target.TARGET_TARGETS);
            data.setComponent(targetCondition, Components.CREATURE_CARD);
            data.setComponent(targetCondition, Components.Condition.ON_BOARD);
            conditions.add(targetCondition);
        }
        data.setComponent(spell, Components.CONDITIONS, toArray(conditions));
        // Instant effect triggers
        int instantEffectTrigger = data.createEntity();
        LinkedList<Integer> effects = new LinkedList<>();
        int sourceEffect = data.createEntity();
        data.setComponent(sourceEffect, Components.Target.SOURCE_TARGET);
        boolean hasDraw = random(0, 2) == 0;
        if (hasDraw) {
            int draw = random(1, manaCost / 3);
            data.setComponent(sourceEffect, Components.Effect.DRAW, draw);
        }
        effects.add(sourceEffect);
        if (targeted) {
            int targetEffect = data.createEntity();
            data.setComponent(targetEffect, Components.Target.TARGET_TARGETS);
            if (!hasDraw || random()) {
                addRandomTargetEffects(data, targetEffect, manaCost);
            }
            effects.add(targetEffect);
        }
        if ((!targeted && !hasDraw) || (random(0, 3) == 0)) {
            int conditionTargetsEffect = data.createEntity();
            int targetCondition = data.createEntity();
            data.setComponent(targetCondition, Components.Target.TARGET_TARGETS);
            int targetOwner = random(0, 2);
            if (targetOwner == 0) {
                data.setComponent(targetCondition, Components.Condition.OPPONENT);
            } else if (targetOwner == 1) {
                data.setComponent(targetCondition, Components.Condition.ALLY);
            }
            data.setComponent(targetCondition, Components.CREATURE_CARD);
            data.setComponent(targetCondition, Components.Condition.ON_BOARD);
            data.setComponent(conditionTargetsEffect, Components.Target.CONDITION_TARGETS, new int[] { targetCondition });
            addRandomTargetEffects(data, conditionTargetsEffect, manaCost / 2);
            effects.add(conditionTargetsEffect);
        }
        data.setComponent(instantEffectTrigger, Components.EffectTrigger.EFFECTS, toArray(effects));
        data.setComponent(spell, Components.Spell.INSTANT_EFFECT_TRIGGERS, new int[] { instantEffectTrigger });
        return spell;
    }

    private static void addRandomTargetEffects(EntityData data, int effect, int manaCost) {
        boolean damageAndHeal = random(0, 5) == 0;
        boolean damageOrHeal = random();
        if (damageAndHeal || damageOrHeal) {
            int damage = random(1, manaCost);
            data.setComponent(effect, Components.Effect.DAMAGE, damage);
        }
        if (damageAndHeal || !damageOrHeal) {
            int heal = random(1, manaCost);
            data.setComponent(effect, Components.Effect.HEAL, heal);
        }
    }

    private static boolean random() {
        return (Math.random() < 0.5);
    }

    private static int random(int min, int max) {
        return (min + ((int) (Math.random() * (max - min))));
    }

    private static int[] toArray(List<Integer> list) {
        int[] array = new int[list.size()];
        int i = 0;
        for (Integer value : list) {
            array[i++] = value;
        }
        return array;
    }
}
