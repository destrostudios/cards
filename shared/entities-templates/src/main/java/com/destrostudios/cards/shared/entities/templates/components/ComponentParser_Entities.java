package com.destrostudios.cards.shared.entities.templates.components;

import com.destrostudios.cards.shared.entities.templates.*;

public class ComponentParser_Entities extends ComponentParser<Object, Object[], int[]> {

    @Override
    public Object[] parse(TemplateParser parser, TemplateFormat format, Object node) {
        return parseOrCreateChildEntities(parser, format, node, TemplateKeyword.ENTITIES);
    }

    @Override
    public int[] resolve(int[] proxiedEntities, Object[] recordedValue) {
        return resolveEntities(proxiedEntities, recordedValue);
    }
}
