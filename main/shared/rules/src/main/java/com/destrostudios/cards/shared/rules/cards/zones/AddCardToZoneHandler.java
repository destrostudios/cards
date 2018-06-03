package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;

public class AddCardToZoneHandler extends GameEventHandler<AddCardToZoneEvent> {

    @Override
    public void handle(AddCardToZoneEvent event) {
        int player = data.getComponent(event.card, Components.OWNED_BY);
        data.setComponent(event.card, event.zone, data.query(event.zone).count(hasComponentValue(Components.OWNED_BY, player)));
    }
}
