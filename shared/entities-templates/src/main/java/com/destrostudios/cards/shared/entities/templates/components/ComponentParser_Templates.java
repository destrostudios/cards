package com.destrostudios.cards.shared.entities.templates.components;

import com.destrostudios.cards.shared.entities.templates.ComponentParser;
import com.destrostudios.cards.shared.entities.templates.Template;
import com.destrostudios.cards.shared.entities.templates.TemplateFormat;
import com.destrostudios.cards.shared.entities.templates.TemplateParser;

public class ComponentParser_Templates extends ComponentParser<Object, Template[], Template[]> {

    @Override
    public Template[] parse(TemplateParser parser, TemplateFormat format, Object node) {
        String[] lines = format.getText(node).split("\n");
        Template[] templates = new Template[lines.length];
        for (int i = 0; i < templates.length; i++) {
            templates[i] = parser.parseTemplate(lines[i].trim());
        }
        return templates;
    }

    @Override
    public Template[] resolve(int[] proxiedEntities, Template[] recordedValue) {
        return resolveTemplates(proxiedEntities, recordedValue);
    }
}
