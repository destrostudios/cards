package com.destrostudios.cards.shared.rules.game.phases.main;

import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.cards.DrawCardEvent;

public class DrawCardOnMainPhaseOneHandler extends GameEventHandler<StartMainPhaseOneEvent> {

    @Override
    public void handle(StartMainPhaseOneEvent event) {
        events.fire(new DrawCardEvent(event.player));
    }
}
