package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.entities.IntList;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.game.GameStartEvent;

import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShuffleAllLibrariesOnGameStartHandler extends GameEventHandler<GameStartEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(ShuffleAllLibrariesOnGameStartHandler.class);

    @Override
    public void handle(GameStartEvent event, NetworkRandom random) {
        IntList players = data.list(Components.NEXT_PLAYER);
        LOG.debug("Shuffling libraries of players {}", inspect(players));
        for (int player : players) {
            events.fire(new ShuffleLibraryEvent(player), random);
        }
    }
}
