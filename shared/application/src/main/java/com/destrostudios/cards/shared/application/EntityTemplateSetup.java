package com.destrostudios.cards.shared.application;

import com.destrostudios.cards.shared.entities.templates.EntityTemplate;
import com.destrostudios.cards.shared.entities.templates.XMLComponentParser;
import com.destrostudios.cards.shared.entities.templates.XMLTemplateManager;
import com.destrostudios.cards.shared.entities.templates.xmlparser.*;
import com.destrostudios.cards.shared.files.FileAssets;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.cards.Foil;

public class EntityTemplateSetup {

    public static void setup() {
        XMLTemplateManager xmlTemplateManager = new XMLTemplateManager(templateName -> FileAssets.getInputStream("templates/" + templateName + ".xml"));

        xmlTemplateManager.registerComponent(new XMLComponentParser_String(Components.NAME));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.TARGETABLE));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.BOARD));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.CREATURE_CARD));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Integer(Components.CREATURE_ZONE));
        xmlTemplateManager.registerComponent(new XMLComponentParser_String(Components.FLAVOUR_TEXT));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Integer(Components.GRAVEYARD));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Integer(Components.HAND));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Integer(Components.SPELL_ZONE));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Integer(Components.LIBRARY));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Entity(Components.NEXT_PLAYER));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Entity(Components.OWNED_BY));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Entities(Components.AURAS));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Entities(Components.BUFFS));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Entities(Components.CONDITIONS));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.SPELL_CARD));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Entities(Components.SPELLS));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Integer(Components.AVAILABLE_MANA));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Integer(Components.MANA));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Entity(Components.COST));
        xmlTemplateManager.registerComponent(new XMLComponentParser_String(Components.DESCRIPTION));
        xmlTemplateManager.registerComponent(new XMLComponentParser<>(Components.FOIL) {

            @Override
            public Foil parseValue() {
                return Foil.valueOf(element.getText());
            }
        });
        xmlTemplateManager.registerComponent(new XMLComponentParser_Entities(Components.DEATH_EFFECT_TRIGGERS));

        xmlTemplateManager.registerComponent(new XMLComponentParser_Integer(Components.Stats.ATTACK));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Integer(Components.Stats.BONUS_ATTACK));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Integer(Components.Stats.HEALTH));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Integer(Components.Stats.BONUS_HEALTH));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Integer(Components.Stats.DAMAGED));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Integer(Components.Stats.BONUS_DAMAGED));

        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.Game.ACTIVE_PLAYER));

        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.Ability.SLOW));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Boolean(Components.Ability.DIVINE_SHIELD));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.Ability.HEXPROOF));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.Ability.IMMUNE));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.Ability.TAUNT));

        xmlTemplateManager.registerComponent(new XMLComponentParser_Entity(Components.Aura.AURA_BUFF));

        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.Tribe.BEAST));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.Tribe.DRAGON));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.Tribe.FISH));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.Tribe.GOD));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.Tribe.HUMAN));

        xmlTemplateManager.registerComponent(new XMLComponentParser_Entities(Components.EffectTrigger.EFFECTS));

        xmlTemplateManager.registerComponent(new XMLComponentParser_Integer(Components.Effect.DAMAGE));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Integer(Components.Effect.HEAL));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.Effect.BATTLE));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Integer(Components.Effect.DRAW));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Integer(Components.Effect.GAIN_MANA));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.Effect.DESTROY));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Entities(Components.Effect.ADD_AURAS));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Entities(Components.Effect.REMOVE_AURAS));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Entities(Components.Effect.ADD_BUFFS));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Entities(Components.Effect.REMOVE_BUFFS));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Templates(Components.Effect.SUMMON));

        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.Effect.Zones.ADD_TO_HAND));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.Effect.Zones.ADD_TO_BOARD));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.Effect.Zones.ADD_TO_GRAVEYARD));

        xmlTemplateManager.registerComponent(new XMLComponentParser_Entities(Components.Target.TARGET_CHAINS));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Entities(Components.Target.TARGET_CHAIN));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.Target.TARGET_SOURCE));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.Target.TARGET_TARGETS));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Entities(Components.Target.TARGET_CUSTOM));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Entities(Components.Target.TARGET_ALL));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.Target.TARGET_OWNER));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.Target.TARGET_OPPONENT));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Integer(Components.Target.TARGET_RANDOM));

        xmlTemplateManager.registerComponent(new XMLComponentParser_Entities(Components.Condition.ONE_OF));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.Condition.NOT));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.Condition.ALLY));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.Condition.OPPONENT));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.Condition.PLAYER));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.Condition.IN_LIBRARY));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.Condition.IN_HAND));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.Condition.ON_BOARD));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.Condition.IN_GRAVEYARD));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Integer(Components.Condition.MINIMUM_MANA_COST));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Integer(Components.Condition.MAXIMUM_MANA_COST));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.Condition.NO_CREATURES));

        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.Spell.TARGET_OPTIONAL));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Integer(Components.Spell.CURRENT_CASTS_PER_TURN));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Integer(Components.Spell.MAXIMUM_CASTS_PER_TURN));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.Spell.TAUNTABLE));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Entities(Components.Spell.INSTANT_EFFECT_TRIGGERS));

        EntityTemplate.addLoader(xmlTemplateManager::loadTemplate);
    }
}
