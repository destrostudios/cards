package com.destrostudios.cards.shared.rules.abilities;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.cards.zones.RemovedFromCreatureZoneEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeactivateDivineShieldOnRemovedFromCreatureZoneHandler extends GameEventHandler<RemovedFromCreatureZoneEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(DeactivateDivineShieldOnRemovedFromCreatureZoneHandler.class);

    @Override
    public void handle(RemovedFromCreatureZoneEvent event) {
        if (data.hasComponent(event.card, Components.Ability.DIVINE_SHIELD)) {
            LOG.debug("Deactivating divine shield for {}", inspect(event.card));
            data.setComponent(event.card, Components.Ability.DIVINE_SHIELD, false);
        }
    }
}
