package com.destrostudios.cards.shared.rules.abilities;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameContext;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.cards.zones.RemovedFromCreatureZoneEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeactivateDivineShieldOnRemovedFromCreatureZoneHandler extends GameEventHandler<RemovedFromCreatureZoneEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(DeactivateDivineShieldOnRemovedFromCreatureZoneHandler.class);

    @Override
    public void handle(GameContext context, RemovedFromCreatureZoneEvent event) {
        EntityData data = context.getData();
        if (data.hasComponent(event.card, Components.Ability.DIVINE_SHIELD)) {
            LOG.debug("Deactivating divine shield for {}", inspect(data, event.card));
            data.setComponent(event.card, Components.Ability.DIVINE_SHIELD, false);
        }
    }
}
