package com.destrostudios.cards.shared.rules.battle;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.buffs.RemoveBuffEvent;
import com.destrostudios.cards.shared.rules.cards.zones.RemoveCardFromBoardZoneEvent;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;

public class RemoveBuffsOnRemoveFromBoardHandler extends GameEventHandler<RemoveCardFromBoardZoneEvent> {

    @Override
    public void handle(RemoveCardFromBoardZoneEvent event, NetworkRandom random) {
        int[] buffs = data.getComponent(event.card, Components.BUFFS);
        if (buffs != null) {
            for (int buff : buffs) {
                events.fire(new RemoveBuffEvent(event.card, buff), random);
            }
        }
    }
}
