package com.destrostudios.cards.shared.entities.templates.xmlparser;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.entities.templates.XMLComponentParser;

public class XMLComponentParser_Templates extends XMLComponentParser<String[]> {

    public XMLComponentParser_Templates(ComponentDefinition<String[]> component){
        super(component);
    }

    @Override
    public String[] parseValue() {
        String[] lines = element.getText().split("\n");
        String[] values = new String[lines.length];
        for (int i = 0; i < values.length; i++) {
            values[i] = xmlTemplateManager.parseTemplateText(entityData, lines[i].trim());
        }
        return values;
    }
}
