package com.destrostudios.cards.shared.rules.effects;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.entities.IntList;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameContext;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.expressions.Expressions;
import com.destrostudios.cards.shared.rules.util.TargetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TriggerEffectHandler extends GameEventHandler<TriggerEffectEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(TriggerEffectHandler.class);

    @Override
    public void handle(GameContext context, TriggerEffectEvent event) {
        EntityData data = context.getData();
        LOG.debug("Triggering effect (source = {}, targets = {}, effect = {})", inspect(data, event.source), inspect(data, event.targets), inspect(data, event.effect));

        String repeatExpression = data.getComponent(event.effect, Components.Effect.REPEAT);
        int repetitions = ((repeatExpression != null) ? Expressions.evaluate(repeatExpression, Expressions.getContext_Provider(data, event)) : 1);
        int[] targetDefinitions = data.getComponent(event.effect, Components.Target.TARGETS);
        IntList affectedTargets = TargetUtil.getAffectedTargets(data, targetDefinitions, event.source, event.targets, event, context.getRandom());
        for (int i = 0; i < repetitions; i++) {
            for (int target : affectedTargets) {
                context.getEvents().fire(new TriggerEffectImpactEvent(event.source, target, event.effect));
            }
        }
    }
}
