package com.destrostudios.cards.shared.rules.effects;

import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.CreateLocation;

public class CreateEvent extends Event {

    public final int player;
    public final String template;
    public final CreateLocation location;

    public CreateEvent(int player, String template, CreateLocation location) {
        this.player = player;
        this.template = template;
        this.location = location;
    }

    @Override
    public String toString() {
        return "CreateEvent{" + "player=" + player + ", template=" + template + ", location=" + location + '}';
    }
}
