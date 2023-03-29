package com.destrostudios.cards.shared.rules.effects;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.expressions.Expressions;
import com.destrostudios.cards.shared.rules.util.ConditionUtil;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TriggerEffectTriggerIfPossibleHandler extends GameEventHandler<TriggerEffectTriggerIfPossibleEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(TriggerEffectTriggerIfPossibleHandler.class);

    @Override
    public void handle(TriggerEffectTriggerIfPossibleEvent event, NetworkRandom random) {
        LOG.info("Trying to trigger effect trigger (source={}, target={}, effectTrigger={})", event.source, event.targets, event.effectTrigger);

        if (ConditionUtil.isConditionFulfilled(data, event.effectTrigger, event.source, event.targets)) {
            String repeatExpression = data.getComponent(event.effectTrigger, Components.Effect.REPEAT);
            int repetitions = ((repeatExpression != null) ? Expressions.evaluate(data, repeatExpression, event.source, event.targets) : 1);
            int[] effects = data.getComponent(event.effectTrigger, Components.EffectTrigger.EFFECTS);
            for (int effect : effects) {
                for (int i = 0; i < repetitions; i++) {
                    events.fire(new TriggerEffectEvent(event.source, event.targets, effect), random);
                }
            }
        }
    }
}
