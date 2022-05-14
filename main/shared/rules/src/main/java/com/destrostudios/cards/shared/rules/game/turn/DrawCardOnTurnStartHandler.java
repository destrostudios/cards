package com.destrostudios.cards.shared.rules.game.turn;

import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.cards.DrawCardEvent;

public class DrawCardOnTurnStartHandler extends GameEventHandler<StartTurnEvent> {

    @Override
    public void handle(StartTurnEvent event) {
        events.fire(new DrawCardEvent(event.player));
    }
}
