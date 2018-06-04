package com.destrostudios.cards.shared.entities.templates.xmlparser;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.entities.templates.XMLComponentParser;

public class XMLComponentParser_Integer extends XMLComponentParser<Integer> {

    public XMLComponentParser_Integer(ComponentDefinition<Integer> component){
        super(component);
    }

    @Override
    public Integer parseValue() {
        return Integer.parseInt(xmlTemplateManager.parseValue(entityData, element.getText()));
    }
}
