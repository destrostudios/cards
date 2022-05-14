package com.destrostudios.cards.shared.rules.game.turn;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.effects.SetAvailableManaEvent;

public class IncreaseAvailableManaOnTurnStartHandler extends GameEventHandler<StartTurnEvent> {

    @Override
    public void handle(StartTurnEvent event) {
        int currentAvailableMana = data.getOptionalComponent(event.player, Components.AVAILABLE_MANA).orElse(0);
        if (currentAvailableMana < 10) {
            events.fire(new SetAvailableManaEvent(event.player, currentAvailableMana + 1));
        }
    }
}
