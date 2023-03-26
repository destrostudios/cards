package com.destrostudios.cards.shared.rules.expressions;

import com.destrostudios.cards.shared.entities.EntityData;
import org.apache.commons.jexl3.*;

public class Expressions {

    private static JexlEngine jexlEngine = new JexlBuilder().cache(512).strict(true).silent(false).create();

    public static <T> T evaluate(EntityData data, int source, int target, String expression) {
        JexlContext context = new MapContext();
        context.set("source", new ExpressionEntity(data, source));
        context.set("target", new ExpressionEntity(data, target));
        return (T) jexlEngine.createExpression(expression).evaluate(context);
    }
}
