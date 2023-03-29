package com.destrostudios.cards.shared.events;

import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class PendingEventHandler {

    @Getter
    private Event event;
    private EventHandler eventHandler;
    private NetworkRandom random;

    public void handleEvent() {
        eventHandler.onEvent(event, random);
    }
}
