package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;

public class RemoveCardFromBoardZoneHandler extends GameEventHandler<RemoveCardFromBoardZoneEvent> {

    @Override
    public void handle(RemoveCardFromBoardZoneEvent event) {
        events.fire(new RemoveCardFromZoneEvent(event.card, event.zone));
        data.removeComponent(event.card, Components.BOARD);
    }
}
