package com.destrostudios.cards.shared.rules.expressions;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.events.Event;
import org.apache.commons.jexl3.*;

import java.lang.reflect.Field;
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

    public static JexlContext getContext_Source_Target(EntityData data, int source, Integer target) {
        JexlContext context = new MapContext();
        context.set("source", ExpressionEntity.wrap(data, source));
        context.set("target", ExpressionEntity.wrap(data, target));
        return context;
    }

    public static JexlContext getContext_Event(EntityData data, Event event) {
        JexlContext context = new MapContext();
        if (event != null) {
            for (Field field : event.getClass().getDeclaredFields()) {
                String name = field.getName();
                try {
                    Object value = field.get(event);
                    if (name.equals("source") || name.equals("target")) {
                        value = ExpressionEntity.wrap(data, (Integer) value);
                    } else if (name.equals("targets")) {
                        value = ExpressionEntity.wrap(data, (int[]) value);
                    }
                    context.set(name, value);
                } catch (IllegalAccessException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
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
            return (T) JEXL_ENGINE.createExpression(expression).evaluate(context);
        } catch (JexlException ex) {
            System.err.println("Error while evaluating expression: " + expression);
            ex.printStackTrace();
            throw ex;
        }
    }
}
