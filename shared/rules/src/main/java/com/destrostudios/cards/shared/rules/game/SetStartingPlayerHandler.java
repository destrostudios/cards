package com.destrostudios.cards.shared.rules.game;

import com.destrostudios.cards.shared.entities.IntList;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SetStartingPlayerHandler extends GameEventHandler<GameStartEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(SetStartingPlayerHandler.class);

    @Override
    public void handle(GameStartEvent event) {
        IntList players = data.list(Components.NEXT_PLAYER);
        int player = players.getRandomItem(random::nextInt);
        LOG.debug("Starting player is {}", inspect(player));
        data.setComponent(player, Components.Player.ACTIVE_PLAYER);
    }
}
