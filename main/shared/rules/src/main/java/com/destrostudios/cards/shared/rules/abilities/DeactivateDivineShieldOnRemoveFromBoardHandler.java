package com.destrostudios.cards.shared.rules.abilities;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.cards.zones.RemoveCardFromBoardZoneEvent;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;

public class DeactivateDivineShieldOnRemoveFromBoardHandler extends GameEventHandler<RemoveCardFromBoardZoneEvent> {

    @Override
    public void handle(RemoveCardFromBoardZoneEvent event, NetworkRandom random) {
        if (data.hasComponent(event.card, Components.Ability.DIVINE_SHIELD)) {
            data.setComponent(event.card, Components.Ability.DIVINE_SHIELD, false);
        }
    }
}