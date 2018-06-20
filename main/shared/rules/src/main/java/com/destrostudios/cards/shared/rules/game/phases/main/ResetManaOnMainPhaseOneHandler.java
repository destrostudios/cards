package com.destrostudios.cards.shared.rules.game.phases.main;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;

public class ResetManaOnMainPhaseOneHandler extends GameEventHandler<StartMainPhaseOneEvent> {

    @Override
    public void handle(StartMainPhaseOneEvent event) {
        data.removeComponent(event.player, Components.ManaAmount.NEUTRAL);
        data.removeComponent(event.player, Components.ManaAmount.WHITE);
        data.removeComponent(event.player, Components.ManaAmount.RED);
        data.removeComponent(event.player, Components.ManaAmount.GREEN);
        data.removeComponent(event.player, Components.ManaAmount.BLUE);
        data.removeComponent(event.player, Components.ManaAmount.BLACK);
    }
}
