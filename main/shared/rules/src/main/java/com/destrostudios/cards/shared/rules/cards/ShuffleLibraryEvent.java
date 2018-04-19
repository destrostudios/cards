package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.events.TriggeredEvent;

/**
 *
 * @author Philipp
 */
public class ShuffleLibraryEvent extends TriggeredEvent {

    public int player;

    public ShuffleLibraryEvent(int player) {
        this.player = player;
    }

    @Override
    public String toString() {
        return "ShuffleLibraryEvent{" + "player=" + player + '}';
    }
}
