package com.destrostudios.cards.shared.entities.templates.xmlparser;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.entities.templates.XMLComponentParser;

public class XMLComponentParser_Boolean extends XMLComponentParser<Boolean> {

    public XMLComponentParser_Boolean(ComponentDefinition<Boolean> component){
        super(component);
    }

    @Override
    public Boolean parseValue() {
        return Boolean.parseBoolean(xmlTemplateManager.parseValue(entityData, element.getText()));
    }
}
