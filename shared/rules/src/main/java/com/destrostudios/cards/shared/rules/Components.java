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

    static <T> ComponentDefinition<T> create(String name) {
        ComponentDefinition<T> componentDefinition = new ComponentDefinition<>(ALL.size(), name);
        ALL.add(componentDefinition);
        return componentDefinition;
    }

    public static final ComponentDefinition<String> NAME = create("name");
    public static final ComponentDefinition<Integer> SOURCE = create("source");
    public static final ComponentDefinition<Void> BOARD = create("board");
    public static final ComponentDefinition<Void> CREATURE_CARD = create("creatureCard");
    public static final ComponentDefinition<Integer> CREATURE_ZONE = create("creatureZone");
    public static final ComponentDefinition<String> FLAVOUR_TEXT = create("flavourText");
    public static final ComponentDefinition<Integer> HAND = create("hand");
    public static final ComponentDefinition<Integer> LIBRARY = create("library");
    public static final ComponentDefinition<Integer> GRAVEYARD = create("graveyard");
    public static final ComponentDefinition<Integer> NEXT_PLAYER = create("nextPlayer");
    public static final ComponentDefinition<Integer> OWNED_BY = create("ownedBy");
    public static final ComponentDefinition<int[]> AURAS = create("auras");
    public static final ComponentDefinition<int[]> BUFFS = create("buffs");
    public static final ComponentDefinition<String> CONDITION = create("condition");
    public static final ComponentDefinition<Void> SPELL_CARD = create("spellCard");
    public static final ComponentDefinition<int[]> SPELLS = create("spells");
    public static final ComponentDefinition<Integer> AVAILABLE_MANA = create("availableMana");
    public static final ComponentDefinition<Integer> MANA = create("mana");
    public static final ComponentDefinition<String> DESCRIPTION = create("description");
    public static final ComponentDefinition<Foil> FOIL = create("foil");

    public static class Cost {
        public static final ComponentDefinition<Integer> MANA_COST = create("manaCost");
        public static final ComponentDefinition<String> BONUS_MANA_COST = create("bonusManaCost");
    }

    public static class Stats {
        public static final ComponentDefinition<Integer> ATTACK = create("attack");
        public static final ComponentDefinition<String> BONUS_ATTACK = create("bonusAttack");
        public static final ComponentDefinition<Integer> HEALTH = create("health");
        public static final ComponentDefinition<String> BONUS_HEALTH = create("bonusHealth");
        public static final ComponentDefinition<Integer> DAMAGED = create("damaged");
        public static final ComponentDefinition<Integer> BONUS_DAMAGED = create("bonusDamaged");
    }

    public static class Game {
        public static final ComponentDefinition<Void> ACTIVE_PLAYER = create("activePlayer");
    }

    public static class Ability {
        public static final ComponentDefinition<Boolean> DIVINE_SHIELD = create("divineShield");
        public static final ComponentDefinition<Void> TAUNT = create("taunt");
    }

    public static class Aura {
        public static final ComponentDefinition<Integer> AURA_BUFF = create("auraBuff");
    }

    public static class Tribe {
        public static final ComponentDefinition<Void> BEAST = create("beast");
        public static final ComponentDefinition<Void> DRAGON = create("dragon");
        public static final ComponentDefinition<Void> GOBLIN = create("goblin");
    }

    public static class Trigger {
        public static final ComponentDefinition<int[]> EFFECTS = create("effects");
    }

    public static class Effect {
        public static final ComponentDefinition<String> REPEAT = create("repeat");
        public static final ComponentDefinition<String> DAMAGE = create("damage");
        public static final ComponentDefinition<String> HEAL = create("heal");
        public static final ComponentDefinition<String> DRAW = create("draw");
        public static final ComponentDefinition<String> GAIN_MANA = create("gainMana");
        public static final ComponentDefinition<Void> DESTROY = create("destroy");
        public static final ComponentDefinition<Void> BATTLE = create("battle");
        public static final ComponentDefinition<AddBuff> ADD_BUFF = create("addBuff");
        public static final ComponentDefinition<String[]> SUMMON = create("summon");

        public static class Zones {
            public static final ComponentDefinition<Void> ADD_TO_HAND = create("addToHand");
            public static final ComponentDefinition<Void> ADD_TO_BOARD = create("addToBoard");
            public static final ComponentDefinition<Void> ADD_TO_GRAVEYARD = create("addToGraveyard");
        }
    }

    public static class Target {
        public static final ComponentDefinition<TargetPrefilter> TARGET_PREFILTER = create("targetPrefilter");
        public static final ComponentDefinition<int[]> TARGETS = create("targets");
        public static final ComponentDefinition<String> TARGET = create("target");
        public static final ComponentDefinition<String> TARGET_ALL = create("targetAll");
        public static final ComponentDefinition<Integer> TARGET_RANDOM = create("targetRandom");
    }

    public static class Spell {
        public static final ComponentDefinition<Void> TARGET_OPTIONAL = create("targetOptional");
        public static final ComponentDefinition<Integer> CURRENT_CASTS_PER_TURN = create("currentCastsThisTurn");
        public static final ComponentDefinition<Integer> MAXIMUM_CASTS_PER_TURN = create("maximumCastsPerTurn");
        public static final ComponentDefinition<Void> TAUNTABLE = create("tauntable");
        public static final ComponentDefinition<int[]> CAST_TRIGGERS = create("castTriggers");
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor
    @Getter
    public static class AddBuff {
        private int buff;
        private boolean evaluated;
    }
}
