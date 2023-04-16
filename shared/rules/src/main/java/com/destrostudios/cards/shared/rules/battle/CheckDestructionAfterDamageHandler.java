package com.destrostudios.cards.shared.rules.battle;

import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;

public class CheckDestructionAfterDamageHandler extends BaseDestroyOnZeroHealthHandler<DamageEvent> {

    @Override
    public void handle(DamageEvent event, NetworkRandom random) {
        destroyOnZeroHealth(event.target, event, random);
    }
}
