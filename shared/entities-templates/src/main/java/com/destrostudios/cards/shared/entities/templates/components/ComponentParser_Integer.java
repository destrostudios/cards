package com.destrostudios.cards.shared.entities.templates.components;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.entities.templates.ComponentParser;
import com.destrostudios.cards.shared.entities.templates.TemplateFormat;
import com.destrostudios.cards.shared.entities.templates.TemplateParser;

public class ComponentParser_Integer extends ComponentParser<Object, Integer> {

    public ComponentParser_Integer(ComponentDefinition<Integer> component) {
        super(component);
    }

    @Override
    public Integer parseValue(TemplateParser parser, TemplateFormat format, EntityData entityData, Object node) {
        return Integer.parseInt(parser.parseValue(entityData, format.getText(node)));
    }
}
