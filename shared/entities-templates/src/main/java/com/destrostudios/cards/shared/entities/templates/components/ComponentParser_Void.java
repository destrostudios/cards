package com.destrostudios.cards.shared.entities.templates.components;

import com.destrostudios.cards.shared.entities.templates.ComponentParser;
import com.destrostudios.cards.shared.entities.templates.TemplateFormat;
import com.destrostudios.cards.shared.entities.templates.TemplateParser;

public class ComponentParser_Void extends ComponentParser<Object, Void, Void> {

    @Override
    public Void parse(TemplateParser parser, TemplateFormat format, Object node) {
        return null;
    }

    @Override
    public Void resolve(int[] proxiedEntities, Void recordedValue) {
        return null;
    }
}
