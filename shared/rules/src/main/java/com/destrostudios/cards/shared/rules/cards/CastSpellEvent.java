package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.events.Event;

import java.util.Arrays;
import java.util.Objects;

public class CastSpellEvent extends Event {

    public int spell;
    public int[] targets;

    // Used by serializer
    private CastSpellEvent() {
        this(0, null);
    }

    public CastSpellEvent(int spell, int[] targets) {
        this.spell = spell;
        this.targets = targets;
    }

    @Override
    public String toString() {
        return "CastSpellEvent{" + "spell=" + spell + ", targets=" + targets + '}';
    }

    // Used by game-tools bot

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CastSpellEvent that = (CastSpellEvent) o;
        return spell == that.spell && Arrays.equals(targets, that.targets);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(spell);
        result = 31 * result + Arrays.hashCode(targets);
        return result;
    }
}
