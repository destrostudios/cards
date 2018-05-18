package com.destrostudios.cards.shared.rules;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.entities.SimpleEntityData;
import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.events.EventDispatcher;
import com.destrostudios.cards.shared.events.EventQueue;
import com.destrostudios.cards.shared.rules.battle.*;
import com.destrostudios.cards.shared.rules.cards.*;
import com.destrostudios.cards.shared.rules.game.*;

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

    public GameContext(EventQueueProvider<EventQueueType> eventQueueProvider, IntUnaryOperator random) {
        data = new SimpleEntityData();
        preDispatcher = new EventDispatcher();
        dispatcher = new EventDispatcher();
        postDispatcher = new EventDispatcher();
        this.events = eventQueueProvider.provideEventQueue(preDispatcher::fire, dispatcher::fire, postDispatcher::fire);
        this.random = random;
        initListeners();
    }

    private void initListeners() {
        addGameEventHandler(AddCardToBoardEvent.class, new AddCardToBoardHandler());
        addGameEventHandler(AddCardToHandEvent.class, new AddCardToHandHandler());
        addGameEventHandler(AddCardToLibraryEvent.class, new AddCardToLibraryHandler());
        addGameEventHandler(DamageEvent.class, new DamageHandler());
        addGameEventHandler(DrawCardEvent.class, new DrawCardHandler());
        addGameEventHandler(EndTurnEvent.class, new EndTurnHandler());
        addGameEventHandlers(GameStartEvent.class, new ShuffleAllLibrariesOnGameStartHandler(), new SetStartingPlayerHandler());
        addGameEventHandler(PlayCardFromHandEvent.class, new PlayCardFromHandHandler());
        addGameEventHandler(RemoveCardFromHandEvent.class, new RemoveCardFromHandHandler());
        addGameEventHandler(RemoveCardFromLibraryEvent.class, new RemoveCardFromLibraryHandler());
        addGameEventHandler(SetHealthEvent.class, new SetHealthHandler());
        addGameEventHandler(ShuffleLibraryEvent.class, new ShuffleLibraryHandler());
        addGameEventHandler(StartTurnEvent.class, new DrawCardOnStartTurnHandler());
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

    public interface EventQueueProvider<EventQueueType extends EventQueue> {
        EventQueueType provideEventQueue(Consumer<Event> preDispatcher, Consumer<Event> dispatcher, Consumer<Event> postDispatcher);
    }

}
