package com.destrostudios.cards.shared.rules.game;

import com.destrostudios.cards.shared.entities.collections.IntArrayList;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class SetStartingPlayerHandler extends GameEventHandler<GameStartEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(SetStartingPlayerHandler.class);

    @Override
    public void handle(GameStartEvent event) {
        IntArrayList players = data.entities(Components.NEXT_PLAYER);
        int player = players.get(random.applyAsInt(players.size()));
        data.setComponent(player, Components.ACTIVE_PLAYER);
        LOG.info("starting player is {}.", player);
    }
}
