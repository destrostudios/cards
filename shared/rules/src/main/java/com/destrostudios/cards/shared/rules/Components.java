package com.destrostudios.cards.shared.rules;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.rules.cards.Foil;

public class Components {

    public static final ComponentDefinition<String> NAME = new ComponentDefinition<>("name");
    public static final ComponentDefinition<Void> CHARACTER = new ComponentDefinition<>("character");
    public static final ComponentDefinition<Void> BOARD = new ComponentDefinition<>("board");
    public static final ComponentDefinition<Void> CREATURE_CARD = new ComponentDefinition<>("creatureCard");
    public static final ComponentDefinition<Integer> CREATURE_ZONE = new ComponentDefinition<>("creatureZone");
    public static final ComponentDefinition<String> FLAVOUR_TEXT = new ComponentDefinition<>("flavourText");
    public static final ComponentDefinition<Integer> HAND = new ComponentDefinition<>("hand");
    public static final ComponentDefinition<Integer> LIBRARY = new ComponentDefinition<>("library");
    public static final ComponentDefinition<Integer> GRAVEYARD = new ComponentDefinition<>("graveyard");
    public static final ComponentDefinition<Integer> SPELL_ZONE = new ComponentDefinition<>("spellZone");
    public static final ComponentDefinition<Integer> NEXT_PLAYER = new ComponentDefinition<>("nextPlayer");
    public static final ComponentDefinition<Integer> OWNED_BY = new ComponentDefinition<>("ownedBy");
    public static final ComponentDefinition<int[]> AURAS = new ComponentDefinition<>("auras");
    public static final ComponentDefinition<int[]> BUFFS = new ComponentDefinition<>("buffs");
    public static final ComponentDefinition<int[]> CONDITIONS = new ComponentDefinition<>("conditions");
    public static final ComponentDefinition<Void> SPELL_CARD = new ComponentDefinition<>("spellCard");
    public static final ComponentDefinition<int[]> SPELL_ENTITIES = new ComponentDefinition<>("spells");
    public static final ComponentDefinition<Integer> AVAILABLE_MANA = new ComponentDefinition<>("availableMana");
    public static final ComponentDefinition<Integer> MANA = new ComponentDefinition<>("mana");
    public static final ComponentDefinition<Integer> COST = new ComponentDefinition<>("cost");
    public static final ComponentDefinition<String> DESCRIPTION = new ComponentDefinition<>("description");
    public static final ComponentDefinition<Foil> FOIL = new ComponentDefinition<>("foil");

    public static class Stats {
        public static final ComponentDefinition<Integer> ATTACK = new ComponentDefinition<>("attack");
        public static final ComponentDefinition<Integer> BONUS_ATTACK = new ComponentDefinition<>("bonusAttack");
        public static final ComponentDefinition<Integer> HEALTH = new ComponentDefinition<>("health");
        public static final ComponentDefinition<Integer> BONUS_HEALTH = new ComponentDefinition<>("bonusHealth");
        public static final ComponentDefinition<Integer> DAMAGED = new ComponentDefinition<>("damaged");
        public static final ComponentDefinition<Integer> BONUS_DAMAGED = new ComponentDefinition<>("bonusDamaged");
    }

    public static class Game {
        public static final ComponentDefinition<Void> ACTIVE_PLAYER = new ComponentDefinition<>("activePlayer");
    }

    public static class Ability {
        public static final ComponentDefinition<Void> SLOW = new ComponentDefinition<>("slow");
        public static final ComponentDefinition<Boolean> DIVINE_SHIELD = new ComponentDefinition<>("divineShield");
        public static final ComponentDefinition<Void> HEXPROOF = new ComponentDefinition<>("hexproof");
        public static final ComponentDefinition<Void> IMMUNE = new ComponentDefinition<>("immune");
        public static final ComponentDefinition<Void> TAUNT = new ComponentDefinition<>("taunt");
    }

    public static class Aura {
        public static final ComponentDefinition<Integer> AURA_BUFF = new ComponentDefinition<>("auraBuff");
    }

