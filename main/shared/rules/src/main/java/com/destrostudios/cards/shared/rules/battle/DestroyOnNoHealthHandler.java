package com.destrostudios.cards.shared.rules.battle;

import com.destrostudios.cards.shared.rules.GameEventHandler;

public class DestroyOnNoHealthHandler extends GameEventHandler<SetHealthEvent> {

    @Override
    public void handle(SetHealthEvent event) {
        if (event.health <= 0) {
            events.fire(new DestructionEvent(event.target));
        }
    }
}
