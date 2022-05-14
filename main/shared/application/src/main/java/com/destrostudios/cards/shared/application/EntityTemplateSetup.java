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

        xmlTemplateManager.registerComponent(new XMLComponentParser_Integer(Components.ATTACK));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.BOARD));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.CREATURE_CARD));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Integer(Components.CREATURE_ZONE));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.DAMAGED));
        xmlTemplateManager.registerComponent(new XMLComponentParser_String(Components.DISPLAY_NAME));
        xmlTemplateManager.registerComponent(new XMLComponentParser_String(Components.FLAVOUR_TEXT));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.HAS_ATTACKED));
        xmlTemplateManager.registerComponent(new XMLComponentParser<Foil>(Components.FOIL) {

            @Override
            public Foil parseValue() {
                return Foil.valueOf(element.getText());
            }
        });
        xmlTemplateManager.registerComponent(new XMLComponentParser_Integer(Components.GRAVEYARD));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Integer(Components.HAND_CARDS));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Integer(Components.HEALTH));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Integer(Components.SPELL_ZONE));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Integer(Components.LIBRARY));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Integer(Components.SPELL_ZONE));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Entity(Components.NEXT_PLAYER));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Entity(Components.OWNED_BY));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.SPELL_CARD));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Integer(Components.SPELL_ZONE));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Entities(Components.SPELL_ENTITIES));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Integer(Components.MANA));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.Ability.SLOW));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.Ability.DIVINE_SHIELD));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.Ability.HEXPROOF));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.Ability.IMMUNE));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.Ability.TAUNT));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.Color.NEUTRAL));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.Color.WHITE));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.Color.RED));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.Color.GREEN));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.Color.BLUE));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.Color.BLACK));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.Tribe.BEAST));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.Tribe.DRAGON));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.Tribe.FISH));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.Tribe.GOD));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.Tribe.HUMAN));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.Game.ACTIVE_PLAYER));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Entity(Components.Spell.COST_ENTITY));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Entity(Components.Spell.TARGET_RULE));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Entity(Components.Spell.SOURCE_EFFECT));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Entity(Components.Spell.TARGET_EFFECT));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.Spell.CastCondition.FROM_BOARD));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.Spell.CastCondition.FROM_HAND));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Integer(Components.Spell.Effect.DAMAGE));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Integer(Components.Spell.Effect.GAIN_MANA));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.Spell.Effect.Zones.ADD_TO_BOARD));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.Spell.Effect.Zones.ADD_TO_GRAVEYARD));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.Spell.TargetRules.ALLY));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.Spell.TargetRules.OPPONENT));

        EntityTemplate.addLoader((entityData, entity, templateName, parameters) -> xmlTemplateManager.loadTemplate(entityData, entity, templateName, parameters));
    }
}
