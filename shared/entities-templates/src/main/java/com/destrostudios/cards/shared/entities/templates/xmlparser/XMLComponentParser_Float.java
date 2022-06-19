package com.destrostudios.cards.shared.entities.templates.xmlparser;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.entities.templates.XMLComponentParser;
import com.destrostudios.cards.shared.entities.templates.XMLTemplateReader;
import org.jdom2.Element;

public class XMLComponentParser_Float extends XMLComponentParser<Float> {

    public XMLComponentParser_Float(ComponentDefinition<Float> component) {
        super(component);
    }

    @Override
    public Float parseValue(XMLTemplateReader templateReader, EntityData entityData, Element element) {
        return Float.parseFloat(templateReader.parseValue(entityData, element.getText()));
    }
}
