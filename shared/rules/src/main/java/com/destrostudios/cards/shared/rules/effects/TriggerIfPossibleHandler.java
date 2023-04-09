package com.destrostudios.cards.shared.rules.effects;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.expressions.Expressions;
import com.destrostudios.cards.shared.rules.util.ConditionUtil;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TriggerIfPossibleHandler extends GameEventHandler<TriggerIfPossibleEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(TriggerIfPossibleHandler.class);

    @Override
    public void handle(TriggerIfPossibleEvent event, NetworkRandom random) {
        LOG.debug("Trying to trigger trigger (source = " + inspect(event.source) + ", targets = " + inspect(event.targets) + ", trigger = " + inspect(event.trigger) + ")");

        if (ConditionUtil.isConditionFulfilled(data, event.trigger, event.source, event.targets)) {
            String repeatExpression = data.getComponent(event.trigger, Components.Effect.REPEAT);
            int repetitions = ((repeatExpression != null) ? Expressions.evaluate(data, repeatExpression, event.source, event.targets) : 1);
            int[] effects = data.getComponent(event.trigger, Components.Trigger.EFFECTS);
            for (int effect : effects) {
                for (int i = 0; i < repetitions; i++) {
                    events.fire(new TriggerEffectEvent(event.source, event.targets, effect), random);
                }
            }
        }
    }
}
