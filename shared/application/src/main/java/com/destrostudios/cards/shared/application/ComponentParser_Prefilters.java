package com.destrostudios.cards.shared.application;

import com.destrostudios.cards.shared.entities.templates.ComponentParser;
import com.destrostudios.cards.shared.entities.templates.TemplateFormat;
import com.destrostudios.cards.shared.entities.templates.TemplateParser;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.ComponentsParsing;

public class ComponentParser_Prefilters extends ComponentParser<Object, Components.Prefilters, Components.Prefilters> {

    @Override
    public Components.Prefilters parse(TemplateParser parser, TemplateFormat format, Object node) {
        String basicText = format.getAttribute(node, "basic");
        String advancedText = format.getAttribute(node, "advanced");
        String[] basicNames = ((basicText != null) ? basicText.split(",") : new String[0]);
        String[] advancedNames = ((advancedText != null) ? advancedText.split(",") : new String[0]);
        return ComponentsParsing.parsePrefilters(basicNames, advancedNames);
    }

    @Override
    public Components.Prefilters resolve(int[] proxiedEntities, Components.Prefilters recordedValue) {
        return recordedValue;
    }
}
