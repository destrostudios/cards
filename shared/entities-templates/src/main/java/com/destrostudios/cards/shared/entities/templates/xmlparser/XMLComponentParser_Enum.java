package com.destrostudios.cards.shared.entities.templates.xmlparser;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.entities.templates.XMLComponentParser;

import java.util.function.Function;

public class XMLComponentParser_Enum<E extends Enum<?>> extends XMLComponentParser<E> {

    public XMLComponentParser_Enum(ComponentDefinition<E> component, Function<String, E> mapEnum){
        super(component);
        this.mapEnum = mapEnum;
    }
    private Function<String, E> mapEnum;

    @Override
    public E parseValue() {
        return mapEnum.apply(element.getText());
    }
}
