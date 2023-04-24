package com.destrostudios.cards.shared.rules.effects;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.EventType;
import com.destrostudios.cards.shared.rules.expressions.ExpressionContextProvider;
import com.destrostudios.cards.shared.rules.expressions.ExpressionEntity;
import org.apache.commons.jexl3.JexlContext;

public class TriggerEvent extends Event implements ExpressionContextProvider {

    public final int source;
    public final int[] targets;
    public final int trigger;

    public TriggerEvent(int source, int[] targets, int trigger) {
        super(EventType.TRIGGER);
        this.source = source;
        this.targets = targets;
        this.trigger = trigger;
    }

    @Override
    public String toString() {
        return "TriggerEvent{" + "source=" + source + ", targets=" + targets + ", trigger=" + trigger + '}';
    }

    @Override
    public void fillMinimalRequiredExpressionContext(EntityData data, JexlContext context) {
        context.set("source", ExpressionEntity.wrap(data, source));
        context.set("targets", ExpressionEntity.wrap(data, targets));
    }
}
