package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.events.Event;

public class PlaySpellEvent extends Event {
    public int spell;

    // Used by serializer
    private PlaySpellEvent() { }

    public PlaySpellEvent(int spell) {
        this.spell = spell;
    }
}
