package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;

public class RemoveFromOtherZonesOnAddHandler extends GameEventHandler<AddCardToZoneEvent> {

    @Override
    public void handle(AddCardToZoneEvent event, NetworkRandom random) {
        if ((event.zone != Components.LIBRARY) && data.hasComponent(event.card, Components.LIBRARY)) {
            events.fire(new RemoveCardFromLibraryEvent(event.card), random);
        } else if ((event.zone != Components.HAND) && data.hasComponent(event.card, Components.HAND)) {
            events.fire(new RemoveCardFromHandEvent(event.card), random);
        } else if ((event.zone != Components.CREATURE_ZONE) && data.hasComponent(event.card, Components.CREATURE_ZONE)) {
            events.fire(new RemoveCardFromCreatureZoneEvent(event.card), random);
        } else if ((event.zone != Components.GRAVEYARD) && data.hasComponent(event.card, Components.GRAVEYARD)) {
            events.fire(new RemoveCardFromGraveyardEvent(event.card), random);
        }
    }
}
