package com.destrostudios.cards.shared.rules.effects;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.EventType;
import com.destrostudios.cards.shared.rules.expressions.ExpressionContextProvider;
import com.destrostudios.cards.shared.rules.expressions.ExpressionEntity;
import org.apache.commons.jexl3.JexlContext;

public class TriggerEvent extends Event implements ExpressionContextProvider {

    public final int trigger;
    public final int source;
    public final int[] targets;
    public final EffectOptions options;

    public TriggerEvent(int trigger, int source, int[] targets, EffectOptions options) {
        super(EventType.TRIGGER);
        this.trigger = trigger;
        this.source = source;
        this.targets = targets;
        this.options = options;
    }

    @Override
    public String toString() {
        return "TriggerEvent{" + "trigger=" + trigger + ", source=" + source + ", targets=" + targets + ", options=" + options + "}";
    }

    @Override
    public void fillExpressionContext(EntityData data, JexlContext context) {
        context.set("source", ExpressionEntity.wrap(data, source));
        context.set("targets", ExpressionEntity.wrap(data, targets));
    }
}
