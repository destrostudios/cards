package com.destrostudios.cards.shared.rules;

import com.destrostudios.cards.shared.events.*;
import com.destrostudios.cards.shared.rules.abilities.ActivateDivineShieldOnMoveToCreatureZoneHandler;
import com.destrostudios.cards.shared.rules.abilities.DeactivateDivineShieldOnRemovedFromCreatureZoneHandler;
import com.destrostudios.cards.shared.rules.battle.*;
import com.destrostudios.cards.shared.rules.buffs.AddBuffHandler;
import com.destrostudios.cards.shared.rules.buffs.RemoveBuffHandler;
import com.destrostudios.cards.shared.rules.buffs.RemoveBuffsOnRemovedFromCreatureZoneHandler;
import com.destrostudios.cards.shared.rules.buffs.RemoveDefaultCastFromHandSpellBuffsOnRemovedFromHandHandler;
import com.destrostudios.cards.shared.rules.cards.*;
import com.destrostudios.cards.shared.rules.cards.zones.MoveToCreatureZoneHandler;
import com.destrostudios.cards.shared.rules.cards.zones.MoveToGraveyardHandler;
import com.destrostudios.cards.shared.rules.cards.zones.MoveToHandHandler;
import com.destrostudios.cards.shared.rules.cards.zones.MoveToLibraryHandler;
import com.destrostudios.cards.shared.rules.conditions.CheckBonusDamageHandler;
import com.destrostudios.cards.shared.rules.conditions.CheckDestructionAfterConditionsAffectedHandler;
import com.destrostudios.cards.shared.rules.effects.*;
import com.destrostudios.cards.shared.rules.game.GameOverHandler;
import com.destrostudios.cards.shared.rules.game.SetStartingPlayerHandler;
import com.destrostudios.cards.shared.rules.game.turn.*;

import static com.destrostudios.cards.shared.rules.ComponentsTriggers.*;

public class GameEventHandling extends EventHandling<GameContext> {

    public static GameEventHandling GLOBAL_INSTANCE = new GameEventHandling();

    public GameEventHandling() {
        set(instant, EventType.MOVE_TO_CREATURE_ZONE,
            new MoveToCreatureZoneHandler(),
            new ActivateDivineShieldOnMoveToCreatureZoneHandler());
        set(instant, EventType.MOVE_TO_GRAVEYARD, new MoveToGraveyardHandler());
        set(instant, EventType.MOVE_TO_HAND, new MoveToHandHandler());
        set(instant, EventType.MOVE_TO_LIBRARY, new MoveToLibraryHandler());
        set(instant, EventType.ADD_MANA, new AddManaHandler());
        set(instant, EventType.SET_AVAILABLE_MANA, new SetAvailableManaHandler());
        set(instant, EventType.SET_MANA, new SetManaHandler());
        set(instant, EventType.BATTLE, new BattleHandler());
        set(resolved, EventType.BATTLE,
            new CheckTriggersHandler<>(getTriggersComponent(POST, BattleEvent.class))
        );
        set(instant, EventType.DAMAGE, new DamageHandler());
        set(resolved, EventType.DAMAGE,
            new CheckTriggersHandler<>(getTriggersComponent(POST, DamageEvent.class)),
            new CheckDestructionAfterDamageHandler()
        );
        set(instant, EventType.HEAL, new HealHandler());
        set(resolved, EventType.HEAL,
            new CheckTriggersHandler<>(getTriggersComponent(POST, HealEvent.class))
        );
        set(instant, EventType.DESTRUCTION, new DestructionHandler());
        set(resolved, EventType.DESTRUCTION,
            new CheckTriggersHandler<>(getTriggersComponent(POST, DestructionEvent.class))
        );
        set(instant, EventType.DRAW_CARD, new DrawCardHandler());
        set(pre, EventType.END_TURN,
            new CheckTriggersHandler<>(getTriggersComponent(PRE, EndTurnEvent.class)),
            new RemoveTemporaryBuffsOnEndTurnHandler());
        set(instant, EventType.END_TURN,
            new EndTurnHandler(),
            new ResetCurrentCastsPerTurnOnEndTurnHandler()
        );
        set(instant, EventType.GAME_START,
            new SetStartingPlayerHandler(),
            new ShuffleAllLibrariesOnGameStartHandler(),
            new AddInitialCardsHandOnGameStartHandler());
        set(instant, EventType.MULLIGAN, new MulliganHandler());
        set(instant, EventType.PAY_MANA, new PayManaHandler());
        set(instant, EventType.CAST_SPELL,
            new CastSpellHandler(),
            new IncreaseCurrentCastsPerTurnHandler()
        );
        set(resolved, EventType.CAST_SPELL,
            new CheckTriggersHandler<>(getTriggersComponent(POST, CastSpellEvent.class))
        );
        set(instant, EventType.REMOVED_FROM_CREATURE_ZONE,
            new RemoveDamageOnRemovedFromCreatureZoneHandler(),
            new DeactivateDivineShieldOnRemovedFromCreatureZoneHandler(),
            new RemoveBuffsOnRemovedFromCreatureZoneHandler()
        );
        set(instant, EventType.REMOVED_FROM_HAND,
            new RemoveDefaultCastFromHandSpellBuffsOnRemovedFromHandHandler()
        );
        set(instant, EventType.SHUFFLE_LIBRARY, new ShuffleLibraryHandler());
        set(instant, EventType.START_TURN,
            new StartTurnHandler(),
            new IncreaseAvailableManaOnTurnStartHandler(),
            new SetManaToAvailableManaOnTurnStartHandler(),
            new DrawCardOnTurnStartHandler()
        );
        set(instant, EventType.TRIGGER, new TriggerHandler());
        set(instant, EventType.TRIGGER_EFFECT, new TriggerEffectHandler());
        set(instant, EventType.TRIGGER_EFFECT_IMPACT, new TriggerEffectImpactHandler());
        set(instant, EventType.ADD_BUFF, new AddBuffHandler());
        set(instant, EventType.REMOVE_BUFF, new RemoveBuffHandler());
        set(instant, EventType.CREATE, new CreateHandler());
        set(instant, EventType.CONDITIONS_AFFECTED,
            new CheckBonusDamageHandler(),
            new CheckDestructionAfterConditionsAffectedHandler()
        );
        set(instant, EventType.GAME_OVER, new GameOverHandler());
    }

    private <T extends Event> void set(EventHandlers<GameContext> eventHandlers, EventType eventType, GameEventHandler... handlers) {
        eventHandlers.put(eventType, handlers);
    }
}
