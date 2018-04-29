package com.destrostudios.cards.shared.rules;

/**
 *
 * @author Philipp
 */
public class Components {
    
    public static final int HEALTH;
    public static final int OWNED_BY;
    public static final int NEXT_PLAYER;
    public static final int ATTACK;
    public static final int ARMOR;
    public static final int DECLARED_ATTACK;
    public static final int DECLARED_BLOCK;
    public static final int DISPLAY_NAME;
    public static final int TURN_PHASE;
    public static final int LIBRARY;
    public static final int HAND;
    public static final int CARD_TEMPLATE;
    public static final int COMPONENTS_COUNT;
    
    static {
        int next = 0;
        HEALTH = next++;
        OWNED_BY = next++;
        NEXT_PLAYER = next++;
        ATTACK = next++;
        ARMOR = next++;
        DECLARED_ATTACK = next++;
        DECLARED_BLOCK = next++;
        DISPLAY_NAME = next++;
        TURN_PHASE = next++;
        LIBRARY = next++;
        HAND = next++;
        CARD_TEMPLATE = next++;
        
        COMPONENTS_COUNT = next;
    }
}
