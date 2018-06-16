package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;

public class AddCardToLandZoneHandler extends GameEventHandler<AddCardToLandZoneEvent> {

    @Override
    public void handle(AddCardToLandZoneEvent event) {
        events.fire(new AddCardToBoardZoneEvent(event.card, Components.LAND_ZONE));
    }
}
