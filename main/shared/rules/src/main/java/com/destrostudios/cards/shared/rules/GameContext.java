package com.destrostudios.cards.shared.rules;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.entities.SimpleEntityData;
import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.events.EventHandlers;
import com.destrostudios.cards.shared.events.EventQueue;
import com.destrostudios.cards.shared.rules.battle.*;
import com.destrostudios.cards.shared.rules.cards.*;
import com.destrostudios.cards.shared.rules.cards.zones.*;
import com.destrostudios.cards.shared.rules.effects.*;
import com.destrostudios.cards.shared.rules.game.*;
import com.destrostudios.cards.shared.rules.game.turn.*;

import java.util.function.IntUnaryOperator;

/**
 *
 * @author Philipp
 */
public class GameContext {

    private final EntityData data;
    private final EventQueue events;
    private final IntUnaryOperator random;
    private final PlayerActionsGenerator actionGenerator;

    public GameContext(IntUnaryOperator random) {
        data = new SimpleEntityData();
        events = new EventQueue();
        this.random = random;
        actionGenerator = new PlayerActionsGenerator(data);
        initListeners();
    }

    private void initListeners() {
        addEventHandler(events.instant(), AddCardToBoardEvent.class, new AddCardToBoardHandler());
        addEventHandler(events.instant(), AddCardToBoardZoneEvent.class, new AddCardToBoardZoneHandler());
        addEventHandler(events.instant(), AddCardToCreatureZoneEvent.class, new AddCardToCreatureZoneHandler());
        addEventHandler(events.instant(), AddCardToGraveyardEvent.class, new AddCardToGraveyardHandler());
        addEventHandler(events.instant(), AddCardToHandEvent.class, new AddCardToHandHandler());
        addEventHandler(events.instant(), AddCardToLibraryEvent.class, new AddCardToLibraryHandler());
        addEventHandler(events.instant(), AddCardToSpellZoneEvent.class, new AddCardToSpellZoneHandler());
        addEventHandlers(events.instant(), AddCardToZoneEvent.class,
                new RemoveFromOtherZonesOnAddHandler(),
                new AddCardToZoneHandler());
        addEventHandler(events.instant(), AddManaEvent.class, new AddManaHandler());
        addEventHandler(events.instant(), SetAvailableManaEvent.class, new SetAvailableManaHandler());
        addEventHandler(events.instant(), SetManaEvent.class, new SetManaHandler());
        addEventHandler(events.instant(), AttackEvent.class, new AttackHandler());
        addEventHandler(events.instant(), BattleEvent.class, new BattleHandler());
        addEventHandler(events.instant(), DamageEvent.class, new DamageHandler());
        addEventHandler(events.resolved(), DestructionEvent.class, new DestructionHandler());
        addEventHandler(events.instant(), DrawCardEvent.class, new DrawCardHandler());
        addEventHandlers(events.instant(), EndTurnEvent.class,
                new EndTurnHandler(),
                new ResetHasAttackedOnEndTurnHandler());
        addEventHandlers(events.instant(), GameStartEvent.class,
                new ShuffleAllLibrariesOnGameStartHandler(),
                new DrawCardsOnGameStartHandler(),
                new SetStartingPlayerHandler());
        addEventHandler(events.instant(), PayCostEvent.class, new PayCostHandler());
        addEventHandler(events.instant(), PayManaEvent.class, new PayManaHandler());
        addEventHandler(events.instant(), PlaySpellEvent.class, new PlaySpellHandler());
        addEventHandler(events.instant(), RemoveCardFromBoardEvent.class, new RemoveCardFromBoardHandler());
        addEventHandler(events.instant(), RemoveCardFromBoardZoneEvent.class, new RemoveCardFromBoardZoneHandler());
        addEventHandler(events.instant(), RemoveCardFromCreatureZoneEvent.class, new RemoveCardFromCreatureZoneHandler());
        addEventHandler(events.instant(), RemoveCardFromGraveyardEvent.class, new RemoveCardFromGraveyardHandler());
        addEventHandler(events.instant(), RemoveCardFromHandEvent.class, new RemoveCardFromHandHandler());
        addEventHandler(events.instant(), RemoveCardFromLibraryEvent.class, new RemoveCardFromLibraryHandler());
        addEventHandler(events.instant(), RemoveCardFromSpellZoneEvent.class, new RemoveCardFromSpellZoneHandler());
        addEventHandler(events.instant(), RemoveCardFromZoneEvent.class, new RemoveCardFromZoneHandler());
        addEventHandlers(events.instant(), SetHealthEvent.class,
                new SetHealthHandler(),
                new DestroyOnNoHealthHandler());
        addEventHandler(events.instant(), ShuffleLibraryEvent.class, new ShuffleLibraryHandler());
        addEventHandlers(events.instant(), StartTurnEvent.class,
                new StartTurnHandler(),
                new IncreaseAvailableManaOnTurnStartHandler(),
                new SetManaToAvailableManaOnTurnStartHandler(),
                new DrawCardOnTurnStartHandler(),
                new ExecuteBotActionsOnTurnStartHandler());
        addEventHandler(events.instant(), CheckEffectTriggerEvent.class, new CheckEffectTriggerHandler());
        addEventHandler(events.instant(), TriggerEffectEvent.class, new TriggerEffectHandler());
    }

    private <T extends Event> void addEventHandlers(EventHandlers eventHandlers, Class<T> eventClass, GameEventHandler<T>... handlers) {
        for (GameEventHandler<T> handler : handlers) {
            addEventHandler(eventHandlers, eventClass, handler);
        }
    }

    private <T extends Event> void addEventHandler(EventHandlers eventHandlers, Class<T> eventClass, GameEventHandler<T> handler) {
        handler.data = data;
        handler.events = events;
        handler.random = random;
        eventHandlers.add(eventClass, handler::handle);
    }

    public EntityData getData() {
        return data;
    }

    public EventQueue getEvents() {
        return events;
    }

    public IntUnaryOperator getRandom() {
        return random;
    }

    public PlayerActionsGenerator getActionGenerator() {
        return actionGenerator;
    }
}
