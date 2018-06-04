package com.destrostudios.cards.shared.rules;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.rules.game.phases.TurnPhase;

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
    public static final ComponentDefinition<Void> ENCHANTMENT_CARD = new ComponentDefinition<>("enchantmentCard");
    public static final ComponentDefinition<Integer> ENCHANTMENT_ZONE = new ComponentDefinition<>("enchantmentZone");
    public static final ComponentDefinition<String> FLAVOUR_TEXT = new ComponentDefinition<>("flavourText");
    public static final ComponentDefinition<Integer> HAND_CARDS = new ComponentDefinition<>("hand");
    public static final ComponentDefinition<Integer> HEALTH = new ComponentDefinition<>("health");
    public static final ComponentDefinition<Integer> LIBRARY = new ComponentDefinition<>("library");
    public static final ComponentDefinition<Integer> GRAVEYARD = new ComponentDefinition<>("graveyard");
    public static final ComponentDefinition<Void> LAND_CARD = new ComponentDefinition<>("landCard");
    public static final ComponentDefinition<Integer> LAND_ZONE = new ComponentDefinition<>("landZone");
    public static final ComponentDefinition<Integer> NEXT_PLAYER = new ComponentDefinition<>("nextPlayer");
    public static final ComponentDefinition<Integer> OWNED_BY = new ComponentDefinition<>("ownedBy");
    public static final ComponentDefinition<Void> SPELL_CARD = new ComponentDefinition<>("spellCard");
    public static final ComponentDefinition<Integer> SPELL_ZONE = new ComponentDefinition<>("spellZone");
    public static final ComponentDefinition<int[]> SPELL_ENTITIES = new ComponentDefinition<>("spells");
    public static final ComponentDefinition<Void> TAPPED = new ComponentDefinition<>("tapped");
    public static final ComponentDefinition<Integer> DECLARED_ATTACK = new ComponentDefinition<>("declaredAttack");
    public static final ComponentDefinition<Integer> DECLARED_BLOCK = new ComponentDefinition<>("declaredBlock");

    public static class Game {
        public static final ComponentDefinition<TurnPhase> TURN_PHASE = new ComponentDefinition<>("turnPhase");
    }

    public static class Color {
        public static final ComponentDefinition<Void> NEUTRAL = new ComponentDefinition<>("neutralColor");
        public static final ComponentDefinition<Void> WHITE = new ComponentDefinition<>("whiteColor");
        public static final ComponentDefinition<Void> RED = new ComponentDefinition<>("redColor");
        public static final ComponentDefinition<Void> GREEN = new ComponentDefinition<>("greenColor");
        public static final ComponentDefinition<Void> BLUE = new ComponentDefinition<>("blueColor");
        public static final ComponentDefinition<Void> BLACK = new ComponentDefinition<>("blackColor");
    }

    public static class Ability {
        public static final ComponentDefinition<Void> CHARGE = new ComponentDefinition<>("charge");
        public static final ComponentDefinition<Void> DIVINE_SHIELD = new ComponentDefinition<>("divineShield");
        public static final ComponentDefinition<Void> FLYING = new ComponentDefinition<>("flying");
        public static final ComponentDefinition<Void> HEXPROOF = new ComponentDefinition<>("hexproof");
        public static final ComponentDefinition<Void> IMMUNE = new ComponentDefinition<>("immune");
        public static final ComponentDefinition<Void> TAUNT = new ComponentDefinition<>("taunt");
        public static final ComponentDefinition<Void> VIGILANCE = new ComponentDefinition<>("vigilance");
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

        public static class CastCondition {
            public static final ComponentDefinition<Void> ATTACK_PHASE = new ComponentDefinition<>("castableInAttackPhase");
            public static final ComponentDefinition<Void> BLOCK_PHASE = new ComponentDefinition<>("castableInBlockPhase");
            public static final ComponentDefinition<Void> MAIN_PHASE = new ComponentDefinition<>("castableInMainPhase");

            public static final ComponentDefinition<Void> FROM_HAND = new ComponentDefinition<>("castableFromHand");
            public static final ComponentDefinition<Void> FROM_BOARD = new ComponentDefinition<>("castableFromBoard");
        }

        public static class Effect {
            public static final ComponentDefinition<Void> ADD_TO_BOARD = new ComponentDefinition<>("addToBoardEffect");
            public static final ComponentDefinition<Integer> DAMAGE = new ComponentDefinition<>("damageEffect");
            public static final ComponentDefinition<Void> TAP = new ComponentDefinition<>("tapEffect");
            public static final ComponentDefinition<Void> UNTAP = new ComponentDefinition<>("untapEffect");
        }
    }

    public static class Cost {
        public static final ComponentDefinition<Void> TAP = new ComponentDefinition<>("tapCost");
    }

    public static class ManaAmount {
        public static final ComponentDefinition<Integer> NEUTRAL = new ComponentDefinition<>("neutralMana");
        public static final ComponentDefinition<Integer> WHITE = new ComponentDefinition<>("whiteMana");
        public static final ComponentDefinition<Integer> RED = new ComponentDefinition<>("redMana");
        public static final ComponentDefinition<Integer> GREEN = new ComponentDefinition<>("greenMana");
        public static final ComponentDefinition<Integer> BLUE = new ComponentDefinition<>("blueMana");
        public static final ComponentDefinition<Integer> BLACK = new ComponentDefinition<>("blackMana");
    }
}
