package com.destrostudios.cards.shared.rules;

import com.destrostudios.cards.shared.entities.SimpleEntityData;
import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.events.EventHandler;
import com.destrostudios.cards.shared.events.EventHandlers;
import com.destrostudios.cards.shared.events.EventQueue;
import com.destrostudios.cards.shared.rules.abilities.*;
import com.destrostudios.cards.shared.rules.conditions.*;
import com.destrostudios.cards.shared.rules.battle.*;
import com.destrostudios.cards.shared.rules.buffs.*;
import com.destrostudios.cards.shared.rules.cards.*;
import com.destrostudios.cards.shared.rules.cards.zones.*;
import com.destrostudios.cards.shared.rules.effects.*;
import com.destrostudios.cards.shared.rules.game.*;
import com.destrostudios.cards.shared.rules.game.turn.*;
import com.destrostudios.cards.shared.rules.triggers.TriggerHandler;
import lombok.Getter;

import static com.destrostudios.cards.shared.rules.ComponentsTriggers.*;

public class GameContext {

    public GameContext(GameContext gameContext) {
        // TODO: Reuse the same GameEventHandler instances (after making them completely stateless (they still reference data+events))
        this(gameContext.startGameInfo, new SimpleEntityData(gameContext.data));
    }

    public GameContext(StartGameInfo startGameInfo, SimpleEntityData data) {
        this.startGameInfo = startGameInfo;
        this.data = data;
        events = new EventQueue();
        initListeners();
    }
    @Getter
    private StartGameInfo startGameInfo;
    @Getter
    private SimpleEntityData data;
    @Getter
    private EventQueue events;
    @Getter
    private Integer winner;

