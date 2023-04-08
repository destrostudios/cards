package com.destrostudios.cards.shared.rules.battle;

import com.destrostudios.cards.shared.entities.EntityData;

import java.util.Collections;
import java.util.List;

public class CheckDestructionAfterDamageHandler extends DestroyOnZeroHealthHandler<DamageEvent> {

    @Override
    protected List<Integer> getAffectedTargets(EntityData data, DamageEvent event) {
        return Collections.singletonList(event.target);
    }
}
