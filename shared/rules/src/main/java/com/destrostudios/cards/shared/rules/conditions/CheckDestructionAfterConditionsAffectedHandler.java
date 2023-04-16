package com.destrostudios.cards.shared.rules.conditions;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.battle.ConditionsAffectedEvent;
import com.destrostudios.cards.shared.rules.battle.BaseDestroyOnZeroHealthHandler;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;

public class CheckDestructionAfterConditionsAffectedHandler extends BaseDestroyOnZeroHealthHandler<ConditionsAffectedEvent> {

    @Override
    public void handle(ConditionsAffectedEvent event, NetworkRandom random) {
        // Currently, only creatures with self-health-auras could drop to zero health here
        for (int target : data.listAll(Components.Zone.CREATURE_ZONE, Components.AURAS)) {
            destroyOnZeroHealth(target, event, random);
        }
    }
}
