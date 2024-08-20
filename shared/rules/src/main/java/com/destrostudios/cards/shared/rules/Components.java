package com.destrostudios.cards.shared.rules;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.entities.IntList;
import com.destrostudios.cards.shared.rules.cards.Foil;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

public class Components {

    public static final ArrayList<ComponentDefinition<?>> ALL = new ArrayList<>();

    public static void setup() {
        // TODO: Cleanup the components setup - For the tests, we need to ensure these nested ones are initalized, otherwise Components.ALL does not include them yet when the tests run
        Object initializedNestedStaticInnerClassArrayField = Components.Zone.PLAYER_LIBRARY;
    }

    static <T> ComponentDefinition<T>[] create(String name, int count) {
        ComponentDefinition<T>[] components = new ComponentDefinition[count];
        for (int i = 0; i < count; i++) {
            components[i] = create(name + "_" + count);
        }
        return components;
    }

    static <T> ComponentDefinition<T> create(String name) {
        ComponentDefinition<T> component = new ComponentDefinition<>(ALL.size(), name);
        ALL.add(component);
        return component;
    }

    public static final ComponentDefinition<String> NAME = create("name");
    public static final ComponentDefinition<Integer> SOURCE = create("source");
    public static final ComponentDefinition<Void> BOARD = create("board");
    public static final ComponentDefinition<Void> CREATURE_CARD = create("creatureCard");
    public static final ComponentDefinition<String> FLAVOUR_TEXT = create("flavourText");
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
    public static final ComponentDefinition<Void> LEGENDARY = create("legendary");
    public static final ComponentDefinition<Foil> FOIL = create("foil");

    public static class Zone {
        public static final ComponentDefinition<Void> LIBRARY = create("library");
        public static final ComponentDefinition<Void> HAND = create("hand");
        public static final ComponentDefinition<Void> CREATURE_ZONE = create("creatureZone");
        public static final ComponentDefinition<Void> GRAVEYARD = create("graveyard");
        public static final ComponentDefinition<Void>[] PLAYER_LIBRARY = create("library", 2);
        public static final ComponentDefinition<Void>[] PLAYER_HAND = create("hand", 2);
        public static final ComponentDefinition<Void>[] PLAYER_CREATURE_ZONE = create("creatureZone", 2);
        public static final ComponentDefinition<Void>[] PLAYER_GRAVEYARD = create("graveyard", 2);
    }

    public static class Cost {
        public static final ComponentDefinition<Integer> MANA_COST = create("manaCost");
        public static final ComponentDefinition<String> BONUS_MANA_COST = create("bonusManaCost");
        public static final ComponentDefinition<String> SET_MANA_COST = create("setManaCost");
    }

    public static class Stats {
        public static final ComponentDefinition<Integer> ATTACK = create("attack");
        public static final ComponentDefinition<String> BONUS_ATTACK = create("bonusAttack");
        public static final ComponentDefinition<String> SET_ATTACK = create("setAttack");
        public static final ComponentDefinition<Integer> HEALTH = create("health");
        public static final ComponentDefinition<String> BONUS_HEALTH = create("bonusHealth");
        public static final ComponentDefinition<String> SET_HEALTH = create("setHealth");
        public static final ComponentDefinition<Integer> DAMAGED = create("damaged");
        public static final ComponentDefinition<Integer> BONUS_DAMAGED = create("bonusDamaged");
    }

    public static class Player {
        public static final ComponentDefinition<Void> MULLIGAN = create("mulligan");
        public static final ComponentDefinition<Void> ACTIVE_PLAYER = create("activePlayer");
        public static final ComponentDefinition<IntList> LIBRARY_CARDS = create("libraryCards");
        public static final ComponentDefinition<IntList> HAND_CARDS = create("handCards");
        public static final ComponentDefinition<IntList> CREATURE_ZONE_CARDS = create("creatureZoneCards");
        public static final ComponentDefinition<IntList> GRAVEYARD_CARDS = create("graveyardCards");
    }

    public static class Ability {
        public static final ComponentDefinition<Boolean> DIVINE_SHIELD = create("divineShield");
        public static final ComponentDefinition<Void> TAUNT = create("taunt");
    }

    public static class Aura {
        public static final ComponentDefinition<Integer> AURA_BUFF = create("auraBuff");
    }

    public static class Buff {
        public static final ComponentDefinition<Void> UNTIL_END_OF_TURN = create("untilEndOfTurn");
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
        public static final ComponentDefinition<Create> CREATE = create("create");
        public static final ComponentDefinition<Void> END_TURN = create("endTurn");
        public static final ComponentDefinition<String[]> PRE_ANIMATIONS = create("preAnimations");
        public static final ComponentDefinition<String[]> POST_ANIMATIONS = create("postAnimations");

        public static class Zones {
            public static final ComponentDefinition<Void> MOVE_TO_HAND = create("moveToHand");
            public static final ComponentDefinition<Void> MOVE_TO_CREATURE_ZONE = create("moveToCreatureZone");
            public static final ComponentDefinition<Void> MOVE_TO_GRAVEYARD = create("moveToGraveyard");
            public static final ComponentDefinition<Void> MOVE_TO_LIBRARY = create("moveToLibrary");
        }
    }

    public static class Target {
        public static final ComponentDefinition<Prefilters> SOURCE_PREFILTERS = create("sourcePrefilters");
        public static final ComponentDefinition<Prefilters> TARGET_PREFILTERS = create("targetPrefilters");
        public static final ComponentDefinition<int[]> TARGETS = create("targets");
        public static final ComponentDefinition<SimpleTarget[]> TARGET_SIMPLE = create("targetSimple");
        public static final ComponentDefinition<String> TARGET_CUSTOM = create("targetCustom");
        public static final ComponentDefinition<String> TARGET_ALL = create("targetAll");
        public static final ComponentDefinition<String> TARGET_RANDOM = create("targetRandom");
    }

    public static class Spell {
        public static final ComponentDefinition<Void> TARGET_OPTIONAL = create("targetOptional");
        public static final ComponentDefinition<Integer> MINIMUM_TARGETS = create("minimumTargets");
        public static final ComponentDefinition<Integer> MAXIMUM_TARGETS = create("maximumTargets");
        public static final ComponentDefinition<Integer> CURRENT_CASTS_PER_TURN = create("currentCastsThisTurn");
        public static final ComponentDefinition<Integer> MAXIMUM_CASTS_PER_TURN = create("maximumCastsPerTurn");
        public static final ComponentDefinition<Void> TAUNTABLE = create("tauntable");
        public static final ComponentDefinition<int[]> CAST_TRIGGERS = create("castTriggers");
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor
    @Getter
    public static class Prefilters {
        private ComponentDefinition<?>[] basicComponents;
        private AdvancedPrefilter[] advanced;
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor
    @Getter
    public static class AddBuff {
        private int buff;
        private boolean constant;
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor
    @Getter
    public static class Create {
        private String template;
        private CreateLocation location;
    }
}
