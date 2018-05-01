package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.entities.collections.IntArrayList;
import com.destrostudios.cards.shared.events.EventHandler;
import com.destrostudios.cards.shared.events.EventQueue;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.game.GameStartEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class ShuffleAllLibrariesOnGameStartHandler implements EventHandler<GameStartEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(ShuffleLibraryHandler.class);

    private final EntityData data;
    private final EventQueue events;

    public ShuffleAllLibrariesOnGameStartHandler(EntityData data, EventQueue events) {
        this.data = data;
        this.events = events;
    }

    @Override
    public void onEvent(GameStartEvent event) {
        IntArrayList players = data.entities(Components.NEXT_PLAYER);
        LOG.info("shuffling libraries of players {}", players);
        players.stream().forEachOrdered(player -> events.response(new ShuffleLibraryEvent(player)));
    }

}
