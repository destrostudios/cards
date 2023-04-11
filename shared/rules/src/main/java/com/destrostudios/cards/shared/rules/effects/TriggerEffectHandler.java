package com.destrostudios.cards.shared.rules.effects;

import com.destrostudios.cards.shared.entities.IntList;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.expressions.Expressions;
import com.destrostudios.cards.shared.rules.util.TargetUtil;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TriggerEffectHandler extends GameEventHandler<TriggerEffectEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(TriggerEffectHandler.class);

    @Override
    public void handle(TriggerEffectEvent event, NetworkRandom random) {
        LOG.debug("Triggering effect (source = {}, targets = {}, effect = {})", inspect(event.source), inspect(event.targets), inspect(event.effect));

        String repeatExpression = data.getComponent(event.effect, Components.Effect.REPEAT);
        int repetitions = ((repeatExpression != null) ? Expressions.evaluate(repeatExpression, Expressions.getContext_Event(data, event)) : 1);
        int[] targetDefinitions = data.getComponent(event.effect, Components.Target.TARGETS);
        IntList affectedTargets = TargetUtil.getAffectedTargets(data, targetDefinitions, event.source, event, random);
        for (int i = 0; i < repetitions; i++) {
            for (int target : affectedTargets) {
                events.fire(new TriggerEffectImpactEvent(event.source, target, event.effect), random);
            }
        }
    }
}
