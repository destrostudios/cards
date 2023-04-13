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
    ADD_CARD_TO_CREATURE_ZONE,
    ADD_CARD_TO_GRAVEYARD,
    ADD_CARD_TO_HAND,
    ADD_CARD_TO_LIBRARY,
    ADD_CARD_TO_ZONE,
    REMOVE_CARD_FROM_CREATURE_ZONE,
    REMOVE_CARD_FROM_GRAVEYARD,
    REMOVE_CARD_FROM_HAND,
    REMOVE_CARD_FROM_LIBRARY,
    REMOVE_CARD_FROM_ZONE,
    // cards
    CAST_SPELL,
    DRAW_CARD,
    MULLIGAN,
    PAY_MANA,
    SHUFFLE_LIBRARY,
    // effects
    ADD_MANA,
    CREATE,
    SET_AVAILABLE_MANA,
    SET_MANA,
    TRIGGER_EFFECT,
    TRIGGER_EFFECT_IMPACT,
    TRIGGER_IF_POSSIBLE,
    // game/turn
    END_TURN,
    START_TURN,
    // game
    GAME_OVER,
    GAME_START,
}
