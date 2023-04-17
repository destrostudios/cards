package com.destrostudios.cards.shared.rules.buffs;

import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.util.BuffUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddBuffHandler extends GameEventHandler<AddBuffEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(AddBuffHandler.class);

    @Override
    public void handle(AddBuffEvent event) {
        LOG.debug("Adding buff {} to {}", inspect(event.buff), inspect(event.target));
        BuffUtil.add(data, event.target, event.buff);
    }
}
