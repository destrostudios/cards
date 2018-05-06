package com.destrostudios.cards.shared.network;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.rules.Components;
import com.jme3.network.serializing.Serializer;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

/**
 *
 * @author Philipp
 */
@SuppressWarnings("rawtypes")
public class ComponentDefinitionSerializer extends Serializer {

    private static final ComponentDefinition[] COMPONENTS;

    static {
        List<ComponentDefinition<?>> list = new ArrayList<>();
        collectComponents(Components.class, list::add);
        list.sort(Comparator.comparing(ComponentDefinition::getName));
        COMPONENTS = list.toArray(new ComponentDefinition[list.size()]);
    }

    private static void collectComponents(Class<?> clss, Consumer<ComponentDefinition<?>> consumer) {
        try {
            collectComponentsThrows(clss, consumer);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static void collectComponentsThrows(Class<?> clss, Consumer<ComponentDefinition<?>> consumer) throws IllegalArgumentException, IllegalAccessException {
        for (Class<?> classe : clss.getClasses()) {
            collectComponentsThrows(classe, consumer);
        }
        for (Field field : clss.getFields()) {
            if (Modifier.isStatic(field.getModifiers()) && ComponentDefinition.class.isAssignableFrom(field.getType())) {
                ComponentDefinition<?> component = (ComponentDefinition<?>) field.get(null);
                consumer.accept(component);
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T readObject(ByteBuffer data, Class<T> c) throws IOException {
        return (T) COMPONENTS[data.getInt()];
    }

    @Override
    public void writeObject(ByteBuffer buffer, Object object) throws IOException {
        buffer.putInt(indexOf(COMPONENTS, object));
    }

    private static <T> int indexOf(T[] array, T item) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == item) {
                return i;
            }
        }
        throw new IllegalStateException();
    }

}
