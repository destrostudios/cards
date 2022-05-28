package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;

public class AddCardToBoardZoneHandler extends GameEventHandler<AddCardToBoardZoneEvent> {

    @Override
    public void handle(AddCardToBoardZoneEvent event, NetworkRandom random) {
        events.fire(new AddCardToZoneEvent(event.card, event.zone), random);
        data.setComponent(event.card, Components.BOARD);
    }
}
