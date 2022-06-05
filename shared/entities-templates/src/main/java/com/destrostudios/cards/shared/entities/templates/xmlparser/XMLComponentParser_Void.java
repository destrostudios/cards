package com.destrostudios.cards.shared.entities.templates.xmlparser;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.entities.templates.XMLComponentParser;

public class XMLComponentParser_Void extends XMLComponentParser<Void> {

    public XMLComponentParser_Void(ComponentDefinition<Void> component){
        super(component);
    }

    @Override
    public Void parseValue() {
        return null;
    }
}
