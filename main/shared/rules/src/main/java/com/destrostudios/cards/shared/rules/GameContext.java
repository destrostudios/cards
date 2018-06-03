package com.destrostudios.cards.shared.rules;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.entities.SimpleEntityData;
import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.events.EventDispatcher;
import com.destrostudios.cards.shared.events.EventQueue;
import com.destrostudios.cards.shared.rules.battle.*;
import com.destrostudios.cards.shared.rules.cards.*;
import com.destrostudios.cards.shared.rules.cards.zones.*;
import com.destrostudios.cards.shared.rules.game.*;
import com.destrostudios.cards.shared.rules.game.phases.attack.*;
import com.destrostudios.cards.shared.rules.game.phases.block.*;
import com.destrostudios.cards.shared.rules.game.phases.main.*;

import java.util.function.Consumer;
import java.util.function.IntUnaryOperator;

/**
 *
 * @author Philipp
 */
public class GameContext<EventQueueType extends EventQueue> {

    private final EntityData data;
    private final EventDispatcher preDispatcher;
    private final EventDispatcher dispatcher;
    private final EventDispatcher postDispatcher;
    private final EventQueueType events;
    private final IntUnaryOperator random;
    private final PlayerActionsGenerator actionGenerator;

    public GameContext(EventQueueProvider<EventQueueType> eventQueueProvider, IntUnaryOperator random) {
        data = new SimpleEntityData();
        preDispatcher = new EventDispatcher();
        dispatcher = new EventDispatcher();
        postDispatcher = new EventDispatcher();
        this.events = eventQueueProvider.provideEventQueue(preDispatcher::fire, dispatcher::fire, postDispatcher::fire);
        this.random = random;
        actionGenerator = new PlayerActionsGenerator(data);
        initListeners();
    }

    private void initListeners() {
        addGameEventHandler(AddCardToBoardEvent.class, new AddCardToBoardHandler());
        addGameEventHandler(AddCardToBoardZoneEvent.class, new AddCardToBoardZoneHandler());
        addGameEventHandler(AddCardToCreatureZoneEvent.class, new AddCardToCreatureZoneHandler());
        addGameEventHandler(AddCardToEnchantmentZoneEvent.class, new AddCardToEnchantmentZoneHandler());
        addGameEventHandler(AddCardToGraveyardEvent.class, new AddCardToGraveyardHandler());
        addGameEventHandler(AddCardToHandEvent.class, new AddCardToHandHandler());
        addGameEventHandler(AddCardToLandZoneEvent.class, new AddCardToLandZoneHandler());
        addGameEventHandler(AddCardToLibraryEvent.class, new AddCardToLibraryHandler());
        addGameEventHandlers(AddCardToZoneEvent.class,
                new RemoveFromOtherZonesOnAddHandler(),
                new AddCardToZoneHandler());
        addGameEventHandler(BattleEvent.class, new BattleHandler());
        addGameEventHandler(DamageEvent.class, new DamageHandler());
        addGameEventHandlers(DeclareAttackEvent.class,
                new DeclareAttackHandler(),
                new TapOnDeclareAttackHandler());
        addGameEventHandler(DeclareBlockEvent.class, new DeclareBlockHandler());
        addGameEventHandler(DestructionEvent.class, new DestructionHandler());
        addGameEventHandler(DrawCardEvent.class, new DrawCardHandler());
        addGameEventHandler(EndAttackPhaseEvent.class, new EndAttackPhaseHandler());
        addGameEventHandlers(EndBlockPhaseEvent.class,
                new EndBlockPhaseHandler(),
                new BattlesOnBlockPhaseEndHandler());
        addGameEventHandler(EndMainPhaseOneEvent.class, new EndMainPhaseOneHandler());
        addGameEventHandler(EndMainPhaseTwoEvent.class, new EndMainPhaseTwoHandler());
        addGameEventHandlers(GameStartEvent.class,
                new ShuffleAllLibrariesOnGameStartHandler(),
                new DrawCardsOnGameStartHandler(),
                new SetStartingPlayerHandler());
        addGameEventHandler(PayCostEvent.class, new PayCostHandler());
        addGameEventHandler(PlaySpellEvent.class, new PlaySpellHandler());
        addGameEventHandler(RemoveCardFromBoardEvent.class, new RemoveCardFromBoardHandler());
        addGameEventHandler(RemoveCardFromBoardZoneEvent.class, new RemoveCardFromBoardZoneHandler());
        addGameEventHandler(RemoveCardFromCreatureZoneEvent.class, new RemoveCardFromCreatureZoneHandler());
        addGameEventHandler(RemoveCardFromEnchantmentZoneEvent.class, new RemoveCardFromEnchantmentZoneHandler());
        addGameEventHandler(RemoveCardFromGraveyardEvent.class, new RemoveCardFromGraveyardHandler());
        addGameEventHandler(RemoveCardFromHandEvent.class, new RemoveCardFromHandHandler());
        addGameEventHandler(RemoveCardFromLandZoneEvent.class, new RemoveCardFromLandZoneHandler());
        addGameEventHandler(RemoveCardFromLibraryEvent.class, new RemoveCardFromLibraryHandler());
        addGameEventHandler(RemoveCardFromZoneEvent.class, new RemoveCardFromZoneHandler());
        addGameEventHandlers(SetHealthEvent.class,
                new SetHealthHandler(),
                new DestroyOnNoHealthHandler());
        addGameEventHandler(ShuffleLibraryEvent.class, new ShuffleLibraryHandler());
        addGameEventHandlers(StartAttackPhaseEvent.class, new StartAttackPhaseHandler());
        addGameEventHandler(StartBlockPhaseEvent.class, new StartBlockPhaseHandler());
        addGameEventHandlers(StartMainPhaseOneEvent.class,
                new UntapCardsOnMainPhaseOneHandler(),
                new DrawCardOnMainPhaseOneHandler(),
                new StartMainPhaseOneHandler());
        addGameEventHandler(StartMainPhaseTwoEvent.class, new StartMainPhaseTwoHandler());
        addGameEventHandler(TapEvent.class, new TapHandler());
        addGameEventHandler(UntapEvent.class, new UntapHandler());
    }
    
    @SafeVarargs
    private final <T extends Event> void addGameEventHandlers(Class<T> eventType, GameEventHandler<T>... handlers) {
        for (GameEventHandler<T> handler : handlers) {
            addGameEventHandler(eventType, handler);
        }
    }

    private <T extends Event> void addGameEventHandler(Class<T> eventType, GameEventHandler<T> handler) {
        handler.data = data;
        handler.events = events;
        handler.random = random;
        dispatcher.addListeners(eventType, handler::handle);
    }

    public EntityData getData() {
        return data;
    }

    public EventDispatcher getPreDispatcher() {
        return preDispatcher;
    }

    public EventDispatcher getDispatcher() {
        return dispatcher;
    }

    public EventDispatcher getPostDispatcher() {
        return postDispatcher;
    }

    public EventQueueType getEvents() {
        return events;
    }

    public IntUnaryOperator getRandom() {
        return random;
    }

    public PlayerActionsGenerator getActionGenerator() {
        return actionGenerator;
    }

    public interface EventQueueProvider<EventQueueType extends EventQueue> {
        EventQueueType provideEventQueue(Consumer<Event> preDispatcher, Consumer<Event> dispatcher, Consumer<Event> postDispatcher);
    }

}
