package com.destrostudios.cards.shared.rules;

import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.aggregator.ArgumentsAggregator;

import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.RecordComponent;
import java.lang.reflect.Type;
import java.util.stream.Stream;

public class RecordAggregator<T> implements ArgumentsAggregator {

    @Override
    public T aggregateArguments(ArgumentsAccessor arguments, ParameterContext context) {
        Type superClass = getClass().getGenericSuperclass();
        Class<T> recordType = (Class) ((ParameterizedType) superClass).getActualTypeArguments()[0];
        RecordComponent[] recordComponents = recordType.getRecordComponents();
        Object[] values = new Object[recordComponents.length];
        for (int i = 0; i < recordComponents.length; i++) {
            RecordComponent component = recordComponents[i];
            Class<?> componentType = component.getType();
            Object value;
            if (componentType == int.class) {
                value = arguments.getInteger(i);
            } else if (componentType == String.class) {
                value = arguments.getString(i);
            } else {
                throw new UnsupportedOperationException("Unsupported component type " + componentType);
            }
            values[i] = value;
        }
        try {
            Constructor<T> constructor = recordType.getConstructor(Stream.of(recordComponents).map(RecordComponent::getType).toArray(Class[]::new));
            return constructor.newInstance(values);
        } catch (Exception ex) {
            throw new RuntimeException("Error while creating record of type " + recordType.getName(), ex);
        }
    }
}
