package com.destrostudios.cards.shared.entities.templates.components;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.entities.templates.ComponentParser;
import com.destrostudios.cards.shared.entities.templates.TemplateFormat;
import com.destrostudios.cards.shared.entities.templates.TemplateParser;

public class ComponentParser_Boolean extends ComponentParser<Object, Boolean> {

    public ComponentParser_Boolean(ComponentDefinition<Boolean> component) {
        super(component);
    }

    @Override
    public Boolean parseValue(TemplateParser parser, TemplateFormat format, EntityData entityData, Object node) {
        return Boolean.parseBoolean(parser.parseValue(entityData, format.getText(node)));
    }
}
