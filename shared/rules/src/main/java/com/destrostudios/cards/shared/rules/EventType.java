package com.destrostudios.cards.shared.rules;

public enum EventType {
    // battle
    BATTLE,
    CONDITIONS_AFFECTED,
    DAMAGE,
    DESTRUCTION,
    HEAL,
    // buffs
    ADD_BUFF,
    REMOVE_BUFF,
    // cards/zones
    MOVE_TO_CREATURE_ZONE,
    MOVE_TO_GRAVEYARD,
    MOVE_TO_HAND,
    MOVE_TO_LIBRARY,
    REMOVED_FROM_CREATURE_ZONE,
    REMOVED_FROM_HAND,
    // cards
    CAST_SPELL,
    DISCARD,
    DRAW_CARD,
    MULLIGAN,
    PAY_MANA,
    SHUFFLE_LIBRARY,
    // effects
    ADD_MANA,
    CREATE,
    DISCOVER,
    SET_AVAILABLE_MANA,
    SET_MANA,
    TRIGGER_EFFECT,
    TRIGGER_EFFECT_IMPACT,
    TRIGGER,
    // game/turn
    END_TURN,
    START_TURN,
    // game
    GAME_OVER,
    GAME_START,
}
