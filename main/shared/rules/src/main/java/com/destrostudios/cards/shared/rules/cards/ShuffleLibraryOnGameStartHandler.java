package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.entities.collections.IntArrayList;
import com.destrostudios.cards.shared.events.EventHandler;
import com.destrostudios.cards.shared.events.EventQueue;
import com.destrostudios.cards.shared.rules.game.StartGameEvent;
import org.slf4j.Logger;

/**
 *
 * @author Philipp
 */
public class ShuffleLibraryOnGameStartHandler implements EventHandler<StartGameEvent> {

    private final EntityData data;
    private final EventQueue events;
    private final Logger log;
    private final int playerKey;

    public ShuffleLibraryOnGameStartHandler(EntityData data, EventQueue events, Logger log, int playerKey) {
        this.data = data;
        this.events = events;
        this.log = log;
        this.playerKey = playerKey;
    }

    @Override
    public void onEvent(StartGameEvent event) {
        IntArrayList players = data.entitiesWithComponent(playerKey);
        log.info("shuffling libraries of players {}", players);
        players.stream().forEachOrdered(player -> events.trigger(new ShuffleLibraryEvent(player)));
    }

}
