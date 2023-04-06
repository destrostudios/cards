package com.destrostudios.cards.shared.rules.abilities;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.cards.zones.AddCardToBoardEvent;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActivateDivineShieldOnAddToBoardHandler extends GameEventHandler<AddCardToBoardEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(ActivateDivineShieldOnAddToBoardHandler.class);

    @Override
    public void handle(AddCardToBoardEvent event, NetworkRandom random) {
        if (data.hasComponent(event.card, Components.Ability.DIVINE_SHIELD)) {
            LOG.info("Activating divine shield for " + inspect(event.card));
            data.setComponent(event.card, Components.Ability.DIVINE_SHIELD, true);
        }
    }
}
