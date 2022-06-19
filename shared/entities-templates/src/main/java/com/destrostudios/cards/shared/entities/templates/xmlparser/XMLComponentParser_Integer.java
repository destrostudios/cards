package com.destrostudios.cards.shared.entities.templates.xmlparser;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.entities.templates.XMLComponentParser;
import com.destrostudios.cards.shared.entities.templates.XMLTemplateReader;
import org.jdom2.Element;

public class XMLComponentParser_Integer extends XMLComponentParser<Integer> {

    public XMLComponentParser_Integer(ComponentDefinition<Integer> component) {
        super(component);
    }

    @Override
    public Integer parseValue(XMLTemplateReader templateReader, EntityData entityData, Element element) {
        return Integer.parseInt(templateReader.parseValue(entityData, element.getText()));
    }
}
