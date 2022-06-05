package com.destrostudios.cards.shared.entities.templates.xmlparser;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.entities.templates.XMLComponentParser;

public class XMLComponentParser_Float extends XMLComponentParser<Float> {

    public XMLComponentParser_Float(ComponentDefinition<Float> component){
        super(component);
    }

    @Override
    public Float parseValue() {
        return Float.parseFloat(xmlTemplateManager.parseValue(entityData, element.getText()));
    }
}
