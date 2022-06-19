package com.destrostudios.cards.shared.entities.templates.xmlparser;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.entities.templates.XMLComponentParser;
import com.destrostudios.cards.shared.entities.templates.XMLTemplateReader;
import org.jdom2.Element;

public class XMLComponentParser_String extends XMLComponentParser<String> {

    public XMLComponentParser_String(ComponentDefinition<String> component) {
        super(component);
    }

    @Override
    public String parseValue(XMLTemplateReader templateReader, EntityData entityData, Element element) {
        return templateReader.parseValue(entityData, element.getText());
    }
}
