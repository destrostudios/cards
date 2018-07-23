package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.util.MixedManaAmount;

public class PlaySpellEvent extends Event {

    public int spell;
    public MixedManaAmount payedMana;
    public int[] targets;

    // Used by serializer
    private PlaySpellEvent() {
        this(0, new MixedManaAmount());
    }

    public PlaySpellEvent(int spell) {
        this(spell, new MixedManaAmount(), new int[0]);
    }

    public PlaySpellEvent(int spell, MixedManaAmount payedMana) {
        this(spell, payedMana, new int[0]);
    }

    public PlaySpellEvent(int spell, int[] targets) {
        this(spell, new MixedManaAmount(), targets);
    }

    public PlaySpellEvent(int spell, MixedManaAmount payedMana, int[] targets) {
        this.spell = spell;
        this.payedMana = payedMana;
        this.targets = targets;
    }

    @Override
    public String toString() {
        return "PlaySpellEvent{" + "spell=" + spell + ", payedMana=" + payedMana + ", targets=" + targets + '}';
    }
}
