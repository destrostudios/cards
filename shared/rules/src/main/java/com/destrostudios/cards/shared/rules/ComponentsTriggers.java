package com.destrostudios.cards.shared.rules;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.battle.BattleEvent;
import com.destrostudios.cards.shared.rules.battle.DamageEvent;
import com.destrostudios.cards.shared.rules.battle.DestructionEvent;
import com.destrostudios.cards.shared.rules.battle.HealEvent;
import com.destrostudios.cards.shared.rules.cards.CastSpellEvent;
import com.destrostudios.cards.shared.rules.cards.DrawCardEvent;
import com.destrostudios.cards.shared.rules.game.turn.EndTurnEvent;

import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ComponentsTriggers {

    public static final boolean PRE = false;
    public static final boolean POST = true;
    private static HashMap<Class<? extends Event>, HashMap<Boolean, TriggerDefinition>> TRIGGERS = new HashMap<>();
    static {
        TriggerRegistration<?>[] triggers = new TriggerRegistration[] {
            new TriggerRegistration<>(new boolean[] { true }, BattleEvent.class, event -> new int[] { event.source }),
            new TriggerRegistration<>(new boolean[] { true }, CastSpellEvent.class, event -> new int[] { event.spell }),
            new TriggerRegistration<>(new boolean[] { true }, DamageEvent.class, event -> new int[] { event.target }),
            new TriggerRegistration<>(new boolean[] { true }, DestructionEvent.class, event -> new int[] { event.target }),
            new TriggerRegistration<>(new boolean[] { true }, DrawCardEvent.class, event -> new int[] { event.player }),
            new TriggerRegistration<>(new boolean[] { false }, EndTurnEvent.class, event -> new int[] { event.player }),
            new TriggerRegistration<>(new boolean[] { true }, HealEvent.class, event -> new int[] { event.target }),
        };
        for (TriggerRegistration<?> trigger : triggers) {
            String eventClassName = trigger.eventClass.getSimpleName();
            for (boolean postOrPre : trigger.postOrPres) {
                String componentName = (postOrPre ? "post" : "pre") + eventClassName.substring(0, eventClassName.length() - "Event".length()) + "Triggers";
                ComponentDefinition<int[]> component = Components.create(componentName);
                TRIGGERS.computeIfAbsent(trigger.eventClass, _ -> new HashMap<>()).put(postOrPre, new TriggerDefinition(postOrPre, trigger.eventClass, component, trigger.getTargets));
            }
        }
    }

    public static <T extends Event> TriggerDefinition<T> getTriggersComponent(boolean postOrPre, Class<? extends Event> eventClass) {
        return TRIGGERS.get(eventClass).get(postOrPre);
    }

    public static List<ComponentDefinition<int[]>> getAllComponents() {
        return TRIGGERS.values().stream()
                .flatMap(triggers -> triggers.values().stream().map(trigger -> (ComponentDefinition<int[]>) trigger.component))
                .collect(Collectors.toList());
    }

    public record TriggerRegistration<T extends Event>(
        boolean[] postOrPres,
        Class<T> eventClass,
        Function<T, int[]> getTargets
    ) {}

    public record TriggerDefinition<T extends Event>(
        boolean postOrPre,
        Class<T> eventClass,
        ComponentDefinition<int[]> component,
        Function<T, int[]> getTargets
    ) {}
}
