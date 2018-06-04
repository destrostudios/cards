package com.destrostudios.cards.shared.application;

import com.destrostudios.cards.shared.entities.templates.EntityTemplate;
import com.destrostudios.cards.shared.entities.templates.XMLComponentParser;
import com.destrostudios.cards.shared.entities.templates.XMLTemplateManager;
import com.destrostudios.cards.shared.entities.templates.xmlparser.*;
import com.destrostudios.cards.shared.files.FileAssets;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.game.phases.TurnPhase;

public class EntityTemplateSetup {

    public static void setup() {
        XMLTemplateManager xmlTemplateManager = new XMLTemplateManager(templateName -> FileAssets.getInputStream("templates/" + templateName + ".xml"));

        xmlTemplateManager.registerComponent(new XMLComponentParser_Integer(Components.ATTACK));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.BOARD));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.CREATURE_CARD));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Integer(Components.CREATURE_ZONE));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.DAMAGED));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Integer(Components.DECLARED_ATTACK));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Integer(Components.DECLARED_BLOCK));
        xmlTemplateManager.registerComponent(new XMLComponentParser_String(Components.DISPLAY_NAME));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.ENCHANTMENT_CARD));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Integer(Components.ENCHANTMENT_ZONE));
        xmlTemplateManager.registerComponent(new XMLComponentParser_String(Components.FLAVOUR_TEXT));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Integer(Components.GRAVEYARD));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Integer(Components.HAND_CARDS));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Integer(Components.HEALTH));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.LAND_CARD));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Integer(Components.LAND_ZONE));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Integer(Components.LIBRARY));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Integer(Components.LAND_ZONE));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Entity(Components.NEXT_PLAYER));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Entity(Components.OWNED_BY));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.SPELL_CARD));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Integer(Components.SPELL_ZONE));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Entities(Components.SPELL_ENTITIES));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.TAPPED));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.Ability.CHARGE));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.Ability.DIVINE_SHIELD));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.Ability.FLYING));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.Ability.HEXPROOF));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.Ability.IMMUNE));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.Ability.TAUNT));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.Ability.VIGILANCE));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.Color.NEUTRAL));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.Color.WHITE));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.Color.RED));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.Color.GREEN));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.Color.BLUE));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.Color.BLACK));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.Cost.TAP));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.Tribe.BEAST));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.Tribe.DRAGON));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.Tribe.FISH));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.Tribe.GOD));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.Tribe.HUMAN));
        xmlTemplateManager.registerComponent(new XMLComponentParser<TurnPhase>(Components.Game.TURN_PHASE) {

            @Override
            public TurnPhase parseValue() {
                return TurnPhase.valueOf(element.getText());
            }
        });
        xmlTemplateManager.registerComponent(new XMLComponentParser_Integer(Components.ManaAmount.NEUTRAL));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Integer(Components.ManaAmount.WHITE));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Integer(Components.ManaAmount.RED));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Integer(Components.ManaAmount.GREEN));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Integer(Components.ManaAmount.BLUE));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Integer(Components.ManaAmount.BLACK));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Entity(Components.Spell.COST_ENTITY));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Entity(Components.Spell.TARGET_RULE));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.Spell.CastCondition.ATTACK_PHASE));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.Spell.CastCondition.BLOCK_PHASE));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.Spell.CastCondition.FROM_BOARD));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.Spell.CastCondition.FROM_HAND));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.Spell.CastCondition.MAIN_PHASE));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.Spell.Effect.ADD_TO_BOARD));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Integer(Components.Spell.Effect.DAMAGE));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.Spell.Effect.TAP));
        xmlTemplateManager.registerComponent(new XMLComponentParser_Void(Components.Spell.Effect.UNTAP));

        EntityTemplate.addLoader((entityData, entity, templateName, parameters) -> xmlTemplateManager.loadTemplate(entityData, entity, templateName, parameters));
    }
}
