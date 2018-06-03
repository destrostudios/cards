package com.destrostudios.cards.shared.rules.battle;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class DeclareBlockHandler extends GameEventHandler<DeclareBlockEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(DeclareBlockHandler.class);

    @Override
    public void handle(DeclareBlockEvent event) {
        LOG.info("{} declared block to {}", event.source, event.target);
        data.setComponent(event.source, Components.DECLARED_BLOCK, event.target);
    }

}
