package com.destrostudios.cards.shared.entities.templates.components;

import com.destrostudios.cards.shared.entities.templates.ComponentParser;
import com.destrostudios.cards.shared.entities.templates.TemplateFormat;
import com.destrostudios.cards.shared.entities.templates.TemplateParser;

public class ComponentParser_String extends ComponentParser<Object, String, String> {

    @Override
    public String parse(TemplateParser parser, TemplateFormat format, Object node) {
        return parser.parseText(format.getText(node));
    }

    @Override
    public String resolve(int[] proxiedEntities, String recordedValue) {
        return recordedValue;
    }
}
