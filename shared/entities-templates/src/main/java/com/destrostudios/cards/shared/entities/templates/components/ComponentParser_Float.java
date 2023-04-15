package com.destrostudios.cards.shared.entities.templates.components;

import com.destrostudios.cards.shared.entities.templates.ComponentParser;
import com.destrostudios.cards.shared.entities.templates.TemplateFormat;
import com.destrostudios.cards.shared.entities.templates.TemplateParser;

public class ComponentParser_Float extends ComponentParser<Object, Float, Float> {

    @Override
    public Float parse(TemplateParser parser, TemplateFormat format, Object node) {
        return Float.parseFloat(parser.parseText(format.getText(node)));
    }

    @Override
    public Float resolve(int[] proxiedEntities, Float recordedValue) {
        return recordedValue;
    }
}
