package com.destrostudios.cards.shared.entities.templates.xmlparser;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.entities.templates.XMLComponentParser;

public class XMLComponentParser_Entity extends XMLComponentParser<Integer> {

    public XMLComponentParser_Entity(ComponentDefinition<Integer> component){
        super(component);
    }

    @Override
    public Integer parseValue() {
        return createChildEntity(0, "entity");
    }
}
