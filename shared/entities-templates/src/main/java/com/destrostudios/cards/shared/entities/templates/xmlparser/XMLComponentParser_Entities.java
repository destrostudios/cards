package com.destrostudios.cards.shared.entities.templates.xmlparser;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.entities.templates.XMLComponentParser;
import com.destrostudios.cards.shared.entities.templates.XMLTemplateReader;
import org.jdom2.Element;

public class XMLComponentParser_Entities extends XMLComponentParser<int[]> {

    public XMLComponentParser_Entities(ComponentDefinition<int[]> component) {
        super(component);
    }

    @Override
    public int[] parseValue(XMLTemplateReader templateReader, EntityData entityData, Element element) {
        return createChildEntities(templateReader, entityData, element, 0, "entities");
    }
}
