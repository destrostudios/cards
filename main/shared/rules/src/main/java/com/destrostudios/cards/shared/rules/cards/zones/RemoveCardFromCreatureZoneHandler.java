package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;

public class RemoveCardFromCreatureZoneHandler extends GameEventHandler<RemoveCardFromCreatureZoneEvent> {

    @Override
    public void handle(RemoveCardFromCreatureZoneEvent event) {
        events.fireSubEvent(new RemoveCardFromBoardZoneEvent(event.card, Components.CREATURE_ZONE));
    }
}
