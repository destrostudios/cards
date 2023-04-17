package com.destrostudios.cards.shared.rules.effects;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameContext;
import com.destrostudios.cards.shared.rules.expressions.Expressions;
import com.destrostudios.cards.shared.rules.util.ConditionUtil;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TriggerIfPossibleHandler extends GameEventHandler<TriggerIfPossibleEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(TriggerIfPossibleHandler.class);

    @Override
    public void handle(GameContext context, TriggerIfPossibleEvent event) {
        EntityData data = context.getData();
        LOG.debug("Trying to trigger trigger (source = {}, targets = {}, trigger = {})", inspect(data, event.source), inspect(data, event.targets), inspect(data, event.trigger));

        if (ConditionUtil.isConditionFulfilled(data, event.trigger, event.source, event.targets)) {
            String repeatExpression = data.getComponent(event.trigger, Components.Effect.REPEAT);
            int repetitions = ((repeatExpression != null) ? Expressions.evaluate(repeatExpression, Expressions.getContext_Provider(data, event)) : 1);
            int[] effects = data.getComponent(event.trigger, Components.Trigger.EFFECTS);
            for (int effect : effects) {
                for (int i = 0; i < repetitions; i++) {
                    context.getEvents().fire(new TriggerEffectEvent(event.source, event.targets, effect));
                }
            }
        }
    }
}
