package com.destrostudios.cards.shared.rules.buffs;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.cards.zones.RemovedFromCreatureZoneEvent;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RemoveBuffsOnRemovedFromCreatureZoneHandler extends GameEventHandler<RemovedFromCreatureZoneEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(RemoveBuffsOnRemovedFromCreatureZoneHandler.class);

    @Override
    public void handle(RemovedFromCreatureZoneEvent event, NetworkRandom random) {
        int[] buffs = data.getComponent(event.card, Components.BUFFS);
        if (buffs != null) {
            LOG.debug("Removing all {} buffs from {}", buffs.length, inspect(event.card));
            for (int buff : buffs) {
                events.fire(new RemoveBuffEvent(event.card, buff), random);
            }
        }
    }
}
