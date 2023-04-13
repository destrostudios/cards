package com.destrostudios.cards.shared.rules.battle;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.cards.zones.RemoveCardFromCreatureZoneEvent;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RemoveDamageOnRemoveFromCreatureZoneHandler extends GameEventHandler<RemoveCardFromCreatureZoneEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(RemoveDamageOnRemoveFromCreatureZoneHandler.class);

    @Override
    public void handle(RemoveCardFromCreatureZoneEvent event, NetworkRandom random) {
        LOG.debug("Removing damage from {}", inspect(event.card));
        data.removeComponent(event.card, Components.Stats.DAMAGED);
        data.removeComponent(event.card, Components.Stats.BONUS_DAMAGED);
    }
}
