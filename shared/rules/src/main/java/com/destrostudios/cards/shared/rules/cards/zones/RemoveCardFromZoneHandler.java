package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.battle.ConditionsAffectedEvent;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RemoveCardFromZoneHandler extends GameEventHandler<RemoveCardFromZoneEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(RemoveCardFromZoneHandler.class);

    @Override
    public void handle(RemoveCardFromZoneEvent event, NetworkRandom random) {
        LOG.debug("Removing {} from zone {}", inspect(event.card), event.zone.getName());
        int player = data.getComponent(event.card, Components.OWNED_BY);
        int cardZoneIndex = data.getComponent(event.card, event.zone);
        for (int card : data.query(event.zone).list(
            x -> data.hasComponentValue(x, Components.OWNED_BY, player) && data.getComponent(x, event.zone) > cardZoneIndex)
        ) {
            data.setComponent(card, event.zone, data.getComponent(card, event.zone) - 1);
        }
        data.removeComponent(event.card, event.zone);
        if (event.zone == Components.CREATURE_ZONE) {
            data.removeComponent(event.card, Components.BOARD);
            events.fire(new RemoveCardFromBoardEvent(event.card), random);
        }
        events.fire(new ConditionsAffectedEvent(), random);
    }
}
