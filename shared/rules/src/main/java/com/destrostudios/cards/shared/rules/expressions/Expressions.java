package com.destrostudios.cards.shared.rules.expressions;

import com.destrostudios.cards.shared.entities.EntityData;
import org.apache.commons.jexl3.*;
import org.apache.commons.jexl3.introspection.JexlPermissions;

public class Expressions {

    private static JexlEngine ENGINE;

    public static void setup() {
        ENGINE = new JexlBuilder()
            .silent(false)
            .strict(true)
            .debug(false)
            .permissions(JexlPermissions.UNRESTRICTED)
            .cache(512)
            .cacheThreshold(Integer.MAX_VALUE)
            // .namespaces(Collections.singletonMap(null, ExpressionGlobals.class))
            .create();
    }

    public static JexlContext getContext_Source_Target(EntityData data, int source, Integer target) {
        JexlContext context = new MapContext();
        context.set("source", ExpressionEntity.wrap(data, source));
        context.set("target", ExpressionEntity.wrap(data, target));
        return context;
    }

    public static JexlContext getContext_Provider(EntityData data, ExpressionContextProvider provider) {
        JexlContext context = new MapContext();
        provider.fillExpressionContext(data, context);
        return context;
    }

    public static int[] evaluateEntities(String expression, JexlContext context) {
        Object result = evaluate(expression, context);
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

    public static <T> T evaluate(String expression, JexlContext context) {
        try {
            if (isIntegerExpression(expression)) {
                return (T) Integer.valueOf(expression);
            }
            return (T) ENGINE.createExpression(expression).evaluate(context);
        } catch (JexlException ex) {
            System.err.println("Error while evaluating expression: " + expression);
            ex.printStackTrace();
            throw ex;
        }
    }

    private static boolean isIntegerExpression(String text) {
        return text.chars().allMatch(c -> ((c <= '9') && ((c >= '0')) || (c == '-')));
    }
}
