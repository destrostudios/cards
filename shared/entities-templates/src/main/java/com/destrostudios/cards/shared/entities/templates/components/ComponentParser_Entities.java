package com.destrostudios.cards.shared.entities.templates.components;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.entities.templates.ComponentParser;
import com.destrostudios.cards.shared.entities.templates.TemplateFormat;
import com.destrostudios.cards.shared.entities.templates.TemplateParser;

public class ComponentParser_Entities extends ComponentParser<Object, int[]> {

    public ComponentParser_Entities(ComponentDefinition<int[]> component) {
        super(component);
    }

    @Override
    public int[] parseValue(TemplateParser parser, TemplateFormat format, EntityData entityData, Object node) {
        return createChildEntities(parser, format, entityData, node, 0, "entities");
    }
}
