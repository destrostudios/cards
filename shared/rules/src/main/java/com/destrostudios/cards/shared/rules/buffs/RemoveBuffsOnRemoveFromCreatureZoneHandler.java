package com.destrostudios.cards.shared.rules.buffs;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.cards.zones.RemoveCardFromCreatureZoneEvent;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RemoveBuffsOnRemoveFromCreatureZoneHandler extends GameEventHandler<RemoveCardFromCreatureZoneEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(RemoveBuffsOnRemoveFromCreatureZoneHandler.class);

    @Override
    public void handle(RemoveCardFromCreatureZoneEvent event, NetworkRandom random) {
        int[] buffs = data.getComponent(event.card, Components.BUFFS);
        if (buffs != null) {
            LOG.debug("Removing all {} buffs from {}", buffs.length, inspect(event.card));
            for (int buff : buffs) {
                events.fire(new RemoveBuffEvent(event.card, buff), random);
            }
        }
    }
}
