package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.battle.ConditionsAffectedEvent;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;

public class AddCardToZoneHandler extends GameEventHandler<AddCardToZoneEvent> {

    @Override
    public void handle(AddCardToZoneEvent event, NetworkRandom random) {
        int player = data.getComponent(event.card, Components.OWNED_BY);
        data.setComponent(event.card, event.zone, data.query(event.zone).count(hasComponentValue(Components.OWNED_BY, player)));
        events.fire(new ConditionsAffectedEvent(), random);
    }
}
