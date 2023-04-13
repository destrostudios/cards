package com.destrostudios.cards.shared.rules.battle;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.entities.IntList;

public class CheckDestructionAfterDamageHandler extends BaseDestroyOnZeroHealthHandler<DamageEvent> {

    @Override
    protected IntList getAffectedTargets(EntityData data, DamageEvent event) {
        return IntList.singletonList(event.target);
    }
}
