package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.entities.collections.IntArrayList;
import com.destrostudios.cards.shared.events.EventHandler;
import com.destrostudios.cards.shared.events.EventQueue;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.game.StartGameEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class ShuffleLibraryOnGameStartHandler implements EventHandler<StartGameEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(ShuffleLibraryEventHandler.class);

    private final EntityData data;
    private final EventQueue events;

    public ShuffleLibraryOnGameStartHandler(EntityData data, EventQueue events) {
        this.data = data;
        this.events = events;
    }

    @Override
    public void onEvent(StartGameEvent event) {
        IntArrayList players = data.entities(Components.NEXT_PLAYER);
        LOG.info("shuffling libraries of players {}", players);
        players.stream().forEachOrdered(player -> events.trigger(new ShuffleLibraryEvent(player)));
    }

}
