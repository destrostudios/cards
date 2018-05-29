package com.destrostudios.cards.shared.rules.game.phases.attack;

import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.cards.DrawCardEvent;
import com.destrostudios.cards.shared.rules.game.phases.attack.StartAttackPhaseEvent;

public class DrawCardOnStartAttackPhaseHandler extends GameEventHandler<StartAttackPhaseEvent> {

    @Override
    public void handle(StartAttackPhaseEvent event) {
        events.fireSubEvent(new DrawCardEvent(event.player));
    }
}
