package com.destrostudios.cards.shared.application;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.entities.templates.*;
import com.destrostudios.cards.shared.entities.templates.components.*;
import com.destrostudios.cards.shared.entities.templates.formats.*;
import com.destrostudios.cards.shared.files.FileAssets;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.ComponentsTriggers;
import com.destrostudios.cards.shared.rules.CreateLocation;
import com.destrostudios.cards.shared.rules.TargetPrefilter;
import com.destrostudios.cards.shared.rules.cards.Foil;

public class EntityTemplateSetup {

    public static void setup() {
        TemplateManager templateManager = new TemplateManager(
            templateName -> FileAssets.getInputStream("templates/" + templateName + ".xml"),
            new XmlTemplateFormat()
        );

        templateManager.registerComponent(new ComponentParser_String(Components.NAME));
        templateManager.registerComponent(new ComponentParser_Entity(Components.SOURCE));
        templateManager.registerComponent(new ComponentParser_Void(Components.BOARD));
        templateManager.registerComponent(new ComponentParser_Void(Components.CREATURE_CARD));
        templateManager.registerComponent(new ComponentParser_Integer(Components.CREATURE_ZONE));
        templateManager.registerComponent(new ComponentParser_String(Components.FLAVOUR_TEXT));
        templateManager.registerComponent(new ComponentParser_Integer(Components.GRAVEYARD));
        templateManager.registerComponent(new ComponentParser_Integer(Components.HAND));
        templateManager.registerComponent(new ComponentParser_Integer(Components.LIBRARY));
        templateManager.registerComponent(new ComponentParser_Entity(Components.NEXT_PLAYER));
        templateManager.registerComponent(new ComponentParser_Entity(Components.OWNED_BY));
        templateManager.registerComponent(new ComponentParser_Entities(Components.AURAS));
        templateManager.registerComponent(new ComponentParser_Entities(Components.BUFFS));
        templateManager.registerComponent(new ComponentParser_String(Components.CONDITION));
        templateManager.registerComponent(new ComponentParser_Void(Components.SPELL_CARD));
        templateManager.registerComponent(new ComponentParser_Entities(Components.SPELLS));
        templateManager.registerComponent(new ComponentParser_Integer(Components.AVAILABLE_MANA));
        templateManager.registerComponent(new ComponentParser_Integer(Components.MANA));
        templateManager.registerComponent(new ComponentParser_String(Components.DESCRIPTION));
        templateManager.registerComponent(new ComponentParser_Void(Components.LEGENDARY));
        templateManager.registerComponent(new ComponentParser_Enum<>(Components.FOIL, Foil::valueOf));

        templateManager.registerComponent(new ComponentParser_Integer(Components.Cost.MANA_COST));
        templateManager.registerComponent(new ComponentParser_String(Components.Cost.BONUS_MANA_COST));
        templateManager.registerComponent(new ComponentParser_String(Components.Cost.SET_MANA_COST));

        templateManager.registerComponent(new ComponentParser_Integer(Components.Stats.ATTACK));
        templateManager.registerComponent(new ComponentParser_String(Components.Stats.BONUS_ATTACK));
        templateManager.registerComponent(new ComponentParser_String(Components.Stats.SET_ATTACK));
        templateManager.registerComponent(new ComponentParser_Integer(Components.Stats.HEALTH));
        templateManager.registerComponent(new ComponentParser_String(Components.Stats.BONUS_HEALTH));
        templateManager.registerComponent(new ComponentParser_String(Components.Stats.SET_HEALTH));
        templateManager.registerComponent(new ComponentParser_Integer(Components.Stats.DAMAGED));
        templateManager.registerComponent(new ComponentParser_Integer(Components.Stats.BONUS_DAMAGED));

        templateManager.registerComponent(new ComponentParser_Void(Components.Game.ACTIVE_PLAYER));

        templateManager.registerComponent(new ComponentParser_Boolean(Components.Ability.DIVINE_SHIELD));
        templateManager.registerComponent(new ComponentParser_Void(Components.Ability.TAUNT));

        templateManager.registerComponent(new ComponentParser_Entity(Components.Aura.AURA_BUFF));

        templateManager.registerComponent(new ComponentParser_Void(Components.Buff.UNTIL_END_OF_TURN));

        templateManager.registerComponent(new ComponentParser_Void(Components.Tribe.BEAST));
        templateManager.registerComponent(new ComponentParser_Void(Components.Tribe.DRAGON));
        templateManager.registerComponent(new ComponentParser_Void(Components.Tribe.GOBLIN));

        templateManager.registerComponent(new ComponentParser_Entities(Components.Trigger.EFFECTS));

        templateManager.registerComponent(new ComponentParser_String(Components.Effect.REPEAT));
        templateManager.registerComponent(new ComponentParser_String(Components.Effect.DAMAGE));
        templateManager.registerComponent(new ComponentParser_String(Components.Effect.HEAL));
        templateManager.registerComponent(new ComponentParser_String(Components.Effect.DRAW));
        templateManager.registerComponent(new ComponentParser_String(Components.Effect.GAIN_MANA));
        templateManager.registerComponent(new ComponentParser_Void(Components.Effect.DESTROY));
        templateManager.registerComponent(new ComponentParser_Void(Components.Effect.BATTLE));
        templateManager.registerComponent(new ComponentParser<>(Components.Effect.ADD_BUFF) {

            @Override
            public Components.AddBuff parseValue(TemplateParser parser, TemplateFormat format, EntityData entityData, Object node) {
                int buff = createChildEntity(parser, format, entityData, node, 0, "buff");
                boolean constant = "true".equals(format.getAttribute(node, "constant"));
                return new Components.AddBuff(buff, constant);
            }
        });
        templateManager.registerComponent(new ComponentParser<>(Components.Effect.CREATE) {

            @Override
            public Components.Create parseValue(TemplateParser parser, TemplateFormat format, EntityData entityData, Object node) {
                String template = parser.parseTemplateText(entityData, format.getText(node));
                CreateLocation location = CreateLocation.valueOf(parser.parseValue(entityData, format.getAttribute(node, "location")));
                return new Components.Create(template, location);
            }
        });
        templateManager.registerComponent(new ComponentParser_Void(Components.Effect.END_TURN));

        templateManager.registerComponent(new ComponentParser_Void(Components.Effect.Zones.ADD_TO_HAND));
        templateManager.registerComponent(new ComponentParser_Void(Components.Effect.Zones.ADD_TO_BOARD));
        templateManager.registerComponent(new ComponentParser_Void(Components.Effect.Zones.ADD_TO_GRAVEYARD));

        templateManager.registerComponent(new ComponentParser_Enum<>(Components.Target.TARGET_PREFILTER, TargetPrefilter::valueOf));
        templateManager.registerComponent(new ComponentParser_Entities(Components.Target.TARGETS));
        templateManager.registerComponent(new ComponentParser_String(Components.Target.TARGET));
        templateManager.registerComponent(new ComponentParser_String(Components.Target.TARGET_ALL));
        templateManager.registerComponent(new ComponentParser_String(Components.Target.TARGET_RANDOM));

        templateManager.registerComponent(new ComponentParser_Void(Components.Spell.TARGET_OPTIONAL));
        templateManager.registerComponent(new ComponentParser_Integer(Components.Spell.CURRENT_CASTS_PER_TURN));
        templateManager.registerComponent(new ComponentParser_Integer(Components.Spell.MAXIMUM_CASTS_PER_TURN));
        templateManager.registerComponent(new ComponentParser_Void(Components.Spell.TAUNTABLE));
        templateManager.registerComponent(new ComponentParser_Entities(Components.Spell.CAST_TRIGGERS));

        for (ComponentDefinition<int[]> component : ComponentsTriggers.getAllComponents()) {
            templateManager.registerComponent(new ComponentParser_Entities(component));
        }

        EntityTemplate.addLoader(templateManager::loadTemplate);
    }
}
