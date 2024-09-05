package com.destrostudios.cards.shared.rules.buffs;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.entities.IntList;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameContext;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.cards.zones.RemovedFromCreatureZoneEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RemoveBuffsOnRemovedFromCreatureZoneHandler extends GameEventHandler<RemovedFromCreatureZoneEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(RemoveBuffsOnRemovedFromCreatureZoneHandler.class);

    @Override
    public void handle(GameContext context, RemovedFromCreatureZoneEvent event) {
        EntityData data = context.getData();
        IntList buffs = data.getComponent(event.card, Components.BUFFS);
        if (buffs != null) {
            LOG.debug("Removing all {} buffs from {}", buffs.size(), inspect(data, event.card));
            for (int buff : buffs) {
                context.getEvents().fire(new RemoveBuffEvent(event.card, buff));
            }
        }
    }
}
