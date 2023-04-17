package com.destrostudios.cards.shared.rules.effects;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameContext;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SetAvailableManaHandler extends GameEventHandler<SetAvailableManaEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(SetAvailableManaHandler.class);

    @Override
    public void handle(GameContext context, SetAvailableManaEvent event) {
        EntityData data = context.getData();
        LOG.debug("Set available mana of player {} to {}", inspect(data, event.player), event.manaAmount);
        data.setComponent(event.player, Components.AVAILABLE_MANA, event.manaAmount);
    }
}
