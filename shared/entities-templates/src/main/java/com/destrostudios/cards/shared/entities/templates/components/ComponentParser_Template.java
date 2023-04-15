package com.destrostudios.cards.shared.entities.templates.components;

import com.destrostudios.cards.shared.entities.templates.ComponentParser;
import com.destrostudios.cards.shared.entities.templates.Template;
import com.destrostudios.cards.shared.entities.templates.TemplateFormat;
import com.destrostudios.cards.shared.entities.templates.TemplateParser;

public class ComponentParser_Template extends ComponentParser<Object, Template, Template> {

    @Override
    public Template parse(TemplateParser parser, TemplateFormat format, Object node) {
        return parser.parseTemplate(format.getText(node));
    }

    @Override
    public Template resolve(int[] proxiedEntities, Template recordedValue) {
        return resolveTemplate(proxiedEntities, recordedValue);
    }
}
