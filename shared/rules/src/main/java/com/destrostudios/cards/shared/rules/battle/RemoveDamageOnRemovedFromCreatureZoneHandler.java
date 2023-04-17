package com.destrostudios.cards.shared.rules.battle;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.cards.zones.RemovedFromCreatureZoneEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RemoveDamageOnRemovedFromCreatureZoneHandler extends GameEventHandler<RemovedFromCreatureZoneEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(RemoveDamageOnRemovedFromCreatureZoneHandler.class);

    @Override
    public void handle(RemovedFromCreatureZoneEvent event) {
        LOG.debug("Removing damage from {}", inspect(event.card));
        data.removeComponent(event.card, Components.Stats.DAMAGED);
        data.removeComponent(event.card, Components.Stats.BONUS_DAMAGED);
    }
}
