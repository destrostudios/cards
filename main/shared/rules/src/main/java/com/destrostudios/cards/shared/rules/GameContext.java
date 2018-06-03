package com.destrostudios.cards.shared.rules;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.entities.SimpleEntityData;
import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.events.EventDispatcher;
import com.destrostudios.cards.shared.events.EventQueue;
import com.destrostudios.cards.shared.rules.battle.*;
import com.destrostudios.cards.shared.rules.cards.*;
import com.destrostudios.cards.shared.rules.game.GameStartEvent;
import com.destrostudios.cards.shared.rules.game.SetStartingPlayerHandler;
import com.destrostudios.cards.shared.rules.game.phases.attack.*;
import com.destrostudios.cards.shared.rules.game.phases.block.EndBlockPhaseEvent;
import com.destrostudios.cards.shared.rules.game.phases.block.EndBlockPhaseHandler;
import com.destrostudios.cards.shared.rules.game.phases.block.StartBlockPhaseEvent;
import com.destrostudios.cards.shared.rules.game.phases.block.StartBlockPhaseHandler;
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
        addGameEventHandler(AddCardToHandEvent.class, new AddCardToHandHandler());
        addGameEventHandler(AddCardToLibraryEvent.class, new AddCardToLibraryHandler());
        addGameEventHandler(BattleEvent.class, new BattleHandler());
        addGameEventHandler(DamageEvent.class, new DamageHandler());
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
        addGameEventHandler(PlaySpellEvent.class, new PlaySpellHandler());
        addGameEventHandler(RemoveCardFromHandEvent.class, new RemoveCardFromHandHandler());
        addGameEventHandler(RemoveCardFromLibraryEvent.class, new RemoveCardFromLibraryHandler());
        addGameEventHandler(SetHealthEvent.class, new SetHealthHandler());
        addGameEventHandler(ShuffleLibraryEvent.class, new ShuffleLibraryHandler());
        addGameEventHandlers(StartAttackPhaseEvent.class, new StartAttackPhaseHandler());
        addGameEventHandler(StartBlockPhaseEvent.class, new StartBlockPhaseHandler());
        addGameEventHandlers(StartMainPhaseOneEvent.class,
                new StartMainPhaseOneHandler(),
                new DrawCardOnMainPhaseOneHandler());
        addGameEventHandler(StartMainPhaseTwoEvent.class, new StartMainPhaseTwoHandler());
        addGameEventHandler(DeclareAttackEvent.class, new DeclareAttackHandler());
        addGameEventHandler(DeclareBlockEvent.class, new DeclareBlockHandler());
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
