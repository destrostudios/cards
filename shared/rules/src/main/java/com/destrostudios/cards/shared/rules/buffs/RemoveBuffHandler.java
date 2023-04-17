package com.destrostudios.cards.shared.rules.buffs;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameContext;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.util.ArrayUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RemoveBuffHandler extends GameEventHandler<RemoveBuffEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(RemoveBuffHandler.class);

    @Override
    public void handle(GameContext context, RemoveBuffEvent event) {
        EntityData data = context.getData();
        LOG.debug("Removing buff {} from {}", inspect(data, event.buff), inspect(data, event.target));
        ArrayUtil.remove(data, event.target, Components.BUFFS, event.buff);
        // TODO: Remove buff entity if it was an evaluated copy? Overkill for now?
    }
}
