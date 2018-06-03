package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;

public class AddCardToBoardZoneHandler extends GameEventHandler<AddCardToBoardZoneEvent> {

    @Override
    public void handle(AddCardToBoardZoneEvent event) {
        events.fireSubEvent(new AddCardToZoneEvent(event.card, event.zone));
        data.setComponent(event.card, Components.BOARD);
    }
}
