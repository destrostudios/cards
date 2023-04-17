package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameContext;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PayManaHandler extends GameEventHandler<PayManaEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(PayManaHandler.class);

    @Override
    public void handle(GameContext context, PayManaEvent event) {
        EntityData data = context.getData();
        if (event.manaAmount != 0) {
            int currentMana = data.getOptionalComponent(event.player, Components.MANA).orElse(0);
            int newMana = currentMana - event.manaAmount;
            LOG.debug("Player {} is paying {} mana (current mana = {}, new mana = {})", inspect(data, event.player), event.manaAmount, currentMana, newMana);
            data.setComponent(event.player, Components.MANA, newMana);
        }
    }
}
