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

        Integer mana = data.getComponent(event.cost, Components.MANA);
        if (mana != null) {
            int player = data.getComponent(event.card, Components.OWNED_BY);
            events.fire(new PayManaEvent(player, mana));
        }
    }
}
