package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
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

        // TODO: Neutral mana handling (Pass payed mana in event)
        int player = data.getComponent(event.card, Components.OWNED_BY);
        payManaCost(player, event.cost, Components.ManaAmount.NEUTRAL);
        payManaCost(player, event.cost, Components.ManaAmount.WHITE);
        payManaCost(player, event.cost, Components.ManaAmount.RED);
        payManaCost(player, event.cost, Components.ManaAmount.GREEN);
        payManaCost(player, event.cost, Components.ManaAmount.BLUE);
        payManaCost(player, event.cost, Components.ManaAmount.BLACK);

        if (data.hasComponent(event.cost, Components.Cost.TAP)) {
            data.setComponent(event.card, Components.TAPPED);
        }
    }

    private void payManaCost(int player, int costEntity, ComponentDefinition<Integer> manaAmountComponent) {
        Integer manaCost = data.getComponent(costEntity, manaAmountComponent);
        if (manaCost != null) {
            int currentMana = data.getOptionalComponent(player, manaAmountComponent).orElse(0);
            data.setComponent(player, manaAmountComponent, currentMana - manaCost);
        }
    }
}
