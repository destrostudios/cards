package com.destrostudios.cards.shared.rules.effects;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.expressions.Expressions;
import com.destrostudios.cards.shared.rules.util.TargetUtil;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;

import java.util.List;

public class TriggerEffectHandler extends GameEventHandler<TriggerEffectEvent> {

    @Override
    public void handle(TriggerEffectEvent event, NetworkRandom random) {
        String repeatExpression = data.getComponent(event.effect, Components.Effect.REPEAT);
        int repetitions = ((repeatExpression != null) ? Expressions.evaluate(data, repeatExpression, event.source, event.targets) : 1);
        int[] targetDefinitions = data.getComponent(event.effect, Components.Target.TARGETS);
        List<Integer> affectedTargets = TargetUtil.getAffectedTargets(data, targetDefinitions, event.source, event.targets);
        for (int i = 0; i < repetitions; i++) {
            for (int target : affectedTargets) {
                events.fire(new TriggerEffectImpactEvent(event.source, target, event.effect), random);
            }
        }
    }
}
