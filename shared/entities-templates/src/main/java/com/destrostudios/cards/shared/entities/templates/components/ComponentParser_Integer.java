package com.destrostudios.cards.shared.entities.templates.components;

import com.destrostudios.cards.shared.entities.templates.ComponentParser;
import com.destrostudios.cards.shared.entities.templates.TemplateFormat;
import com.destrostudios.cards.shared.entities.templates.TemplateParser;

public class ComponentParser_Integer extends ComponentParser<Object, Integer, Integer> {

    @Override
    public Integer parse(TemplateParser parser, TemplateFormat format, Object node) {
        return Integer.parseInt(parser.parseText(format.getText(node)));
    }

    @Override
    public Integer resolve(int[] proxiedEntities, Integer recordedValue) {
        return recordedValue;
    }
}
