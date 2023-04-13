package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.EventType;

import java.util.Arrays;
import java.util.Objects;

public class CastSpellEvent extends Event {

    public int source;
    public int spell;
    public int[] targets;

    // Used by serializer
    private CastSpellEvent() {
        this(0, 0, null);
    }

    public CastSpellEvent(int source, int spell, int[] targets) {
        super(EventType.CAST_SPELL);
        this.source = source;
        this.spell = spell;
        this.targets = targets;
    }

    @Override
    public String toString() {
        return "CastSpellEvent{" + "source=" + source + ", spell=" + spell + ", targets=" + targets + '}';
    }

    // Used by game-tools bot

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CastSpellEvent that = (CastSpellEvent) o;
        return source == that.source && spell == that.spell && Arrays.equals(targets, that.targets);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(source, spell);
        result = 31 * result + Arrays.hashCode(targets);
        return result;
    }
}
