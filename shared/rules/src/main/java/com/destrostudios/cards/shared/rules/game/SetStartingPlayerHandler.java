package com.destrostudios.cards.shared.rules.game;

import com.destrostudios.cards.shared.entities.IntList;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SetStartingPlayerHandler extends GameEventHandler<GameStartEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(SetStartingPlayerHandler.class);

    @Override
    public void handle(GameStartEvent event, NetworkRandom random) {
        IntList players = data.list(Components.NEXT_PLAYER);
        int player = players.get(random.nextInt(players.size()));
        LOG.debug("Starting player is {}", inspect(player));
        data.setComponent(player, Components.Player.ACTIVE_PLAYER);
    }
}
