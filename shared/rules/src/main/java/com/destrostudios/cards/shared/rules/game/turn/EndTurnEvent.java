package com.destrostudios.cards.shared.rules.game.turn;

import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.EventType;

import java.util.Objects;

public class EndTurnEvent extends Event {

    public int player;

    // Used by serializer
    private EndTurnEvent() {
        this(0);
    }

    public EndTurnEvent(int player) {
        super(EventType.END_TURN);
        this.player = player;
    }

    @Override
    public String toString() {
        return EndTurnEvent.class.getSimpleName() + "{player=" + player + '}';
    }

    // Used by game-tools bot

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EndTurnEvent that = (EndTurnEvent) o;
        return player == that.player;
    }

    @Override
    public int hashCode() {
        return Objects.hash(player);
    }
}
