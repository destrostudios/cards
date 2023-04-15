package com.destrostudios.cards.shared.entities.templates.components;

import com.destrostudios.cards.shared.entities.templates.ComponentParser;
import com.destrostudios.cards.shared.entities.templates.TemplateFormat;
import com.destrostudios.cards.shared.entities.templates.TemplateParser;

import java.util.function.Function;

public class ComponentParser_Enum<E extends Enum<?>> extends ComponentParser<Object, E, E> {

    public ComponentParser_Enum(Function<String, E> mapEnum) {
        this.mapEnum = mapEnum;
    }
    private Function<String, E> mapEnum;

    @Override
    public E parse(TemplateParser parser, TemplateFormat format, Object node) {
        return mapEnum.apply(parser.parseText(format.getText(node)));
    }

    @Override
    public E resolve(int[] proxiedEntities, E recordedValue) {
        return recordedValue;
    }
}
