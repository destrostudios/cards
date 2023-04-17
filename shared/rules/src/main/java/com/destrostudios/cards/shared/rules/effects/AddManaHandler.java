package com.destrostudios.cards.shared.rules.effects;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddManaHandler extends GameEventHandler<AddManaEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(AddManaHandler.class);

    @Override
    public void handle(AddManaEvent event, NetworkRandom random) {
        int currentMana = data.getOptionalComponent(event.player, Components.MANA).orElse(0);
        int newMana = currentMana + event.manaAmount;
        LOG.debug("Adding {} mana to player {} (current mana = {}, new mana = {})", event.manaAmount, inspect(event.player), currentMana, newMana);
        events.fire(new SetManaEvent(event.player, newMana));
    }
}
