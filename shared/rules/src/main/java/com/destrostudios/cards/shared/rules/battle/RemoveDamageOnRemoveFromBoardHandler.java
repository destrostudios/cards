package com.destrostudios.cards.shared.rules.battle;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.cards.zones.RemoveCardFromBoardZoneEvent;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;

public class RemoveDamageOnRemoveFromBoardHandler extends GameEventHandler<RemoveCardFromBoardZoneEvent> {

    @Override
    public void handle(RemoveCardFromBoardZoneEvent event, NetworkRandom random) {
        data.removeComponent(event.card, Components.Stats.DAMAGED);
        data.removeComponent(event.card, Components.Stats.BONUS_DAMAGED);
    }
}
