package com.destrostudios.cards.shared.rules.conditions;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.battle.ConditionsAffectedEvent;
import com.destrostudios.cards.shared.rules.battle.DestroyOnZeroHealthHandler;

import java.util.List;

public class CheckDestructionAfterConditionsAffectedHandler extends DestroyOnZeroHealthHandler<ConditionsAffectedEvent> {

    @Override
    protected List<Integer> getAffectedTargets(EntityData data, ConditionsAffectedEvent event) {
        return data.query(Components.AURAS).list(card -> data.hasComponent(card, Components.BOARD));
    }
}