    private void initListeners() {
        setEventHandlers(events.instant(), EventType.MOVE_TO_CREATURE_ZONE,
                new MoveToCreatureZoneHandler(),
                new ActivateDivineShieldOnMoveToCreatureZoneHandler());
        setEventHandlers(events.instant(), EventType.MOVE_TO_GRAVEYARD, new MoveToGraveyardHandler());
        setEventHandlers(events.instant(), EventType.MOVE_TO_HAND, new MoveToHandHandler());
        setEventHandlers(events.instant(), EventType.MOVE_TO_LIBRARY, new MoveToLibraryHandler());
        setEventHandlers(events.instant(), EventType.ADD_MANA, new AddManaHandler());
        setEventHandlers(events.instant(), EventType.SET_AVAILABLE_MANA, new SetAvailableManaHandler());
        setEventHandlers(events.instant(), EventType.SET_MANA, new SetManaHandler());
        setEventHandlers(events.instant(), EventType.BATTLE, new BattleHandler());
        setEventHandlers(events.resolved(), EventType.BATTLE,
                new TriggerHandler<>(getTriggersComponent(POST, BattleEvent.class))
        );
        setEventHandlers(events.instant(), EventType.DAMAGE, new DamageHandler());
        setEventHandlers(events.resolved(), EventType.DAMAGE,
                new TriggerHandler<>(getTriggersComponent(POST, DamageEvent.class)),
                new CheckDestructionAfterDamageHandler()
        );
        setEventHandlers(events.instant(), EventType.HEAL, new HealHandler());
        setEventHandlers(events.resolved(), EventType.HEAL,
                new TriggerHandler<>(getTriggersComponent(POST, HealEvent.class))
        );
        setEventHandlers(events.instant(), EventType.DESTRUCTION, new DestructionHandler());
        setEventHandlers(events.resolved(), EventType.DESTRUCTION,
                new TriggerHandler<>(getTriggersComponent(POST, DestructionEvent.class))
        );
        setEventHandlers(events.instant(), EventType.DRAW_CARD, new DrawCardHandler());
        setEventHandlers(events.pre(), EventType.END_TURN,
                new TriggerHandler<>(getTriggersComponent(PRE, EndTurnEvent.class)),
                new RemoveTemporaryBuffsOnEndTurnHandler());
        setEventHandlers(events.instant(), EventType.END_TURN,
                new EndTurnHandler(),
                new ResetCurrentCastsPerTurnOnEndTurnHandler()
        );
        setEventHandlers(events.instant(), EventType.GAME_START,
                new SetStartingPlayerHandler(),
                new ShuffleAllLibrariesOnGameStartHandler(),
                new AddInitialCardsHandOnGameStartHandler());
        setEventHandlers(events.instant(), EventType.MULLIGAN, new MulliganHandler());
        setEventHandlers(events.instant(), EventType.PAY_MANA, new PayManaHandler());
        setEventHandlers(events.instant(), EventType.CAST_SPELL,
                new CastSpellHandler(),
                new IncreaseCurrentCastsPerTurnHandler()
        );
        setEventHandlers(events.resolved(), EventType.CAST_SPELL,
                new TriggerHandler<>(getTriggersComponent(POST, CastSpellEvent.class))
        );
        setEventHandlers(events.instant(), EventType.REMOVED_FROM_CREATURE_ZONE,
                new RemoveDamageOnRemovedFromCreatureZoneHandler(),
                new DeactivateDivineShieldOnRemovedFromCreatureZoneHandler(),
                new RemoveBuffsOnRemovedFromCreatureZoneHandler()
        );
        setEventHandlers(events.instant(), EventType.REMOVED_FROM_HAND,
                new RemoveDefaultCastFromHandSpellBuffsOnRemovedFromHandHandler()
        );
        setEventHandlers(events.instant(), EventType.SHUFFLE_LIBRARY, new ShuffleLibraryHandler());
        setEventHandlers(events.instant(), EventType.START_TURN,
                new StartTurnHandler(),
                new IncreaseAvailableManaOnTurnStartHandler(),
                new SetManaToAvailableManaOnTurnStartHandler(),
                new DrawCardOnTurnStartHandler()
        );
        setEventHandlers(events.instant(), EventType.TRIGGER_IF_POSSIBLE, new TriggerIfPossibleHandler());
        setEventHandlers(events.instant(), EventType.TRIGGER_EFFECT, new TriggerEffectHandler());
        setEventHandlers(events.instant(), EventType.TRIGGER_EFFECT_IMPACT, new TriggerEffectImpactHandler());
        setEventHandlers(events.instant(), EventType.ADD_BUFF, new AddBuffHandler());
        setEventHandlers(events.instant(), EventType.REMOVE_BUFF, new RemoveBuffHandler());
        setEventHandlers(events.instant(), EventType.CREATE, new CreateHandler());
        setEventHandlers(events.instant(), EventType.CONDITIONS_AFFECTED,
                new CheckBonusDamageHandler(),
                new CheckDestructionAfterConditionsAffectedHandler()
        );
        setEventHandlers(events.instant(), EventType.GAME_OVER, new GameOverHandler(this));
    }

    private <T extends Event> void setEventHandlers(EventHandlers eventHandlers, EventType eventType, GameEventHandler... handlers) {
        EventHandler<T>[] rawHandlers = new EventHandler[handlers.length];
        for (int i = 0; i < rawHandlers.length; i++) {
            GameEventHandler<T> handler = handlers[i];
            handler.data = data;
            handler.events = events;
            rawHandlers[i] = handler::handle;
        }
        eventHandlers.put(eventType, rawHandlers);
    }

    public void onGameOver(int winner) {
        this.winner = winner;
    }

    public boolean isGameOver() {
        return (winner != null);
    }

    public int getUserId(int player) {
        String login = data.getComponent(player, Components.NAME);
        for (PlayerInfo playerInfo : startGameInfo.getPlayers()) {
            if (playerInfo.getLogin().equals(login)) {
                return playerInfo.getId();
            }
        }
        throw new RuntimeException("User with login '" + login + "' not found.");
    }
}
