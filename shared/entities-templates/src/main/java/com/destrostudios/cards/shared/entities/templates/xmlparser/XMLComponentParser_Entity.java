package com.destrostudios.cards.shared.entities.templates.xmlparser;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.entities.templates.XMLComponentParser;
import com.destrostudios.cards.shared.entities.templates.XMLTemplateReader;
import org.jdom2.Element;

public class XMLComponentParser_Entity extends XMLComponentParser<Integer> {

    public XMLComponentParser_Entity(ComponentDefinition<Integer> component) {
        super(component);
    }

    @Override
    public Integer parseValue(XMLTemplateReader templateReader, EntityData entityData, Element element) {
        return createChildEntity(templateReader, entityData, element, 0, "entity");
    }
}
