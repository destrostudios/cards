package com.destrostudios.cards.shared.rules.conditions;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.entities.IntList;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.battle.ConditionsAffectedEvent;
import com.destrostudios.cards.shared.rules.battle.BaseDestroyOnZeroHealthHandler;

public class CheckDestructionAfterConditionsAffectedHandler extends BaseDestroyOnZeroHealthHandler<ConditionsAffectedEvent> {

    @Override
    protected IntList getAffectedTargets(EntityData data, ConditionsAffectedEvent event) {
        return data.queryAll(Components.CREATURE_ZONE, Components.AURAS).list();
    }
}
