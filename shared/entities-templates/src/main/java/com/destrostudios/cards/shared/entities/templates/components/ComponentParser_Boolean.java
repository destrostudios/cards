package com.destrostudios.cards.shared.entities.templates.components;

import com.destrostudios.cards.shared.entities.templates.ComponentParser;
import com.destrostudios.cards.shared.entities.templates.TemplateFormat;
import com.destrostudios.cards.shared.entities.templates.TemplateParser;

public class ComponentParser_Boolean extends ComponentParser<Object, Boolean, Boolean> {

    @Override
    public Boolean parse(TemplateParser parser, TemplateFormat format, Object node) {
        return parser.parseBoolean(format.getText(node));
    }

    @Override
    public Boolean resolve(int[] proxiedEntities, Boolean recordedValue) {
        return recordedValue;
    }
}
