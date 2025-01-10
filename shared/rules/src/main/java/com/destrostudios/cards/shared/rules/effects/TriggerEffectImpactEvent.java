package com.destrostudios.cards.shared.rules.effects;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.EventType;
import com.destrostudios.cards.shared.rules.expressions.ExpressionContextProvider;
import com.destrostudios.cards.shared.rules.expressions.ExpressionEntity;
import org.apache.commons.jexl3.JexlContext;

public class TriggerEffectImpactEvent extends Event implements ExpressionContextProvider {

    public final int source;
    public final int[] baseTargets;
    public final int target;
    public final int effect;
    public final EffectOptions options;

    public TriggerEffectImpactEvent(int source, int[] baseTargets, int target, int effect, EffectOptions options) {
        super(EventType.TRIGGER_EFFECT_IMPACT);
        this.source = source;
        this.baseTargets = baseTargets;
        this.target = target;
        this.effect = effect;
        this.options = options;
    }

    @Override
    public String toString() {
        return "TriggerEffectImpactEvent{" + "source=" + source + ", baseTargets=" + baseTargets + ", target=" + target + ", effect=" + effect + ", options=" + options + "}";
    }

    @Override
    public void fillExpressionContext(EntityData data, JexlContext context) {
        context.set("source", ExpressionEntity.wrap(data, source));
        context.set("baseTargets", ExpressionEntity.wrap(data, baseTargets));
        context.set("target", ExpressionEntity.wrap(data, target));
    }
}
