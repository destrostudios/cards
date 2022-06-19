package com.destrostudios.cards.shared.entities.templates.xmlparser;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.entities.templates.XMLComponentParser;
import com.destrostudios.cards.shared.entities.templates.XMLTemplateReader;
import org.jdom2.Element;

public class XMLComponentParser_Void extends XMLComponentParser<Void> {

    public XMLComponentParser_Void(ComponentDefinition<Void> component) {
        super(component);
    }

    @Override
    public Void parseValue(XMLTemplateReader templateReader, EntityData entityData, Element element) {
        return null;
    }
}
