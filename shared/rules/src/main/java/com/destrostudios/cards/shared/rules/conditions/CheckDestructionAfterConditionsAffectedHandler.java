package com.destrostudios.cards.shared.rules.conditions;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.entities.IntList;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.battle.ConditionsAffectedEvent;
import com.destrostudios.cards.shared.rules.battle.DestroyOnZeroHealthHandler;

public class CheckDestructionAfterConditionsAffectedHandler extends DestroyOnZeroHealthHandler<ConditionsAffectedEvent> {

    @Override
    protected IntList getAffectedTargets(EntityData data, ConditionsAffectedEvent event) {
        // At the moment, the only cases here are creatures on board that have an own health aura
        return data.query(Components.CREATURE_ZONE).list(card -> data.hasComponent(card, Components.AURAS));
    }
}
