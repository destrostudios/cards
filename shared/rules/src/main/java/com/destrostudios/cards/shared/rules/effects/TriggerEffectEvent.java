package com.destrostudios.cards.shared.rules.effects;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.EventType;
import com.destrostudios.cards.shared.rules.expressions.ExpressionContextProvider;
import com.destrostudios.cards.shared.rules.expressions.ExpressionEntity;
import org.apache.commons.jexl3.JexlContext;

public class TriggerEffectEvent extends Event implements ExpressionContextProvider {

    public final int source;
    public final int[] targets;
    public final int effect;

    public TriggerEffectEvent(int source, int[] targets, int effect) {
        super(EventType.TRIGGER_EFFECT);
        this.source = source;
        this.targets = targets;
        this.effect = effect;
    }

    @Override
    public String toString() {
        return "TriggerEffectEvent{" + "source=" + source + ", targets=" + targets + ", effect=" + effect + '}';
    }

    @Override
    public void fillMinimalRequiredExpressionContext(EntityData data, JexlContext context) {
        context.set("source", ExpressionEntity.wrap(data, source));
        context.set("targets", ExpressionEntity.wrap(data, targets));
    }
}
