package com.destrostudios.cards.shared.entities.templates.xmlparser;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.entities.templates.XMLComponentParser;

public class XMLComponentParser_Entities extends XMLComponentParser<int[]> {

    public XMLComponentParser_Entities(ComponentDefinition<int[]> component){
        super(component);
    }

    @Override
    public int[] parseValue() {
        return createChildEntities(0, "entities");
    }
}
