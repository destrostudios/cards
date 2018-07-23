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
import com.destrostudios.cards.shared.rules.game.phases.attack.*;
import com.destrostudios.cards.shared.rules.game.phases.block.*;
import com.destrostudios.cards.shared.rules.game.phases.main.*;

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
        addEventHandler(events.instant(), AddCardToEnchantmentZoneEvent.class, new AddCardToEnchantmentZoneHandler());
        addEventHandler(events.instant(), AddCardToGraveyardEvent.class, new AddCardToGraveyardHandler());
        addEventHandler(events.instant(), AddCardToHandEvent.class, new AddCardToHandHandler());
        addEventHandler(events.instant(), AddCardToLandZoneEvent.class, new AddCardToLandZoneHandler());
        addEventHandler(events.instant(), AddCardToLibraryEvent.class, new AddCardToLibraryHandler());
        addEventHandlers(events.instant(), AddCardToZoneEvent.class,
                new RemoveFromOtherZonesOnAddHandler(),
                new AddCardToZoneHandler());
        addEventHandler(events.instant(), AddManaEvent.class, new AddManaHandler());
        addEventHandler(events.instant(), BattleEvent.class, new BattleHandler());
        addEventHandler(events.instant(), DamageEvent.class, new DamageHandler());
        addEventHandlers(events.instant(), DeclareAttackEvent.class,
                new DeclareAttackHandler(),
                new TapOnDeclareAttackHandler());
        addEventHandler(events.instant(), DeclareBlockEvent.class, new DeclareBlockHandler());
        addEventHandler(events.resolved(), DestructionEvent.class, new DestructionHandler());
        addEventHandler(events.instant(), DrawCardEvent.class, new DrawCardHandler());
        addEventHandler(events.instant(), EndAttackPhaseEvent.class, new EndAttackPhaseHandler());
        addEventHandlers(events.instant(), EndBlockPhaseEvent.class,
                new EndBlockPhaseHandler(),
                new BattlesOnBlockPhaseEndHandler());
        addEventHandler(events.instant(), EndMainPhaseOneEvent.class, new EndMainPhaseOneHandler());
        addEventHandler(events.instant(), EndMainPhaseTwoEvent.class, new EndMainPhaseTwoHandler());
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
        addEventHandler(events.instant(), RemoveCardFromEnchantmentZoneEvent.class, new RemoveCardFromEnchantmentZoneHandler());
        addEventHandler(events.instant(), RemoveCardFromGraveyardEvent.class, new RemoveCardFromGraveyardHandler());
        addEventHandler(events.instant(), RemoveCardFromHandEvent.class, new RemoveCardFromHandHandler());
        addEventHandler(events.instant(), RemoveCardFromLandZoneEvent.class, new RemoveCardFromLandZoneHandler());
        addEventHandler(events.instant(), RemoveCardFromLibraryEvent.class, new RemoveCardFromLibraryHandler());
        addEventHandler(events.instant(), RemoveCardFromZoneEvent.class, new RemoveCardFromZoneHandler());
        addEventHandlers(events.instant(), SetHealthEvent.class,
                new SetHealthHandler(),
                new DestroyOnNoHealthHandler());
        addEventHandler(events.instant(), ShuffleLibraryEvent.class, new ShuffleLibraryHandler());
        addEventHandlers(events.instant(), StartAttackPhaseEvent.class, new StartAttackPhaseHandler());
        addEventHandler(events.instant(), StartBlockPhaseEvent.class, new StartBlockPhaseHandler());
        addEventHandlers(events.instant(), StartMainPhaseOneEvent.class,
                new ResetManaOnMainPhaseOneHandler(),
                new UntapCardsOnMainPhaseOneHandler(),
                new DrawCardOnMainPhaseOneHandler(),
                new StartMainPhaseOneHandler());
        addEventHandler(events.instant(), StartMainPhaseTwoEvent.class, new StartMainPhaseTwoHandler());
        addEventHandler(events.instant(), TapEvent.class, new TapHandler());
        addEventHandler(events.instant(), TriggerEffectEvent.class, new TriggerEffectHandler());
        addEventHandler(events.instant(), UntapEvent.class, new UntapHandler());
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
