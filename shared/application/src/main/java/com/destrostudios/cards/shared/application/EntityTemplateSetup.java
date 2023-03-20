package com.destrostudios.cards.shared.application;

import com.destrostudios.cards.shared.entities.templates.EntityTemplate;
import com.destrostudios.cards.shared.entities.templates.TemplateManager;
import com.destrostudios.cards.shared.entities.templates.components.*;
import com.destrostudios.cards.shared.entities.templates.xml.*;
import com.destrostudios.cards.shared.files.FileAssets;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.TargetPrefilter;
import com.destrostudios.cards.shared.rules.cards.Foil;

public class EntityTemplateSetup {

    public static void setup() {
        TemplateManager templateManager = new TemplateManager(
            templateName -> FileAssets.getInputStream("templates/" + templateName + ".xml"),
            new XmlTemplateFormat()
        );

        templateManager.registerComponent(new ComponentParser_String(Components.NAME));
        templateManager.registerComponent(new ComponentParser_Void(Components.BOARD));
        templateManager.registerComponent(new ComponentParser_Void(Components.CREATURE_CARD));
        templateManager.registerComponent(new ComponentParser_Integer(Components.CREATURE_ZONE));
        templateManager.registerComponent(new ComponentParser_String(Components.FLAVOUR_TEXT));
        templateManager.registerComponent(new ComponentParser_Integer(Components.GRAVEYARD));
        templateManager.registerComponent(new ComponentParser_Integer(Components.HAND));
        templateManager.registerComponent(new ComponentParser_Integer(Components.SPELL_ZONE));
        templateManager.registerComponent(new ComponentParser_Integer(Components.LIBRARY));
        templateManager.registerComponent(new ComponentParser_Entity(Components.NEXT_PLAYER));
        templateManager.registerComponent(new ComponentParser_Entity(Components.OWNED_BY));
        templateManager.registerComponent(new ComponentParser_Entities(Components.AURAS));
        templateManager.registerComponent(new ComponentParser_Entities(Components.BUFFS));
        templateManager.registerComponent(new ComponentParser_Entities(Components.CONDITIONS));
        templateManager.registerComponent(new ComponentParser_Void(Components.SPELL_CARD));
        templateManager.registerComponent(new ComponentParser_Entities(Components.SPELLS));
        templateManager.registerComponent(new ComponentParser_Integer(Components.AVAILABLE_MANA));
        templateManager.registerComponent(new ComponentParser_Integer(Components.MANA));
        templateManager.registerComponent(new ComponentParser_String(Components.DESCRIPTION));
        templateManager.registerComponent(new ComponentParser_Enum<>(Components.FOIL, Foil::valueOf));
        templateManager.registerComponent(new ComponentParser_Entities(Components.DEATH_EFFECT_TRIGGERS));

        templateManager.registerComponent(new ComponentParser_Integer(Components.Cost.MANA_COST));
        templateManager.registerComponent(new ComponentParser_Integer(Components.Cost.BONUS_MANA_COST));

        templateManager.registerComponent(new ComponentParser_Integer(Components.Stats.ATTACK));
        templateManager.registerComponent(new ComponentParser_Integer(Components.Stats.BONUS_ATTACK));
        templateManager.registerComponent(new ComponentParser_Integer(Components.Stats.HEALTH));
        templateManager.registerComponent(new ComponentParser_Integer(Components.Stats.BONUS_HEALTH));
        templateManager.registerComponent(new ComponentParser_Integer(Components.Stats.DAMAGED));
        templateManager.registerComponent(new ComponentParser_Integer(Components.Stats.BONUS_DAMAGED));

        templateManager.registerComponent(new ComponentParser_Void(Components.Game.ACTIVE_PLAYER));

        templateManager.registerComponent(new ComponentParser_Void(Components.Ability.SLOW));
        templateManager.registerComponent(new ComponentParser_Boolean(Components.Ability.DIVINE_SHIELD));
        templateManager.registerComponent(new ComponentParser_Void(Components.Ability.HEXPROOF));
        templateManager.registerComponent(new ComponentParser_Void(Components.Ability.IMMUNE));
        templateManager.registerComponent(new ComponentParser_Void(Components.Ability.TAUNT));

        templateManager.registerComponent(new ComponentParser_Entity(Components.Aura.AURA_BUFF));

        templateManager.registerComponent(new ComponentParser_Void(Components.Tribe.BEAST));
        templateManager.registerComponent(new ComponentParser_Void(Components.Tribe.DRAGON));
        templateManager.registerComponent(new ComponentParser_Void(Components.Tribe.FISH));
        templateManager.registerComponent(new ComponentParser_Void(Components.Tribe.GOD));
        templateManager.registerComponent(new ComponentParser_Void(Components.Tribe.HUMAN));

        templateManager.registerComponent(new ComponentParser_Entities(Components.EffectTrigger.EFFECTS));

        templateManager.registerComponent(new ComponentParser_Integer(Components.Effect.DAMAGE));
        templateManager.registerComponent(new ComponentParser_Integer(Components.Effect.HEAL));
        templateManager.registerComponent(new ComponentParser_Void(Components.Effect.BATTLE));
        templateManager.registerComponent(new ComponentParser_Integer(Components.Effect.DRAW));
        templateManager.registerComponent(new ComponentParser_Integer(Components.Effect.GAIN_MANA));
        templateManager.registerComponent(new ComponentParser_Void(Components.Effect.DESTROY));
        templateManager.registerComponent(new ComponentParser_Entities(Components.Effect.ADD_AURAS));
        templateManager.registerComponent(new ComponentParser_Entities(Components.Effect.REMOVE_AURAS));
        templateManager.registerComponent(new ComponentParser_Entities(Components.Effect.ADD_BUFFS));
        templateManager.registerComponent(new ComponentParser_Entities(Components.Effect.REMOVE_BUFFS));
        templateManager.registerComponent(new ComponentParser_Templates(Components.Effect.SUMMON));

        templateManager.registerComponent(new ComponentParser_Void(Components.Effect.Zones.ADD_TO_HAND));
        templateManager.registerComponent(new ComponentParser_Void(Components.Effect.Zones.ADD_TO_BOARD));
        templateManager.registerComponent(new ComponentParser_Void(Components.Effect.Zones.ADD_TO_GRAVEYARD));

        templateManager.registerComponent(new ComponentParser_Enum<>(Components.Target.TARGET_PREFILTER, TargetPrefilter::valueOf));
        templateManager.registerComponent(new ComponentParser_Entities(Components.Target.TARGET_CHAINS));
        templateManager.registerComponent(new ComponentParser_Entities(Components.Target.TARGET_CHAIN));
        templateManager.registerComponent(new ComponentParser_Void(Components.Target.TARGET_SOURCE));
        templateManager.registerComponent(new ComponentParser_Void(Components.Target.TARGET_TARGETS));
        templateManager.registerComponent(new ComponentParser_Entities(Components.Target.TARGET_CUSTOM));
        templateManager.registerComponent(new ComponentParser_Entities(Components.Target.TARGET_ALL));
        templateManager.registerComponent(new ComponentParser_Void(Components.Target.TARGET_OWNER));
        templateManager.registerComponent(new ComponentParser_Void(Components.Target.TARGET_OPPONENT));
        templateManager.registerComponent(new ComponentParser_Void(Components.Target.TARGET_CASTER));
        templateManager.registerComponent(new ComponentParser_Integer(Components.Target.TARGET_RANDOM));

        templateManager.registerComponent(new ComponentParser_Entities(Components.Condition.ONE_OF));
        templateManager.registerComponent(new ComponentParser_Void(Components.Condition.NOT));
        templateManager.registerComponent(new ComponentParser_Void(Components.Condition.ALLY));
        templateManager.registerComponent(new ComponentParser_Void(Components.Condition.OPPONENT));
        templateManager.registerComponent(new ComponentParser_Void(Components.Condition.PLAYER));
        templateManager.registerComponent(new ComponentParser_Void(Components.Condition.IN_LIBRARY));
        templateManager.registerComponent(new ComponentParser_Void(Components.Condition.IN_HAND));
        templateManager.registerComponent(new ComponentParser_Void(Components.Condition.ON_BOARD));
        templateManager.registerComponent(new ComponentParser_Void(Components.Condition.IN_GRAVEYARD));
        templateManager.registerComponent(new ComponentParser_Integer(Components.Condition.MINIMUM_MANA_COST));
        templateManager.registerComponent(new ComponentParser_Integer(Components.Condition.MAXIMUM_MANA_COST));
        templateManager.registerComponent(new ComponentParser_Void(Components.Condition.NO_CREATURES));
        templateManager.registerComponent(new ComponentParser_Void(Components.Condition.DEFAULT_CAST_FROM_HAND_SPELL));

        templateManager.registerComponent(new ComponentParser_Void(Components.Spell.TARGET_OPTIONAL));
        templateManager.registerComponent(new ComponentParser_Integer(Components.Spell.CURRENT_CASTS_PER_TURN));
        templateManager.registerComponent(new ComponentParser_Integer(Components.Spell.MAXIMUM_CASTS_PER_TURN));
        templateManager.registerComponent(new ComponentParser_Void(Components.Spell.TAUNTABLE));
        templateManager.registerComponent(new ComponentParser_Entities(Components.Spell.INSTANT_EFFECT_TRIGGERS));

        EntityTemplate.addLoader(templateManager::loadTemplate);
    }
}
