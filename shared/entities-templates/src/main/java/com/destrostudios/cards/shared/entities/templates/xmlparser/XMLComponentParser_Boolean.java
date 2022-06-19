package com.destrostudios.cards.shared.entities.templates.xmlparser;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.entities.templates.XMLComponentParser;
import com.destrostudios.cards.shared.entities.templates.XMLTemplateReader;
import org.jdom2.Element;

public class XMLComponentParser_Boolean extends XMLComponentParser<Boolean> {

    public XMLComponentParser_Boolean(ComponentDefinition<Boolean> component) {
        super(component);
    }

    @Override
    public Boolean parseValue(XMLTemplateReader templateReader, EntityData entityData, Element element) {
        return Boolean.parseBoolean(templateReader.parseValue(entityData, element.getText()));
    }
}
