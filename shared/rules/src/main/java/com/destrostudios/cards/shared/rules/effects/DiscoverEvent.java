package com.destrostudios.cards.shared.rules.effects;

import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.EventType;

public class DiscoverEvent extends Event {

    public final int source;
    public final int player;
    public final DiscoverPool pool;
    public final int discoverIndex;
    public final int[] triggers;

    public DiscoverEvent(int source, int player, DiscoverPool pool, int discoverIndex, int[] triggers) {
        super(EventType.DISCOVER);
        this.source = source;
        this.player = player;
        this.pool = pool;
        this.discoverIndex = discoverIndex;
        this.triggers = triggers;
    }

    @Override
    public String toString() {
        return "DiscoverEvent{" + "source=" + source + ", player=" + player + ", pool=" + pool + ", discoverIndex=" + discoverIndex + ", triggers=" + triggers + "}";
    }
}
