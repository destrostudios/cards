package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.EventType;

/**
 *
 * @author Philipp
 */
public class ShuffleLibraryEvent extends Event {

    public final int player;

    public ShuffleLibraryEvent(int player) {
        super(EventType.SHUFFLE_LIBRARY);
        this.player = player;
    }

    @Override
    public String toString() {
        return "ShuffleLibraryEvent{" + "player=" + player + '}';
    }
}
