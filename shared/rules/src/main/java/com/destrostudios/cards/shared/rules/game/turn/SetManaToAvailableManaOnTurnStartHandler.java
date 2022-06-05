package com.destrostudios.cards.shared.rules.game.turn;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.effects.SetManaEvent;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;

public class SetManaToAvailableManaOnTurnStartHandler extends GameEventHandler<StartTurnEvent> {

    @Override
    public void handle(StartTurnEvent event, NetworkRandom random) {
        int availableMana = data.getOptionalComponent(event.player, Components.AVAILABLE_MANA).orElse(0);
        events.fire(new SetManaEvent(event.player, availableMana), random);
    }
}
