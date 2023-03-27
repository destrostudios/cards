package com.destrostudios.cards.shared.rules.expressions;

import com.destrostudios.cards.shared.entities.EntityData;
import org.apache.commons.jexl3.*;

public class Expressions {

    private static final JexlEngine JEXL_ENGINE = new JexlBuilder()
            .silent(false)
            .strict(true)
            .debug(false)
            .cache(512)
            .cacheThreshold(Integer.MAX_VALUE)
            .create();

    public static <T> T evaluate(EntityData data, String expression, int source, Integer target) {
        JexlContext context = new MapContext();
        context.set("source", ExpressionEntity.wrap(data, source));
        context.set("target", ExpressionEntity.wrap(data, target));
        return (T) JEXL_ENGINE.createExpression(expression).evaluate(context);
    }
}
