package com.destrostudios.cards.shared.application;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.entities.templates.ComponentParser;
import com.destrostudios.cards.shared.entities.templates.TemplateFormat;
import com.destrostudios.cards.shared.entities.templates.TemplateParser;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.ComponentsParsing;

public class ComponentParser_Prefilters extends ComponentParser<Object, Components.Prefilters> {

    public ComponentParser_Prefilters(ComponentDefinition<Components.Prefilters> component) {
        super(component);
    }

    @Override
    public Components.Prefilters parseValue(TemplateParser parser, TemplateFormat format, EntityData entityData, Object node) {
        String basicText = format.getAttribute(node, "basic");
        String advancedText = format.getAttribute(node, "advanced");
        String[] basicNames = ((basicText != null) ? basicText.split(",") : new String[0]);
        String[] advancedNames = ((advancedText != null) ? advancedText.split(",") : new String[0]);
        return ComponentsParsing.parsePrefilters(basicNames, advancedNames);
    }
}
