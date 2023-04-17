package com.destrostudios.cards.shared.rules.abilities;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameContext;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.cards.zones.MoveToCreatureZoneEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActivateDivineShieldOnMoveToCreatureZoneHandler extends GameEventHandler<MoveToCreatureZoneEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(ActivateDivineShieldOnMoveToCreatureZoneHandler.class);

    @Override
    public void handle(GameContext context, MoveToCreatureZoneEvent event) {
        EntityData data = context.getData();
        if (data.hasComponent(event.card, Components.Ability.DIVINE_SHIELD)) {
            LOG.debug("Activating divine shield for {}", inspect(data, event.card));
            data.setComponent(event.card, Components.Ability.DIVINE_SHIELD, true);
        }
    }
}
