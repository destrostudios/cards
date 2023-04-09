package com.destrostudios.cards.shared.entities.templates.components;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.entities.templates.ComponentParser;
import com.destrostudios.cards.shared.entities.templates.TemplateFormat;
import com.destrostudios.cards.shared.entities.templates.TemplateParser;

import java.util.function.Function;
import java.util.function.IntFunction;

public class ComponentParser_EnumArray<E extends Enum<?>> extends ComponentParser<Object, E[]> {

    public ComponentParser_EnumArray(ComponentDefinition<E[]> component, IntFunction<E[]> createEnumArray, Function<String, E> mapEnum) {
        super(component);
        this.createEnumArray = createEnumArray;
        this.mapEnum = mapEnum;
    }
    private IntFunction<E[]> createEnumArray;
    private Function<String, E> mapEnum;

    @Override
    public E[] parseValue(TemplateParser parser, TemplateFormat format, EntityData entityData, Object node) {
        String[] names = format.getText(node).split(",");
        E[] enums = createEnumArray.apply(names.length);
        for (int i = 0; i < enums.length; i++) {
            enums[i] = mapEnum.apply(names[i]);
        }
        return enums;
    }
}
