package com.destrostudios.cards.shared.rules.game.phases.main;

import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.cards.DrawCardEvent;
import com.destrostudios.cards.shared.rules.game.phases.main.StartMainPhaseOneEvent;

public class DrawCardOnMainPhaseOneHandler extends GameEventHandler<StartMainPhaseOneEvent> {

    @Override
    public void handle(StartMainPhaseOneEvent event) {
        events.fireSubEvent(new DrawCardEvent(event.player));
    }
}
