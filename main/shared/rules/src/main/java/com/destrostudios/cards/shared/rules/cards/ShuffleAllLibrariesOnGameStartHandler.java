package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.entities.collections.IntArrayList;
import com.destrostudios.cards.shared.events.EventQueue;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.game.GameStartEvent;
import java.util.function.IntUnaryOperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class ShuffleAllLibrariesOnGameStartHandler implements GameEventHandler<GameStartEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(ShuffleLibraryHandler.class);

    @Override
    public void handle(EntityData data, EventQueue events, IntUnaryOperator random, GameStartEvent event) {
        IntArrayList players = data.entities(Components.NEXT_PLAYER);
        LOG.info("shuffling libraries of players {}", players);
        players.stream().forEachOrdered(player -> events.fireSubEvent(new ShuffleLibraryEvent(player)));
    }

}
