package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;

public class RemoveCardFromLandZoneHandler extends GameEventHandler<RemoveCardFromLandZoneEvent> {

    @Override
    public void handle(RemoveCardFromLandZoneEvent event) {
        events.fireSubEvent(new RemoveCardFromBoardZoneEvent(event.card, Components.LAND_ZONE));
    }
}
