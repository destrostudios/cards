package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.game.GameStartEvent;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class ShuffleAllLibrariesOnGameStartHandler extends GameEventHandler<GameStartEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(ShuffleLibraryHandler.class);

    @Override
    public void handle(GameStartEvent event) {
        List<Integer> players = data.query(Components.NEXT_PLAYER).list();
        LOG.info("shuffling libraries of players {}", players);
        players.stream().forEachOrdered(player -> events.fireSubEvent(new ShuffleLibraryEvent(player)));
    }

}
