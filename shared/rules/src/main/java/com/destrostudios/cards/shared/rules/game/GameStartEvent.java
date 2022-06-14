package com.destrostudios.cards.shared.rules.game;

import com.destrostudios.cards.shared.events.Event;

public class GameStartEvent extends Event {

    // Empty args constructor used by serializer
    // Equals & hashCode used by game-tools bot

    @Override
    public String toString() {
        return "StartGameEvent{" + '}';
    }
}
