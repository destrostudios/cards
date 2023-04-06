package com.destrostudios.cards.shared.rules.battle;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.cards.zones.RemoveCardFromBoardZoneEvent;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RemoveDamageOnRemoveFromBoardHandler extends GameEventHandler<RemoveCardFromBoardZoneEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(RemoveDamageOnRemoveFromBoardHandler.class);

    @Override
    public void handle(RemoveCardFromBoardZoneEvent event, NetworkRandom random) {
        LOG.info("Removing damage from " + inspect(event.card));
        data.removeComponent(event.card, Components.Stats.DAMAGED);
        data.removeComponent(event.card, Components.Stats.BONUS_DAMAGED);
    }
}
