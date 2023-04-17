package com.destrostudios.cards.shared.rules.battle;

public class CheckDestructionAfterDamageHandler extends BaseDestroyOnZeroHealthHandler<DamageEvent> {

    @Override
    public void handle(DamageEvent event) {
        destroyOnZeroHealth(event.target, event);
    }
}
