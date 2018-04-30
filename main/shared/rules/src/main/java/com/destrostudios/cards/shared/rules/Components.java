package com.destrostudios.cards.shared.rules;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.rules.turns.TurnPhase;

/**
 *
 * @author Philipp
 */
public class Components {

    public static final ComponentDefinition<Integer> HEALTH = new ComponentDefinition<>("health");
    public static final ComponentDefinition<Integer> OWNED_BY = new ComponentDefinition<>("ownedBy");
    public static final ComponentDefinition<Integer> NEXT_PLAYER = new ComponentDefinition<>("nextPlayer");
    public static final ComponentDefinition<Integer> ATTACK = new ComponentDefinition<>("attack");
    public static final ComponentDefinition<Integer> ARMOR = new ComponentDefinition<>("armor");
    public static final ComponentDefinition<Integer> DECLARED_ATTACK = new ComponentDefinition<>("declaredAttack");
    public static final ComponentDefinition<Integer> DECLARED_BLOCK = new ComponentDefinition<>("daclaredBlock");
    public static final ComponentDefinition<TurnPhase> TURN_PHASE = new ComponentDefinition<>("turnPhase");
    public static final ComponentDefinition<Integer> LIBRARY = new ComponentDefinition<>("library");
    public static final ComponentDefinition<String> DISPLAY_NAME = new ComponentDefinition<>("displayName");
    public static final ComponentDefinition<Integer> HAND_CARDS = new ComponentDefinition<>("hand");
    public static final ComponentDefinition<Void> BOARD = new ComponentDefinition<>("board");
    public static final ComponentDefinition<Integer> CREATURE_ZONE = new ComponentDefinition<>("creatureZone");
    public static final ComponentDefinition<Integer> MANA_ZONE = new ComponentDefinition<>("manaZone");
    public static final ComponentDefinition<Integer> ENCHANTMENT_ZONE = new ComponentDefinition<>("enchantmentZone");
    public static final ComponentDefinition<Integer> SPELL_ZONE= new ComponentDefinition<>("spellZone");
    public static final ComponentDefinition<Integer> CARD_TEMPLATE = new ComponentDefinition<>("cardTemplate");
    public static final ComponentDefinition<Void> CREATURE_CARD = new ComponentDefinition<>("creatureCard");
    public static final ComponentDefinition<Void> SPELL_CARD = new ComponentDefinition<>("spellCard");
    public static final ComponentDefinition<Void> ENTCHANTMENT_CARD = new ComponentDefinition<>("entchantmentCard");

}
