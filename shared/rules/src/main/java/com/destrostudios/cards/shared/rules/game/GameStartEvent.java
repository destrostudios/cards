package com.destrostudios.cards.shared.rules.game;

import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.EventType;

public class GameStartEvent extends Event {

    public GameStartEvent() {
        super(EventType.GAME_START);
    }

    @Override
    public String toString() {
        return "GameStartEvent{" + '}';
    }
}
