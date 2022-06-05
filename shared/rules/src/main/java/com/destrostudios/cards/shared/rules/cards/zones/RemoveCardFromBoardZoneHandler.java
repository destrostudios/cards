package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;

public class RemoveCardFromBoardZoneHandler extends GameEventHandler<RemoveCardFromBoardZoneEvent> {

    @Override
    public void handle(RemoveCardFromBoardZoneEvent event, NetworkRandom random) {
        events.fire(new RemoveCardFromZoneEvent(event.card, event.zone), random);
        data.removeComponent(event.card, Components.BOARD);
    }
}
