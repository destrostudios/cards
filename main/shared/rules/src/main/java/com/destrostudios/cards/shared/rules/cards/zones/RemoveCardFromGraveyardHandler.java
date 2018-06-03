package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;

public class RemoveCardFromGraveyardHandler extends GameEventHandler<RemoveCardFromGraveyardEvent> {

    @Override
    public void handle(RemoveCardFromGraveyardEvent event) {
        events.fireSubEvent(new RemoveCardFromZoneEvent(event.card, Components.GRAVEYARD));
    }
}
