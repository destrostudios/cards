package com.destrostudios.cards.shared.rules.game;

import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.cards.DrawCardEvent;

public class DrawCardOnStartTurnHandler extends GameEventHandler<StartTurnEvent> {

    @Override
    public void handle(StartTurnEvent event) {
        events.fireSubEvent(new DrawCardEvent(event.player));
    }
}
