package com.destrostudios.cards.shared.rules;

import com.destrostudios.cards.shared.rules.game.phases.attack.DrawCardOnStartAttackPhaseHandler;
import com.destrostudios.cards.shared.rules.game.phases.main.EndMainPhaseEvent;
import com.destrostudios.cards.shared.rules.game.phases.main.EndMainPhaseHandler;
import com.destrostudios.cards.shared.rules.game.phases.block.EndBlockPhaseHandler;
import com.destrostudios.cards.shared.rules.game.phases.block.EndBlockPhaseEvent;
import com.destrostudios.cards.shared.rules.game.phases.attack.EndAttackPhaseHandler;
import com.destrostudios.cards.shared.rules.game.phases.attack.EndAttackPhaseEvent;
import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.entities.SimpleEntityData;
import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.events.EventDispatcher;
import com.destrostudios.cards.shared.events.EventQueue;
import com.destrostudios.cards.shared.rules.battle.*;
import com.destrostudios.cards.shared.rules.cards.*;
import com.destrostudios.cards.shared.rules.game.*;
import com.destrostudios.cards.shared.rules.game.phases.attack.StartAttackPhaseEvent;
import com.destrostudios.cards.shared.rules.game.phases.attack.StartAttackPhaseHandler;
import com.destrostudios.cards.shared.rules.game.phases.block.StartBlockPhaseEvent;
import com.destrostudios.cards.shared.rules.game.phases.block.StartBlockPhaseHandler;
import com.destrostudios.cards.shared.rules.game.phases.main.StartMainPhaseEvent;
import com.destrostudios.cards.shared.rules.game.phases.main.StartMainPhaseHandler;
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
        addGameEventHandlers(StartAttackPhaseEvent.class,
                new StartAttackPhaseHandler(),
                new DrawCardOnStartAttackPhaseHandler());
        addGameEventHandler(StartBlockPhaseEvent.class, new StartBlockPhaseHandler());
        addGameEventHandler(StartMainPhaseEvent.class, new StartMainPhaseHandler());
        addGameEventHandler(EndAttackPhaseEvent.class, new EndAttackPhaseHandler());
        addGameEventHandler(EndBlockPhaseEvent.class, new EndBlockPhaseHandler());
        addGameEventHandler(EndMainPhaseEvent.class, new EndMainPhaseHandler());
        addGameEventHandlers(GameStartEvent.class,
                new ShuffleAllLibrariesOnGameStartHandler(),
                new DrawCardsOnGameStartHandler(),
                new SetStartingPlayerHandler());
        addGameEventHandler(PlaySpellEvent.class, new PlaySpellHandler());
        addGameEventHandler(RemoveCardFromHandEvent.class, new RemoveCardFromHandHandler());
        addGameEventHandler(RemoveCardFromLibraryEvent.class, new RemoveCardFromLibraryHandler());
        addGameEventHandler(SetHealthEvent.class, new SetHealthHandler());
        addGameEventHandler(ShuffleLibraryEvent.class, new ShuffleLibraryHandler());
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
