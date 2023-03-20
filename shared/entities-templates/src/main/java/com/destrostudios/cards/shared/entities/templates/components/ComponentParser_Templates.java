package com.destrostudios.cards.shared.entities.templates.components;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.entities.templates.ComponentParser;
import com.destrostudios.cards.shared.entities.templates.TemplateFormat;
import com.destrostudios.cards.shared.entities.templates.TemplateParser;

public class ComponentParser_Templates extends ComponentParser<Object, String[]> {

    public ComponentParser_Templates(ComponentDefinition<String[]> component) {
        super(component);
    }

    @Override
    public String[] parseValue(TemplateParser parser, TemplateFormat format, EntityData entityData, Object node) {
        String[] lines = format.getText(node).split("\n");
        String[] values = new String[lines.length];
        for (int i = 0; i < values.length; i++) {
            values[i] = parser.parseTemplateText(entityData, lines[i].trim());
        }
        return values;
    }
}
