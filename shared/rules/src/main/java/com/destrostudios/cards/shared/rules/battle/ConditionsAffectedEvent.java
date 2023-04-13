package com.destrostudios.cards.shared.rules.battle;

import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.EventType;

public class ConditionsAffectedEvent extends Event {

    public ConditionsAffectedEvent() {
        super(EventType.CONDITIONS_AFFECTED);
    }

    @Override
    public String toString() {
        return "ConditionsAffectedEvent";
    }
}
