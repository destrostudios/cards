package com.destrostudios.cards.shared.rules.abilities;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.cards.zones.RemoveCardFromCreatureZoneEvent;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeactivateDivineShieldOnRemoveFromCreatureZoneHandler extends GameEventHandler<RemoveCardFromCreatureZoneEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(DeactivateDivineShieldOnRemoveFromCreatureZoneHandler.class);

    @Override
    public void handle(RemoveCardFromCreatureZoneEvent event, NetworkRandom random) {
        if (data.hasComponent(event.card, Components.Ability.DIVINE_SHIELD)) {
            LOG.debug("Deactivating divine shield for {}", inspect(event.card));
            data.setComponent(event.card, Components.Ability.DIVINE_SHIELD, false);
        }
    }
}
