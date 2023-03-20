package com.destrostudios.cards.shared.entities.templates.components;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.entities.templates.ComponentParser;
import com.destrostudios.cards.shared.entities.templates.TemplateFormat;
import com.destrostudios.cards.shared.entities.templates.TemplateParser;

public class ComponentParser_Float extends ComponentParser<Object, Float> {

    public ComponentParser_Float(ComponentDefinition<Float> component) {
        super(component);
    }

    @Override
    public Float parseValue(TemplateParser parser, TemplateFormat format, EntityData entityData, Object node) {
        return Float.parseFloat(parser.parseValue(entityData, format.getText(node)));
    }
}
