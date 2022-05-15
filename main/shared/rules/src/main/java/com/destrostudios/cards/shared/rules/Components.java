package com.destrostudios.cards.shared.rules;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.rules.cards.Foil;

/**
 *
 * @author Philipp
 */
public class Components {

    public static final ComponentDefinition<Integer> ATTACK = new ComponentDefinition<>("attack");
    public static final ComponentDefinition<Void> BOARD = new ComponentDefinition<>("board");
    public static final ComponentDefinition<Void> CREATURE_CARD = new ComponentDefinition<>("creatureCard");
    public static final ComponentDefinition<Integer> CREATURE_ZONE = new ComponentDefinition<>("creatureZone");
    public static final ComponentDefinition<Void> DAMAGED = new ComponentDefinition<>("damaged");
    public static final ComponentDefinition<String> DISPLAY_NAME = new ComponentDefinition<>("displayName");
    public static final ComponentDefinition<String> FLAVOUR_TEXT = new ComponentDefinition<>("flavourText");
    public static final ComponentDefinition<Integer> HAND_CARDS = new ComponentDefinition<>("hand");
    public static final ComponentDefinition<Integer> HEALTH = new ComponentDefinition<>("health");
    public static final ComponentDefinition<Integer> LIBRARY = new ComponentDefinition<>("library");
    public static final ComponentDefinition<Integer> GRAVEYARD = new ComponentDefinition<>("graveyard");
    public static final ComponentDefinition<Integer> SPELL_ZONE = new ComponentDefinition<>("spellZone");
    public static final ComponentDefinition<Integer> NEXT_PLAYER = new ComponentDefinition<>("nextPlayer");
    public static final ComponentDefinition<Integer> OWNED_BY = new ComponentDefinition<>("ownedBy");
    public static final ComponentDefinition<Void> SPELL_CARD = new ComponentDefinition<>("spellCard");
    public static final ComponentDefinition<int[]> SPELL_ENTITIES = new ComponentDefinition<>("spells");
    public static final ComponentDefinition<Integer> AVAILABLE_MANA = new ComponentDefinition<>("availableMana");
    public static final ComponentDefinition<Integer> MANA = new ComponentDefinition<>("mana");
    public static final ComponentDefinition<Void> HAS_ATTACKED = new ComponentDefinition<>("hasAttacked");
    public static final ComponentDefinition<Foil> FOIL = new ComponentDefinition<>("foil");

    public static class Game {
        public static final ComponentDefinition<Void> ACTIVE_PLAYER = new ComponentDefinition<>("activePlayer");
    }

    public static class Ability {
        public static final ComponentDefinition<Void> SLOW = new ComponentDefinition<>("slow");
        public static final ComponentDefinition<Void> DIVINE_SHIELD = new ComponentDefinition<>("divineShield");
        public static final ComponentDefinition<Void> HEXPROOF = new ComponentDefinition<>("hexproof");
        public static final ComponentDefinition<Void> IMMUNE = new ComponentDefinition<>("immune");
        public static final ComponentDefinition<Void> TAUNT = new ComponentDefinition<>("taunt");
    }

    public static class Tribe {
        public static final ComponentDefinition<Void> BEAST = new ComponentDefinition<>("beast");
        public static final ComponentDefinition<Void> DRAGON = new ComponentDefinition<>("dragon");
        public static final ComponentDefinition<Void> FISH = new ComponentDefinition<>("fish");
        public static final ComponentDefinition<Void> GOD = new ComponentDefinition<>("god");
        public static final ComponentDefinition<Void> HUMAN = new ComponentDefinition<>("human");
    }

    public static class Spell {
        public static final ComponentDefinition<Integer> COST_ENTITY = new ComponentDefinition<>("cost");
        public static final ComponentDefinition<Integer> TARGET_RULE = new ComponentDefinition<>("targetRule");
        public static final ComponentDefinition<Integer> SOURCE_EFFECT = new ComponentDefinition<>("sourceEffect");
        public static final ComponentDefinition<Integer> TARGET_EFFECT = new ComponentDefinition<>("targetEffect");

        public static class CastCondition {
            public static final ComponentDefinition<Void> FROM_HAND = new ComponentDefinition<>("fromHand");
            public static final ComponentDefinition<Void> FROM_BOARD = new ComponentDefinition<>("fromBoard");
        }

        public static class TargetRules {
            public static final ComponentDefinition<Void> ALLY = new ComponentDefinition<>("ally");
            public static final ComponentDefinition<Void> OPPONENT = new ComponentDefinition<>("opponent");
        }

        public static class Effect {
            public static final ComponentDefinition<Integer> DAMAGE = new ComponentDefinition<>("damage");
            public static final ComponentDefinition<Integer> GAIN_MANA = new ComponentDefinition<>("gainMana");

            public static class Zones {
                public static final ComponentDefinition<Void> ADD_TO_BOARD = new ComponentDefinition<>("addToBoard");
                public static final ComponentDefinition<Void> ADD_TO_GRAVEYARD = new ComponentDefinition<>("addToGraveyard");
            }
        }
    }
}
