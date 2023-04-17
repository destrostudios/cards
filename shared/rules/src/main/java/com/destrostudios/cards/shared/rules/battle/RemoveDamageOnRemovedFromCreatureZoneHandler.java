package com.destrostudios.cards.shared.rules.battle;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameContext;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.cards.zones.RemovedFromCreatureZoneEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RemoveDamageOnRemovedFromCreatureZoneHandler extends GameEventHandler<RemovedFromCreatureZoneEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(RemoveDamageOnRemovedFromCreatureZoneHandler.class);

    @Override
    public void handle(GameContext context, RemovedFromCreatureZoneEvent event) {
        EntityData data = context.getData();
        LOG.debug("Removing damage from {}", inspect(data, event.card));
        data.removeComponent(event.card, Components.Stats.DAMAGED);
        data.removeComponent(event.card, Components.Stats.BONUS_DAMAGED);
    }
}
