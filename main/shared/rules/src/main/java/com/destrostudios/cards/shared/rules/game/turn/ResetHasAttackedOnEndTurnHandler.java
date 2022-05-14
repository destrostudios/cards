package com.destrostudios.cards.shared.rules.game.turn;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;

public class ResetHasAttackedOnEndTurnHandler extends GameEventHandler<EndTurnEvent> {

    @Override
    public void handle(EndTurnEvent event) {
        for (int entity : data.query(Components.HAS_ATTACKED).list()) {
            data.removeComponent(entity, Components.HAS_ATTACKED);
        }
    }
}