    public static class Tribe {
        public static final ComponentDefinition<Void> BEAST = new ComponentDefinition<>("beast");
        public static final ComponentDefinition<Void> DRAGON = new ComponentDefinition<>("dragon");
        public static final ComponentDefinition<Void> FISH = new ComponentDefinition<>("fish");
        public static final ComponentDefinition<Void> GOD = new ComponentDefinition<>("god");
        public static final ComponentDefinition<Void> HUMAN = new ComponentDefinition<>("human");
    }

    public static class EffectTrigger {
        public static final ComponentDefinition<int[]> EFFECTS = new ComponentDefinition<>("effects");
    }

    public static class Effect {
        public static final ComponentDefinition<Integer> DAMAGE = new ComponentDefinition<>("damage");
        public static final ComponentDefinition<Integer> HEAL = new ComponentDefinition<>("heal");
        public static final ComponentDefinition<Void> BATTLE = new ComponentDefinition<>("battle");
        public static final ComponentDefinition<Integer> DRAW = new ComponentDefinition<>("draw");
        public static final ComponentDefinition<Integer> GAIN_MANA = new ComponentDefinition<>("gainMana");
        public static final ComponentDefinition<Void> DESTROY = new ComponentDefinition<>("destroy");
        public static final ComponentDefinition<int[]> ADD_AURAS = new ComponentDefinition<>("addAuras");
        public static final ComponentDefinition<int[]> REMOVE_AURAS = new ComponentDefinition<>("removeAuras");
        public static final ComponentDefinition<int[]> ADD_BUFFS = new ComponentDefinition<>("addBuffs");
        public static final ComponentDefinition<int[]> REMOVE_BUFFS = new ComponentDefinition<>("removeBuffs");

        public static class Zones {
            public static final ComponentDefinition<Void> ADD_TO_BOARD = new ComponentDefinition<>("addToBoard");
            public static final ComponentDefinition<Void> ADD_TO_GRAVEYARD = new ComponentDefinition<>("addToGraveyard");
        }
    }

    public static class Target {
        public static final ComponentDefinition<int[]> TARGET_CHAINS = new ComponentDefinition<>("targetChains");
        public static final ComponentDefinition<int[]> TARGET_CHAIN = new ComponentDefinition<>("targetChain");
        public static final ComponentDefinition<Void> TARGET_SOURCE = new ComponentDefinition<>("targetSource");
        public static final ComponentDefinition<Void> TARGET_TARGETS = new ComponentDefinition<>("targetTargets");
        public static final ComponentDefinition<int[]> TARGET_CUSTOM = new ComponentDefinition<>("targetCustom");
        public static final ComponentDefinition<int[]> TARGET_ALL = new ComponentDefinition<>("targetAll");
        public static final ComponentDefinition<Void> TARGET_OWNER = new ComponentDefinition<>("targetOwner");
    }

    public static class Condition {
        public static final ComponentDefinition<int[]> ONE_OF = new ComponentDefinition<>("oneOf");
        public static final ComponentDefinition<Void> NOT = new ComponentDefinition<>("not");
        public static final ComponentDefinition<Void> ALLY = new ComponentDefinition<>("ally");
        public static final ComponentDefinition<Void> OPPONENT = new ComponentDefinition<>("opponent");
        public static final ComponentDefinition<Void> PLAYER = new ComponentDefinition<>("player");
        public static final ComponentDefinition<Void> IN_HAND = new ComponentDefinition<>("inHand");
        public static final ComponentDefinition<Void> ON_BOARD = new ComponentDefinition<>("onBoard");
        public static final ComponentDefinition<Void> NO_CREATURES = new ComponentDefinition<>("noCreatures");
    }

    public static class Spell {
        public static final ComponentDefinition<Void> TARGET_OPTIONAL = new ComponentDefinition<>("targetOptional");
        public static final ComponentDefinition<Integer> CURRENT_CASTS_PER_TURN = new ComponentDefinition<>("currentCastsThisTurn");
        public static final ComponentDefinition<Integer> MAXIMUM_CASTS_PER_TURN = new ComponentDefinition<>("maximumCastsPerTurn");
        public static final ComponentDefinition<Void> TAUNTABLE = new ComponentDefinition<>("tauntable");
        public static final ComponentDefinition<int[]> INSTANT_EFFECT_TRIGGERS = new ComponentDefinition<>("instantEffectTriggers");
    }
}
