package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.events.Event;

/**
 *
 * @author Philipp
 */
public class ShuffleLibraryEvent extends Event {

    public int player;

    public ShuffleLibraryEvent(int player) {
        this.player = player;
    }

    @Override
    public String toString() {
        return "ShuffleLibraryEvent{" + "player=" + player + '}';
    }
}
