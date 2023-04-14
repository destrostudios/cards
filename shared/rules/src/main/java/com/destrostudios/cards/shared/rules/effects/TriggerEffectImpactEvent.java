package com.destrostudios.cards.shared.rules.effects;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.EventType;
import com.destrostudios.cards.shared.rules.expressions.ExpressionContextProvider;
import com.destrostudios.cards.shared.rules.expressions.ExpressionEntity;
import org.apache.commons.jexl3.JexlContext;

public class TriggerEffectImpactEvent extends Event implements ExpressionContextProvider {

    public final int source;
    public final int target;
    public final int effect;

    public TriggerEffectImpactEvent(int source, int target, int effect) {
        super(EventType.TRIGGER_EFFECT_IMPACT);
        this.source = source;
        this.target = target;
        this.effect = effect;
    }

    @Override
    public String toString() {
        return "TriggerEffectImpactEvent{" + "source=" + source + ", target=" + target + ", effect=" + effect + '}';
    }

    @Override
    public void fillMinimalRequiredExpressionContext(EntityData data, JexlContext context) {
        context.set("source", ExpressionEntity.wrap(data, source));
        context.set("target", ExpressionEntity.wrap(data, target));
    }
}
