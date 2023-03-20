package com.destrostudios.cards.shared.entities.templates.components;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.entities.templates.ComponentParser;
import com.destrostudios.cards.shared.entities.templates.TemplateFormat;
import com.destrostudios.cards.shared.entities.templates.TemplateParser;

public class ComponentParser_Void extends ComponentParser<Object, Void> {

    public ComponentParser_Void(ComponentDefinition<Void> component) {
        super(component);
    }

    @Override
    public Void parseValue(TemplateParser parser, TemplateFormat format, EntityData entityData, Object node) {
        return null;
    }
}
