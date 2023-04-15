package com.destrostudios.cards.shared.entities.templates.components;

import com.destrostudios.cards.shared.entities.templates.*;

public class ComponentParser_Entity extends ComponentParser<Object, Object, Integer> {

    @Override
    public Object parse(TemplateParser parser, TemplateFormat format, Object node) {
        return parseOrCreateChildEntity(parser, format, node, 0, TemplateKeyword.ENTITY);
    }

    @Override
    public Integer resolve(int[] proxiedEntities, Object recordedValue) {
        return resolveEntity(proxiedEntities, recordedValue);
    }
}
