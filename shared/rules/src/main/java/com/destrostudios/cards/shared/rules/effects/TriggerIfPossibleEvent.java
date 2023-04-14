package com.destrostudios.cards.shared.rules.effects;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.EventType;
import com.destrostudios.cards.shared.rules.expressions.ExpressionContextProvider;
import com.destrostudios.cards.shared.rules.expressions.ExpressionEntity;
import org.apache.commons.jexl3.JexlContext;

public class TriggerIfPossibleEvent extends Event implements ExpressionContextProvider {

    public final int source;
    public final int[] targets;
    public final int trigger;

    public TriggerIfPossibleEvent(int source, int[] targets, int trigger) {
        super(EventType.TRIGGER_IF_POSSIBLE);
        this.source = source;
        this.targets = targets;
        this.trigger = trigger;
    }

    @Override
    public String toString() {
        return "TriggerIfPossibleEvent{" + "source=" + source + ", targets=" + targets + ", trigger=" + trigger + '}';
    }

    @Override
    public void fillMinimalRequiredExpressionContext(EntityData data, JexlContext context) {
        context.set("source", ExpressionEntity.wrap(data, source));
        context.set("targets", ExpressionEntity.wrap(data, targets));
    }
}
