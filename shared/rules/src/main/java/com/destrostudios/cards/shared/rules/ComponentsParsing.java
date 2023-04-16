package com.destrostudios.cards.shared.rules;

import com.destrostudios.cards.shared.entities.ComponentDefinition;

import java.util.HashMap;

public class ComponentsParsing {

    private static HashMap<String, ComponentDefinition> BASIC_COMPONENTS = new HashMap<>();
    static {
        BASIC_COMPONENTS.put("BOARD", Components.BOARD);
        BASIC_COMPONENTS.put("CREATURE_ZONE", Components.Zone.CREATURE_ZONE);
        BASIC_COMPONENTS.put("GRAVEYARD", Components.Zone.GRAVEYARD);
        BASIC_COMPONENTS.put("HAND", Components.Zone.HAND);
        BASIC_COMPONENTS.put("LIBRARY", Components.Zone.LIBRARY);
        BASIC_COMPONENTS.put("CREATURE_CARD", Components.CREATURE_CARD);
        BASIC_COMPONENTS.put("SPELL_CARD", Components.SPELL_CARD);
        BASIC_COMPONENTS.put("BEAST", Components.Tribe.BEAST);
        BASIC_COMPONENTS.put("DRAGON", Components.Tribe.DRAGON);
        BASIC_COMPONENTS.put("GOBLIN", Components.Tribe.GOBLIN);
    }

    public static Components.Prefilters parsePrefilters(String[] basicNames, String[] advancedNames) {
        ComponentDefinition<?>[] basic = new ComponentDefinition[basicNames.length];
        for (int i = 0; i < basic.length; i++) {
            basic[i] = BASIC_COMPONENTS.get(basicNames[i]);
        }
        Prefilter_Advanced[] advanced = new Prefilter_Advanced[advancedNames.length];
        for (int i = 0; i < advanced.length; i++) {
            advanced[i] = Prefilter_Advanced.valueOf(advancedNames[i]);
        }
        return new Components.Prefilters(basic, advanced);
    }
}
