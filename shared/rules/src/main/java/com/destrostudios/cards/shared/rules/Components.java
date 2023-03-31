package com.destrostudios.cards.shared.rules;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.rules.cards.Foil;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

public class Components {

    public static ArrayList<ComponentDefinition<?>> ALL = new ArrayList<>();

    private static <T> ComponentDefinition<T> createComponent(String name) {
        ComponentDefinition<T> componentDefinition = new ComponentDefinition<>(ALL.size(), name);
        ALL.add(componentDefinition);
        return componentDefinition;
    }

    public static final ComponentDefinition<String> NAME = createComponent("name");
    public static final ComponentDefinition<Integer> SOURCE = createComponent("source");
    public static final ComponentDefinition<Void> BOARD = createComponent("board");
    public static final ComponentDefinition<Void> CREATURE_CARD = createComponent("creatureCard");
    public static final ComponentDefinition<Integer> CREATURE_ZONE = createComponent("creatureZone");
    public static final ComponentDefinition<String> FLAVOUR_TEXT = createComponent("flavourText");
    public static final ComponentDefinition<Integer> HAND = createComponent("hand");
    public static final ComponentDefinition<Integer> LIBRARY = createComponent("library");
    public static final ComponentDefinition<Integer> GRAVEYARD = createComponent("graveyard");
    public static final ComponentDefinition<Integer> NEXT_PLAYER = createComponent("nextPlayer");
    public static final ComponentDefinition<Integer> OWNED_BY = createComponent("ownedBy");
    public static final ComponentDefinition<int[]> AURAS = createComponent("auras");
    public static final ComponentDefinition<int[]> BUFFS = createComponent("buffs");
    public static final ComponentDefinition<String> CONDITION = createComponent("condition");
    public static final ComponentDefinition<Void> SPELL_CARD = createComponent("spellCard");
    public static final ComponentDefinition<int[]> SPELLS = createComponent("spells");
    public static final ComponentDefinition<Integer> AVAILABLE_MANA = createComponent("availableMana");
    public static final ComponentDefinition<Integer> MANA = createComponent("mana");
    public static final ComponentDefinition<String> DESCRIPTION = createComponent("description");
    public static final ComponentDefinition<Foil> FOIL = createComponent("foil");
    public static final ComponentDefinition<int[]> DEATH_EFFECT_TRIGGERS = createComponent("deathEffectTriggers");

    public static class Cost {
        public static final ComponentDefinition<Integer> MANA_COST = createComponent("manaCost");
        public static final ComponentDefinition<String> BONUS_MANA_COST = createComponent("bonusManaCost");
    }

    public static class Stats {
        public static final ComponentDefinition<Integer> ATTACK = createComponent("attack");
        public static final ComponentDefinition<String> BONUS_ATTACK = createComponent("bonusAttack");
        public static final ComponentDefinition<Integer> HEALTH = createComponent("health");
        public static final ComponentDefinition<String> BONUS_HEALTH = createComponent("bonusHealth");
        public static final ComponentDefinition<Integer> DAMAGED = createComponent("damaged");
        public static final ComponentDefinition<Integer> BONUS_DAMAGED = createComponent("bonusDamaged");
    }

    public static class Game {
        public static final ComponentDefinition<Void> ACTIVE_PLAYER = createComponent("activePlayer");
    }

    public static class Ability {
        public static final ComponentDefinition<Boolean> DIVINE_SHIELD = createComponent("divineShield");
        public static final ComponentDefinition<Void> TAUNT = createComponent("taunt");
    }

    public static class Aura {
        public static final ComponentDefinition<Integer> AURA_BUFF = createComponent("auraBuff");
    }

    public static class Tribe {
        public static final ComponentDefinition<Void> BEAST = createComponent("beast");
        public static final ComponentDefinition<Void> DRAGON = createComponent("dragon");
    }

    public static class EffectTrigger {
        public static final ComponentDefinition<int[]> EFFECTS = createComponent("effects");
    }

    public static class Effect {
        public static final ComponentDefinition<String> REPEAT = createComponent("repeat");
        public static final ComponentDefinition<String> DAMAGE = createComponent("damage");
        public static final ComponentDefinition<String> HEAL = createComponent("heal");
        public static final ComponentDefinition<String> DRAW = createComponent("draw");
        public static final ComponentDefinition<String> GAIN_MANA = createComponent("gainMana");
        public static final ComponentDefinition<Void> DESTROY = createComponent("destroy");
        public static final ComponentDefinition<Void> BATTLE = createComponent("battle");
        public static final ComponentDefinition<AddBuff> ADD_BUFF = createComponent("addBuff");
        public static final ComponentDefinition<String[]> SUMMON = createComponent("summon");

        public static class Zones {
            public static final ComponentDefinition<Void> ADD_TO_HAND = createComponent("addToHand");
            public static final ComponentDefinition<Void> ADD_TO_BOARD = createComponent("addToBoard");
            public static final ComponentDefinition<Void> ADD_TO_GRAVEYARD = createComponent("addToGraveyard");
        }
    }

    public static class Target {
        public static final ComponentDefinition<TargetPrefilter> TARGET_PREFILTER = createComponent("targetPrefilter");
        public static final ComponentDefinition<int[]> TARGETS = createComponent("targets");
        public static final ComponentDefinition<String> TARGET = createComponent("target");
        public static final ComponentDefinition<String> TARGET_ALL = createComponent("targetAll");
        public static final ComponentDefinition<Integer> TARGET_RANDOM = createComponent("targetRandom");
    }

    public static class Spell {
        public static final ComponentDefinition<Void> TARGET_OPTIONAL = createComponent("targetOptional");
        public static final ComponentDefinition<Integer> CURRENT_CASTS_PER_TURN = createComponent("currentCastsThisTurn");
        public static final ComponentDefinition<Integer> MAXIMUM_CASTS_PER_TURN = createComponent("maximumCastsPerTurn");
        public static final ComponentDefinition<Void> TAUNTABLE = createComponent("tauntable");
        public static final ComponentDefinition<int[]> INSTANT_EFFECT_TRIGGERS = createComponent("instantEffectTriggers");
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor
    @Getter
    public static class AddBuff {
        private int buff;
        private boolean evaluated;
    }
}
