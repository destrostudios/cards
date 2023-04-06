package com.destrostudios.cards.shared.rules.abilities;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.cards.zones.RemoveCardFromBoardZoneEvent;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeactivateDivineShieldOnRemoveFromBoardHandler extends GameEventHandler<RemoveCardFromBoardZoneEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(DeactivateDivineShieldOnRemoveFromBoardHandler.class);

    @Override
    public void handle(RemoveCardFromBoardZoneEvent event, NetworkRandom random) {
        if (data.hasComponent(event.card, Components.Ability.DIVINE_SHIELD)) {
            LOG.info("Deactivating divine shield for " + inspect(event.card));
            data.setComponent(event.card, Components.Ability.DIVINE_SHIELD, false);
        }
    }
}
