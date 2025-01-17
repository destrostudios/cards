package com.destrostudios.cards.shared.application;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.entities.templates.*;
import com.destrostudios.cards.shared.entities.templates.components.*;
import com.destrostudios.cards.shared.entities.templates.formats.*;
import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.files.FileAssets;
import com.destrostudios.cards.shared.rules.*;
import com.destrostudios.cards.shared.rules.cards.Foil;
import com.destrostudios.cards.shared.rules.effects.DiscoverPool;
import com.destrostudios.cards.shared.rules.game.turn.EndTurnEvent;

public class EntityTemplateSetup {

    public static void setup() {
        TemplateManager templateManager = new TemplateManager(
            templateName -> FileAssets.getInputStream("templates/" + templateName + ".xml"),
            new XmlTemplateFormat(),
            new CardsComponentsInfo()
        );

        templateManager.registerComponent(Components.NAME, new ComponentParser_String());
        templateManager.registerComponent(Components.SOURCE, new ComponentParser_Entity());
        templateManager.registerComponent(Components.BOARD, new ComponentParser_Void());
        templateManager.registerComponent(Components.CREATURE_CARD, new ComponentParser_Void());
        templateManager.registerComponent(Components.FLAVOUR_TEXT, new ComponentParser_String());
        templateManager.registerComponent(Components.NEXT_PLAYER, new ComponentParser_Entity());
        // Components.OWNED_BY
        templateManager.registerComponent(Components.AURAS, new ComponentParser_Entities());
        // Components.BUFFS
        templateManager.registerComponent(Components.CONDITION, new ComponentParser_String());
        templateManager.registerComponent(Components.SPELL_CARD, new ComponentParser_Void());
        templateManager.registerComponent(Components.SPELLS, new ComponentParser_Entities());
        templateManager.registerComponent(Components.AVAILABLE_MANA, new ComponentParser_Integer());
        templateManager.registerComponent(Components.MANA, new ComponentParser_Integer());
        templateManager.registerComponent(Components.DESCRIPTION, new ComponentParser_String());
        templateManager.registerComponent(Components.LEGENDARY, new ComponentParser_Void());
        templateManager.registerComponent(Components.FOIL, new ComponentParser_Enum<>(Foil::valueOf));

        // Components.Zone.CREATURE_ZONE
        // Components.Zone.GRAVEYARD
        // Components.Zone.HAND
        // Components.Zone.LIBRARY
        // Components.Zone.PLAYER_LIBRARY
        // Components.Zone.PLAYER_HAND
        // Components.Zone.PLAYER_CREATURE_ZONE
        // Components.Zone.PLAYER_GRAVEYARD

        templateManager.registerComponent(Components.Cost.MANA_COST, new ComponentParser_Integer());
        templateManager.registerComponent(Components.Cost.BONUS_MANA_COST, new ComponentParser_String());
        templateManager.registerComponent(Components.Cost.SET_MANA_COST, new ComponentParser_String());

        templateManager.registerComponent(Components.Stats.ATTACK, new ComponentParser_Integer());
        templateManager.registerComponent(Components.Stats.BONUS_ATTACK, new ComponentParser_String());
        templateManager.registerComponent(Components.Stats.SET_ATTACK, new ComponentParser_String());
        templateManager.registerComponent(Components.Stats.HEALTH, new ComponentParser_Integer());
        templateManager.registerComponent(Components.Stats.BONUS_HEALTH, new ComponentParser_String());
        templateManager.registerComponent(Components.Stats.SET_HEALTH, new ComponentParser_String());
        templateManager.registerComponent(Components.Stats.DAMAGED, new ComponentParser_Integer());
        templateManager.registerComponent(Components.Stats.BONUS_DAMAGED, new ComponentParser_Integer());

        templateManager.registerComponent(Components.Player.MULLIGAN, new ComponentParser_Void());
        templateManager.registerComponent(Components.Player.ACTIVE_PLAYER, new ComponentParser_Void());
        // Components.Player.LIBRARY_CARDS
        // Components.Player.HAND_CARDS
        // Components.Player.CREATURE_ZONE_CARDS
        // Components.Player.GRAVEYARD_CARDS

        templateManager.registerComponent(Components.Ability.DIVINE_SHIELD, new ComponentParser_Boolean());
        templateManager.registerComponent(Components.Ability.TAUNT, new ComponentParser_Void());

        templateManager.registerComponent(Components.Aura.AURA_BUFF, new ComponentParser_Entity());

        templateManager.registerComponent(Components.Tribe.BEAST, new ComponentParser_Void());
        templateManager.registerComponent(Components.Tribe.DRAGON, new ComponentParser_Void());
        templateManager.registerComponent(Components.Tribe.GOBLIN, new ComponentParser_Void());
        templateManager.registerComponent(Components.Tribe.MACHINE, new ComponentParser_Void());

        templateManager.registerComponent(Components.Trigger.EFFECTS, new ComponentParser_Entities());

        templateManager.registerComponent(Components.Effect.REPEAT, new ComponentParser_String());
        templateManager.registerComponent(Components.Effect.DAMAGE, new ComponentParser_String());
        templateManager.registerComponent(Components.Effect.HEAL, new ComponentParser_String());
        templateManager.registerComponent(Components.Effect.DRAW, new ComponentParser_String());
        templateManager.registerComponent(Components.Effect.DISCARD, new ComponentParser_Void());
        templateManager.registerComponent(Components.Effect.GAIN_MANA, new ComponentParser_String());
        templateManager.registerComponent(Components.Effect.DESTROY, new ComponentParser_Void());
        templateManager.registerComponent(Components.Effect.CAST_ATTACK, new ComponentParser_Void());
        templateManager.registerComponent(Components.Effect.BATTLE, new ComponentParser_Void());
        record AddBuffProxy(Object buff, boolean constant) {}
        templateManager.registerComponent(Components.Effect.ADD_BUFF, new ComponentParser<Object, AddBuffProxy, Components.AddBuff>() {

            @Override
            public AddBuffProxy parse(TemplateParser parser, TemplateFormat format, Object node) {
                Object buff = parseOrCreateChildEntity(parser, format, node, 0, "buff");
                boolean constant = "true".equals(parser.parseTextNullable(format.getAttribute(node, "constant")));
                return new AddBuffProxy(buff, constant);
            }

            @Override
            public Components.AddBuff resolve(int[] proxiedEntities, AddBuffProxy recordedValue) {
                return new Components.AddBuff(resolveEntity(proxiedEntities, recordedValue.buff), recordedValue.constant);
            }
        });
        templateManager.registerComponent(Components.Effect.REMOVE_BUFF, new ComponentParser_Entity());
        record CreateProxy(Template template, CreateLocation location) {}
        templateManager.registerComponent(Components.Effect.CREATE, new ComponentParser<Object, CreateProxy, Components.Create>() {

            @Override
            public CreateProxy parse(TemplateParser parser, TemplateFormat format, Object node) {
                Template template = parser.parseTemplate(format.getText(node));
                CreateLocation location = CreateLocation.valueOf(parser.parseText(format.getAttribute(node, "location")));
                return new CreateProxy(template, location);
            }

            @Override
            public Components.Create resolve(int[] proxiedEntities, CreateProxy recordedValue) {
                return new Components.Create(resolveTemplate(proxiedEntities, recordedValue.template).getAsResolvedText(), recordedValue.location);
            }
        });
        record DiscoverProxy(DiscoverPool pool, Object[] triggers) {}
        templateManager.registerComponent(Components.Effect.DISCOVER, new ComponentParser<Object, DiscoverProxy, Components.Discover>() {

            @Override
            public DiscoverProxy parse(TemplateParser parser, TemplateFormat format, Object node) {
                DiscoverPool pool = DiscoverPool.valueOf(parser.parseText(format.getAttribute(node, "pool")));
                Object[] triggers = parseOrCreateChildEntities(parser, format, node, TemplateKeyword.ENTITIES);
                return new DiscoverProxy(pool, triggers);
            }

            @Override
            public Components.Discover resolve(int[] proxiedEntities, DiscoverProxy recordedValue) {
                return new Components.Discover(recordedValue.pool, resolveEntities(proxiedEntities, recordedValue.triggers));
            }
        });
        templateManager.registerComponent(Components.Effect.SHUFFLE_LIBRARY, new ComponentParser_Void());
        templateManager.registerComponent(Components.Effect.END_TURN, new ComponentParser_Void());
        record TriggerDelayedProxy(ComponentDefinition<Components.TriggeredTrigger[]> triggeredTriggersComponent, Object[] triggers) {}
        templateManager.registerComponent(Components.Effect.TRIGGER_DELAYED, new ComponentParser<Object, TriggerDelayedProxy, Components.TriggerDelayed>() {

            private static final String POST = "post";
            private static final String PRE = "pre";

            @Override
            public TriggerDelayedProxy parse(TemplateParser parser, TemplateFormat format, Object node) {
                String at = parser.parseText(format.getAttribute(node, "at"));
                boolean postOrPre = at.equals(POST);
                String eventClassName = at.substring((postOrPre ? POST : PRE).length());
                Class<? extends Event> eventClass;
                switch (eventClassName) {
                    case "EndTurn" -> eventClass = EndTurnEvent.class;
                    default -> throw new IllegalArgumentException(eventClassName);
                }
                ComponentsTriggers.TriggeredTriggerDefinition<Event> triggeredTriggerDefinition = ComponentsTriggers.getTriggeredTriggerDefinition(postOrPre, eventClass);
                Object[] triggers = parseOrCreateChildEntities(parser, format, node, TemplateKeyword.ENTITIES);
                return new TriggerDelayedProxy(triggeredTriggerDefinition.component(), triggers);
            }

            @Override
            public Components.TriggerDelayed resolve(int[] proxiedEntities, TriggerDelayedProxy recordedValue) {
                return new Components.TriggerDelayed(recordedValue.triggeredTriggersComponent, resolveEntities(proxiedEntities, recordedValue.triggers));
            }
        });
        templateManager.registerComponent(Components.Effect.PRE_ANIMATIONS, new ComponentParser_StringArray());
        templateManager.registerComponent(Components.Effect.POST_ANIMATIONS, new ComponentParser_StringArray());

        templateManager.registerComponent(Components.Effect.Zones.MOVE_TO_HAND, new ComponentParser_Void());
        templateManager.registerComponent(Components.Effect.Zones.MOVE_TO_CREATURE_ZONE, new ComponentParser_Void());
        templateManager.registerComponent(Components.Effect.Zones.MOVE_TO_GRAVEYARD, new ComponentParser_Void());
        templateManager.registerComponent(Components.Effect.Zones.MOVE_TO_LIBRARY, new ComponentParser_Void());

        templateManager.registerComponent(Components.Target.SOURCE_PREFILTERS, new ComponentParser_Prefilters());
        templateManager.registerComponent(Components.Target.TARGET_PREFILTERS, new ComponentParser_Prefilters());
        templateManager.registerComponent(Components.Target.TARGETS, new ComponentParser_Entities());
        templateManager.registerComponent(Components.Target.TARGET_SIMPLE, new ComponentParser_EnumArray<>(SimpleTarget[]::new, SimpleTarget::valueOf));
        templateManager.registerComponent(Components.Target.TARGET_CUSTOM, new ComponentParser_String());
        templateManager.registerComponent(Components.Target.TARGET_ALL, new ComponentParser_String());
        templateManager.registerComponent(Components.Target.TARGET_RANDOM, new ComponentParser_String());

        templateManager.registerComponent(Components.Spell.TARGET_OPTIONAL, new ComponentParser_Void());
        templateManager.registerComponent(Components.Spell.MINIMUM_TARGETS, new ComponentParser_Integer());
        templateManager.registerComponent(Components.Spell.MAXIMUM_TARGETS, new ComponentParser_Integer());
        templateManager.registerComponent(Components.Spell.CURRENT_CASTS_PER_TURN, new ComponentParser_Integer());
        templateManager.registerComponent(Components.Spell.MAXIMUM_CASTS_PER_TURN, new ComponentParser_Integer());
        templateManager.registerComponent(Components.Spell.TAUNTABLE, new ComponentParser_Void());
        templateManager.registerComponent(Components.Spell.CAST_TRIGGERS, new ComponentParser_Entities());

        for (ComponentDefinition<int[]> component : ComponentsTriggers.getAllTriggerComponents()) {
            templateManager.registerComponent(component, new ComponentParser_Entities());
        }

        EntityTemplate.addLoader(templateManager);
    }
}
