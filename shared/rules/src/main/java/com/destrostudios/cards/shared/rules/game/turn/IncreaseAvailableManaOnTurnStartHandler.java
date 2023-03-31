package com.destrostudios.cards.shared.rules.game.turn;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameConstants;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.effects.SetAvailableManaEvent;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;

public class IncreaseAvailableManaOnTurnStartHandler extends GameEventHandler<StartTurnEvent> {

    @Override
    public void handle(StartTurnEvent event, NetworkRandom random) {
        int currentAvailableMana = data.getOptionalComponent(event.player, Components.AVAILABLE_MANA).orElse(0);
        if (currentAvailableMana < GameConstants.MAXIMUM_AVAILABLE_MANA) {
            events.fire(new SetAvailableManaEvent(event.player, currentAvailableMana + 1), random);
        }
    }
}
