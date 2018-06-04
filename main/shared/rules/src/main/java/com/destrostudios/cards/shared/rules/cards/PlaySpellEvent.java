package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.events.Event;

public class PlaySpellEvent extends Event {
    public int spell;
    public int[] targets;

    // Used by serializer
    private PlaySpellEvent() {
        this(0, null);
    }

    public PlaySpellEvent(int spell) {
        this(spell, new int[0]);
    }

    public PlaySpellEvent(int spell, int[] targets) {
        this.spell = spell;
        this.targets = targets;
    }

    @Override
    public String toString() {
        return "PlaySpellEvent{" + "spell=" + spell + ", targets=" + targets + '}';
    }
}
