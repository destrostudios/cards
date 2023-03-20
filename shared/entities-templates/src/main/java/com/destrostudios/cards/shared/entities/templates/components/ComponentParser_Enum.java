package com.destrostudios.cards.shared.entities.templates.components;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.entities.templates.ComponentParser;
import com.destrostudios.cards.shared.entities.templates.TemplateFormat;
import com.destrostudios.cards.shared.entities.templates.TemplateParser;

import java.util.function.Function;

public class ComponentParser_Enum<E extends Enum<?>> extends ComponentParser<Object, E> {

    public ComponentParser_Enum(ComponentDefinition<E> component, Function<String, E> mapEnum) {
        super(component);
        this.mapEnum = mapEnum;
    }
    private Function<String, E> mapEnum;

    @Override
    public E parseValue(TemplateParser parser, TemplateFormat format, EntityData entityData, Object node) {
        return mapEnum.apply(format.getText(node));
    }
}
