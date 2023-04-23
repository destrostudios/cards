package com.destrostudios.cards.shared.entities.templates.components;

import com.destrostudios.cards.shared.entities.templates.ComponentParser;
import com.destrostudios.cards.shared.entities.templates.TemplateFormat;
import com.destrostudios.cards.shared.entities.templates.TemplateParser;

public class ComponentParser_StringArray extends ComponentParser<Object, String[], String[]> {

    @Override
    public String[] parse(TemplateParser parser, TemplateFormat format, Object node) {
        String[] unparsed = format.getText(node).split(",");
        String[] parsed = new String[unparsed.length];
        for (int i = 0; i < parsed.length; i++) {
            parsed[i] = parser.parseText(unparsed[i]);
        }
        return parsed;
    }

    @Override
    public String[] resolve(int[] proxiedEntities, String[] recordedValue) {
        return recordedValue;
    }
}
