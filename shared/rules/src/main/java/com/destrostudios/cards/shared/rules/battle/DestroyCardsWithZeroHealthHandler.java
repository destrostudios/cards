package com.destrostudios.cards.shared.rules.battle;

import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.util.StatsUtil;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;

public class DestroyCardsWithZeroHealthHandler extends GameEventHandler<DamageEvent> {

    @Override
    public void handle(DamageEvent event, NetworkRandom random) {
        if (StatsUtil.getEffectiveHealth(data, event.target) <= 0) {
            events.fire(new DestructionEvent(event.target), random);
        }
    }
}
