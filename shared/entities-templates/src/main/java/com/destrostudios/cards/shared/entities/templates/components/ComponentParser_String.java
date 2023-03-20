package com.destrostudios.cards.shared.entities.templates.components;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.entities.templates.ComponentParser;
import com.destrostudios.cards.shared.entities.templates.TemplateFormat;
import com.destrostudios.cards.shared.entities.templates.TemplateParser;

public class ComponentParser_String extends ComponentParser<Object, String> {

    public ComponentParser_String(ComponentDefinition<String> component) {
        super(component);
    }

    @Override
    public String parseValue(TemplateParser parser, TemplateFormat format, EntityData entityData, Object node) {
        return parser.parseValue(entityData, format.getText(node));
    }
}
