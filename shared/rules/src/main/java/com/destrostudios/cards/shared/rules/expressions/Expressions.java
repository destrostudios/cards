package com.destrostudios.cards.shared.rules.expressions;

import com.destrostudios.cards.shared.entities.EntityData;
import org.apache.commons.jexl3.*;

import java.util.Collections;

public class Expressions {

    private static final JexlEngine JEXL_ENGINE = new JexlBuilder()
            .silent(false)
            .strict(true)
            .debug(false)
            .cache(512)
            .cacheThreshold(Integer.MAX_VALUE)
            .namespaces(Collections.singletonMap(null, ExpressionGlobals.class))
            .create();

    public static int[] evaluateEntities(EntityData data, String expression, int source, int[] targets) {
        Object result = evaluate(data, expression, source, targets);
        if (result instanceof ExpressionEntity expressionEntity) {
            result = new ExpressionEntity[] { expressionEntity };
        }
        ExpressionEntity[] expressionEntities = (ExpressionEntity[]) result;
        int[] entities = new int[expressionEntities.length];
        for (int i = 0; i < entities.length; i++) {
            entities[i] = expressionEntities[i].getEntity();
        }
        return entities;
    }

    public static <T> T evaluate(EntityData data, String expression, int source, int[] targets) {
        JexlContext context = createContext(data, source);
        ExpressionEntity[] expressionEntities = new ExpressionEntity[targets.length];
        for (int i = 0; i < expressionEntities.length; i++) {
            expressionEntities[i] = ExpressionEntity.wrap(data, targets[i]);
        }
        context.set("targets", expressionEntities);
        return evaluate(expression, context);
    }

    public static <T> T evaluate(EntityData data, String expression, int source, Integer target) {
        JexlContext context = createContext(data, source);
        context.set("target", ExpressionEntity.wrap(data, target));
        return evaluate(expression, context);
    }

    private static JexlContext createContext(EntityData data, int source) {
        JexlContext context = new MapContext();
        context.set("source", ExpressionEntity.wrap(data, source));
        return context;
    }

    private static <T> T evaluate(String expression, JexlContext context) {
        try {
            return (T) JEXL_ENGINE.createExpression(expression).evaluate(context);
        } catch (JexlException ex) {
            System.err.println("Error while evaluating expression: " + expression);
            throw ex;
        }
    }
}
