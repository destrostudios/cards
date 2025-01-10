package com.destrostudios.cards.shared.rules.effects;

import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.CreateLocation;
import com.destrostudios.cards.shared.rules.EventType;

public class CreateEvent extends Event {

    public final int source;
    public final int player;
    public final String template;
    public final CreateLocation location;
    public final int[] triggers;

    public CreateEvent(int source, int player, String template, CreateLocation location, int[] triggers) {
        super(EventType.CREATE);
        this.source = source;
        this.player = player;
        this.template = template;
        this.location = location;
        this.triggers = triggers;
    }

    @Override
    public String toString() {
        return "CreateEvent{" + "source=" + source + ", player=" + player + ", template=" + template + ", location=" + location + ", triggers=" + triggers + "}";
    }
}
