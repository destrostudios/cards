package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;

public class AddCardToGraveyardHandler extends GameEventHandler<AddCardToGraveyardEvent> {

    @Override
    public void handle(AddCardToGraveyardEvent event) {
        events.fireSubEvent(new AddCardToZoneEvent(event.card, Components.GRAVEYARD));
    }
}
