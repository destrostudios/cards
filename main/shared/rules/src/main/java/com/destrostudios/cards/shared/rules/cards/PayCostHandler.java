package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PayCostHandler extends GameEventHandler<PayCostEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(PayCostHandler.class);

    @Override
    public void handle(PayCostEvent event) {
        LOG.info("Paying cost {}", event.cost);

        // TODO: Create own subevents/handlers/however-we-want-it for everything

        if (data.hasComponent(event.cost, Components.Cost.TAP)) {
            data.setComponent(event.card, Components.TAPPED);
        }

        // TODO: Pay mana cost
        int player = data.getComponent(event.card, Components.OWNED_BY);
        Integer whiteManaCost = data.getComponent(event.cost, Components.ManaAmount.WHITE);
        if (whiteManaCost != null) {

        }
    }
}
