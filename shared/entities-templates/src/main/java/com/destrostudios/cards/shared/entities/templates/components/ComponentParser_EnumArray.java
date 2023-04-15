package com.destrostudios.cards.shared.entities.templates.components;

import com.destrostudios.cards.shared.entities.templates.ComponentParser;
import com.destrostudios.cards.shared.entities.templates.TemplateFormat;
import com.destrostudios.cards.shared.entities.templates.TemplateParser;

import java.util.function.Function;
import java.util.function.IntFunction;

public class ComponentParser_EnumArray<E extends Enum<?>> extends ComponentParser<Object, E[], E[]> {

    public ComponentParser_EnumArray(IntFunction<E[]> createEnumArray, Function<String, E> mapEnum) {
        this.createEnumArray = createEnumArray;
        this.mapEnum = mapEnum;
    }
    private IntFunction<E[]> createEnumArray;
    private Function<String, E> mapEnum;

    @Override
    public E[] parse(TemplateParser parser, TemplateFormat format, Object node) {
        String[] names = parser.parseText(format.getText(node)).split(",");
        E[] enums = createEnumArray.apply(names.length);
        for (int i = 0; i < enums.length; i++) {
            enums[i] = mapEnum.apply(names[i]);
        }
        return enums;
    }

    @Override
    public E[] resolve(int[] proxiedEntities, E[] recordedValue) {
        return recordedValue;
    }
}
