package com.destrostudios.cards.shared.rules.effects;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.util.ConditionUtil;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.util.TargetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CheckEffectTriggerHandler extends GameEventHandler<CheckEffectTriggerEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(CheckEffectTriggerHandler.class);

    @Override
    public void handle(CheckEffectTriggerEvent event) {
        LOG.info("Checking effect trigger (source={}, target={}, effectTrigger={})", event.source, event.targets, event.effectTrigger);

        if (ConditionUtil.areConditionsFulfilled(data, event.effectTrigger, event.source, event.targets)) {
            int[] effects = data.getComponent(event.effectTrigger, Components.EffectTrigger.EFFECTS);
            for (int effect : effects) {
                List<Integer> affectedTargets = TargetUtil.getAffectedTargets(data, effect, event.source, event.targets);
                for (int target : affectedTargets) {
                    events.fire(new TriggerEffectEvent(event.source, target, effect));
                }
            }
        }
    }
}
