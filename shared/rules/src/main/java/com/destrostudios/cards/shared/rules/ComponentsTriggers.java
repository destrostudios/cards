package com.destrostudios.cards.shared.rules;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.battle.BattleEvent;
import com.destrostudios.cards.shared.rules.battle.DamageEvent;
import com.destrostudios.cards.shared.rules.battle.DestructionEvent;
import com.destrostudios.cards.shared.rules.battle.HealEvent;
import com.destrostudios.cards.shared.rules.cards.CastSpellEvent;
import com.destrostudios.cards.shared.rules.cards.DiscardEvent;
import com.destrostudios.cards.shared.rules.cards.DrawCardEvent;
import com.destrostudios.cards.shared.rules.cards.zones.MoveToCreatureZoneEvent;
import com.destrostudios.cards.shared.rules.game.turn.EndTurnEvent;

import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ComponentsTriggers {

    public static final boolean PRE = false;
    public static final boolean POST = true;
    private static HashMap<Class<? extends Event>, HashMap<Boolean, TriggerDefinition>> TRIGGERS = new HashMap<>();
    private static HashMap<Class<? extends Event>, HashMap<Boolean, TriggeredTriggerDefinition>> TRIGGERED_TRIGGERS = new HashMap<>();
    static {
        TriggerRegistration<?>[] triggers = new TriggerRegistration[] {
            new TriggerRegistration<>(new boolean[] { true }, new boolean[] {}, BattleEvent.class, event -> new int[] { event.source }),
            new TriggerRegistration<>(new boolean[] { true }, new boolean[] {}, CastSpellEvent.class, event -> new int[] { event.spell }),
            new TriggerRegistration<>(new boolean[] { true }, new boolean[] {}, DamageEvent.class, event -> new int[] { event.target }),
            new TriggerRegistration<>(new boolean[] { true }, new boolean[] {}, DestructionEvent.class, event -> new int[] { event.target }),
            new TriggerRegistration<>(new boolean[] { true }, new boolean[] {}, DiscardEvent.class, event -> new int[] { event.card }),
            new TriggerRegistration<>(new boolean[] { true }, new boolean[] {}, DrawCardEvent.class, event -> new int[] { event.player }),
            new TriggerRegistration<>(new boolean[] { false }, new boolean[] { false }, EndTurnEvent.class, event -> new int[] { event.player }),
            new TriggerRegistration<>(new boolean[] { true }, new boolean[] {}, HealEvent.class, event -> new int[] { event.target }),
            new TriggerRegistration<>(new boolean[] { true }, new boolean[] {}, MoveToCreatureZoneEvent.class, event -> new int[] { event.card }),
        };
        for (TriggerRegistration<?> trigger : triggers) {
            String eventClassName = trigger.eventClass.getSimpleName();
            Function<Boolean, String> getComponentNamePrefix = postOrPre -> (postOrPre ? "post" : "pre") + eventClassName.substring(0, eventClassName.length() - "Event".length());
            for (boolean postOrPre : trigger.postOrPresTrigger) {
                String componentName = getComponentNamePrefix.apply(postOrPre) + "Triggers";
                ComponentDefinition<int[]> component = Components.create(componentName);
                TRIGGERS.computeIfAbsent(trigger.eventClass, _ -> new HashMap<>()).put(postOrPre, new TriggerDefinition(postOrPre, trigger.eventClass, component, trigger.getTargets));
            }
            for (boolean postOrPre : trigger.postOrPresTriggeredTrigger) {
                String componentName = getComponentNamePrefix.apply(postOrPre) + "TriggeredTriggers";
                ComponentDefinition<Components.TriggeredTrigger[]> component = Components.create(componentName);
                TRIGGERED_TRIGGERS.computeIfAbsent(trigger.eventClass, _ -> new HashMap<>()).put(postOrPre, new TriggeredTriggerDefinition<>(postOrPre, trigger.eventClass, component));
            }
        }
    }

    public static <T extends Event> TriggerDefinition<T> getTriggerDefinition(boolean postOrPre, Class<? extends Event> eventClass) {
        return TRIGGERS.get(eventClass).get(postOrPre);
    }

    public static <T extends Event> TriggeredTriggerDefinition<T> getTriggeredTriggerDefinition(boolean postOrPre, Class<? extends Event> eventClass) {
        return TRIGGERED_TRIGGERS.get(eventClass).get(postOrPre);
    }

    public static List<ComponentDefinition<int[]>> getAllTriggerComponents() {
        return TRIGGERS.values().stream()
                .flatMap(triggers -> triggers.values().stream().map(trigger -> (ComponentDefinition<int[]>) trigger.component))
                .collect(Collectors.toList());
    }

    public record TriggerRegistration<T extends Event>(
        boolean[] postOrPresTrigger,
        boolean[] postOrPresTriggeredTrigger,
        Class<T> eventClass,
        Function<T, int[]> getTargets
    ) {}

    public record TriggerDefinition<T extends Event>(
        boolean postOrPre,
        Class<T> eventClass,
        ComponentDefinition<int[]> component,
        Function<T, int[]> getTargets
    ) {}

    public record TriggeredTriggerDefinition<T extends Event>(
        boolean postOrPre,
        Class<T> eventClass,
        ComponentDefinition<Components.TriggeredTrigger[]> component
    ) {}
}
