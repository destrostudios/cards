package com.destrostudios.cards.shared.rules.battle;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class DeclareAttackHandler extends GameEventHandler<DeclareAttackEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(DeclareAttackHandler.class);

    @Override
    public void handle(DeclareAttackEvent event) {
        LOG.info("{} declared attack to {}", event.source, event.target);
        data.setComponent(event.source, Components.DECLARED_ATTACK, event.target);
    }

}
