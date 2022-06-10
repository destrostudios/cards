package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.battle.ConditionsAffectedEvent;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;

public class RemoveCardFromZoneHandler extends GameEventHandler<RemoveCardFromZoneEvent> {

    @Override
    public void handle(RemoveCardFromZoneEvent event, NetworkRandom random) {
        int player = data.getComponent(event.card, Components.OWNED_BY);
        int cardZoneIndex = data.getComponent(event.card, event.zone);
        for (int libraryCard : data.query(event.zone).list(
                x -> data.hasComponentValue(x, Components.OWNED_BY, player)
                        && data.getComponent(x, event.zone) > cardZoneIndex)) {
            data.setComponent(libraryCard, event.zone, data.getComponent(libraryCard, event.zone) - 1);
        }
        data.removeComponent(event.card, event.zone);
        events.fire(new ConditionsAffectedEvent(), random);
    }
}
