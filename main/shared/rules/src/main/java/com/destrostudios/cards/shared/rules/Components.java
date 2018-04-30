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
    public static final ComponentDefinition<Integer> HAND = new ComponentDefinition<>("hand");
    public static final ComponentDefinition<Integer> CARD_TEMPLATE = new ComponentDefinition<>("cardTemplate");

}
