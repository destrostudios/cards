package com.destrostudios.cards.shared.entities.templates.xmlparser;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.entities.templates.XMLComponentParser;

public class XMLComponentParser_String extends XMLComponentParser<String> {

    public XMLComponentParser_String(ComponentDefinition<String> component){
        super(component);
    }

    @Override
    public String parseValue() {
        return element.getText();
    }
}
