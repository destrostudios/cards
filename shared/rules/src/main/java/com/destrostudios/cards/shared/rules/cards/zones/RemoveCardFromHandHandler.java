package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;

public class RemoveCardFromHandHandler extends GameEventHandler<RemoveCardFromHandEvent> {

    @Override
    public void handle(RemoveCardFromHandEvent event, NetworkRandom random) {
        events.fire(new RemoveCardFromZoneEvent(event.card, Components.HAND), random);
    }
}
