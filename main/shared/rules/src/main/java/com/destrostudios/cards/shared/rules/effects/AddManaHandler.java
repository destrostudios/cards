package com.destrostudios.cards.shared.rules.effects;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;

public class AddManaHandler extends GameEventHandler<AddManaEvent> {

    @Override
    public void handle(AddManaEvent event) {
        addMana(event.player, event.manaEntity, Components.ManaAmount.NEUTRAL);
        addMana(event.player, event.manaEntity, Components.ManaAmount.WHITE);
        addMana(event.player, event.manaEntity, Components.ManaAmount.RED);
        addMana(event.player, event.manaEntity, Components.ManaAmount.GREEN);
        addMana(event.player, event.manaEntity, Components.ManaAmount.BLUE);
        addMana(event.player, event.manaEntity, Components.ManaAmount.BLACK);
    }

    private void addMana(int player, int manaEntity, ComponentDefinition<Integer> manaAmountComponent) {
        Integer addedMana = data.getComponent(manaEntity, manaAmountComponent);
        if (addedMana != null) {
            int currentMana = data.getOptionalComponent(player, manaAmountComponent).orElse(0);
            data.setComponent(player, manaAmountComponent, currentMana + addedMana);
        }
    }
}
