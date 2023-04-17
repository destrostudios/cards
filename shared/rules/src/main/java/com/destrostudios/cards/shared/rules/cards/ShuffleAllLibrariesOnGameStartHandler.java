package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.entities.IntList;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameContext;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.game.GameStartEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShuffleAllLibrariesOnGameStartHandler extends GameEventHandler<GameStartEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(ShuffleAllLibrariesOnGameStartHandler.class);

    @Override
    public void handle(GameContext context, GameStartEvent event) {
        EntityData data = context.getData();
        IntList players = data.list(Components.NEXT_PLAYER);
        LOG.debug("Shuffling libraries of players {}", inspect(data, players));
        for (int player : players) {
            context.getEvents().fire(new ShuffleLibraryEvent(player));
        }
    }
}
