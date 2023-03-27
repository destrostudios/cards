package com.destrostudios.cards.shared.rules.effects;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.util.ConditionUtil;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.util.TargetUtil;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class TriggerEffectTriggerIfPossibleHandler extends GameEventHandler<TriggerEffectTriggerIfPossibleEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(TriggerEffectTriggerIfPossibleHandler.class);

    @Override
    public void handle(TriggerEffectTriggerIfPossibleEvent event, NetworkRandom random) {
        LOG.info("Trying to trigger effect trigger (source={}, target={}, effectTrigger={})", event.source, event.targets, event.effectTrigger);

        if (ConditionUtil.isConditionFulfilled(data, event.effectTrigger, event.source, event.targets)) {
            int[] effects = data.getComponent(event.effectTrigger, Components.EffectTrigger.EFFECTS);
            for (int effect : effects) {
                int[] targetDefinitions = data.getComponent(effect, Components.Target.TARGETS);
                List<Integer> affectedTargets = TargetUtil.getAffectedTargets(data, targetDefinitions, event.source, event.targets);
                for (int target : affectedTargets) {
                    events.fire(new TriggerEffectEvent(event.source, target, effect), random);
                }
            }
        }
    }
}
