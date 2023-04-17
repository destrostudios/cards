package com.destrostudios.cards.shared.rules.battle;

import com.destrostudios.cards.shared.rules.GameContext;

public class CheckDestructionAfterDamageHandler extends BaseDestroyOnZeroHealthHandler<DamageEvent> {

    @Override
    public void handle(GameContext context, DamageEvent event) {
        destroyOnZeroHealth(context, event.target, event);
    }
}
