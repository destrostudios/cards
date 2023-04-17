package com.destrostudios.cards.shared.rules.buffs;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.rules.GameContext;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.util.BuffUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddBuffHandler extends GameEventHandler<AddBuffEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(AddBuffHandler.class);

    @Override
    public void handle(GameContext context, AddBuffEvent event) {
        EntityData data = context.getData();
        LOG.debug("Adding buff {} to {}", inspect(data, event.buff), inspect(data, event.target));
        BuffUtil.add(data, event.target, event.buff);
    }
}
