package com.destrostudios.cards.shared.rules.game;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.entities.IntList;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameContext;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SetStartingPlayerHandler extends GameEventHandler<GameStartEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(SetStartingPlayerHandler.class);

    @Override
    public void handle(GameContext context, GameStartEvent event) {
        EntityData data = context.getData();
        IntList players = data.list(Components.NEXT_PLAYER);
        int player = players.getRandomItem(max -> context.getRandom().nextInt(max));
        LOG.debug("Starting player is {}", inspect(data, player));
        data.setComponent(player, Components.Player.ACTIVE_PLAYER);
    }
}
