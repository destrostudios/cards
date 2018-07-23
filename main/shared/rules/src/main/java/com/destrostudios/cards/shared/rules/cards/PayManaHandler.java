package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;

public class PayManaHandler extends GameEventHandler<PayManaEvent> {

    @Override
    public void handle(PayManaEvent event) {
        payManaCost(event.player, Components.ManaAmount.NEUTRAL, event.mixedManaAmount.getNeutral());
        payManaCost(event.player, Components.ManaAmount.WHITE, event.mixedManaAmount.getWhite());
        payManaCost(event.player, Components.ManaAmount.RED, event.mixedManaAmount.getRed());
        payManaCost(event.player, Components.ManaAmount.GREEN, event.mixedManaAmount.getGreen());
        payManaCost(event.player, Components.ManaAmount.BLUE, event.mixedManaAmount.getBlue());
        payManaCost(event.player, Components.ManaAmount.BLACK, event.mixedManaAmount.getBlack());
    }

    private void payManaCost(int player, ComponentDefinition<Integer> manaAmountComponent, int payedManaAmount) {
        if (payedManaAmount != 0) {
            int currentMana = data.getOptionalComponent(player, manaAmountComponent).orElse(0);
            data.setComponent(player, manaAmountComponent, currentMana - payedManaAmount);
        }
    }
}
