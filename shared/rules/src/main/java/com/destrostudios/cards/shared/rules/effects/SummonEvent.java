package com.destrostudios.cards.shared.rules.effects;

import com.destrostudios.cards.shared.events.Event;

public class SummonEvent extends Event {

    public final int player;
    public final String template;

    public SummonEvent(int player, String template) {
        this.player = player;
        this.template = template;
    }

    @Override
    public String toString() {
        return "SummonEvent{" + "player=" + player + ", template=" + template + '}';
    }
}
