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

        if (event.payedMana.isNotEmpty()) {
            int player = data.getComponent(event.card, Components.OWNED_BY);
            events.fire(new PayManaEvent(player, event.payedMana));
        }

        // TODO: Create own subevent + handler? Overkill for now?
        if (data.hasComponent(event.cost, Components.Cost.TAP)) {
            data.setComponent(event.card, Components.TAPPED);
        }
    }
}
