package com.destrostudios.cards.shared.rules.buffs;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.cards.zones.RemoveCardFromBoardEvent;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RemoveBuffsOnRemoveFromBoardHandler extends GameEventHandler<RemoveCardFromBoardEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(RemoveBuffsOnRemoveFromBoardHandler.class);

    @Override
    public void handle(RemoveCardFromBoardEvent event, NetworkRandom random) {
        int[] buffs = data.getComponent(event.card, Components.BUFFS);
        if (buffs != null) {
            LOG.info("Removing all " + buffs.length + " buffs from " + inspect(event.card));
            for (int buff : buffs) {
                events.fire(new RemoveBuffEvent(event.card, buff), random);
            }
        }
    }
}
