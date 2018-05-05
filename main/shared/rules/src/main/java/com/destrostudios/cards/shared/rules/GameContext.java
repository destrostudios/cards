package com.destrostudios.cards.shared.rules;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.entities.SimpleEntityData;
import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.events.EventDispatcher;
import com.destrostudios.cards.shared.events.EventQueue;
import com.destrostudios.cards.shared.events.EventQueueImpl;
import com.destrostudios.cards.shared.rules.battle.DamageEvent;
import com.destrostudios.cards.shared.rules.battle.DamageHandler;
import com.destrostudios.cards.shared.rules.battle.SetHealthEvent;
import com.destrostudios.cards.shared.rules.battle.SetHealthHandler;
import com.destrostudios.cards.shared.rules.cards.AddCardToHandEvent;
import com.destrostudios.cards.shared.rules.cards.AddCardToHandHandler;
import com.destrostudios.cards.shared.rules.cards.AddCardToLibraryEvent;
import com.destrostudios.cards.shared.rules.cards.AddCardToLibraryHandler;
import com.destrostudios.cards.shared.rules.cards.DrawCardEvent;
import com.destrostudios.cards.shared.rules.cards.DrawCardHandler;
import com.destrostudios.cards.shared.rules.cards.RemoveCardFromHandEvent;
import com.destrostudios.cards.shared.rules.cards.RemoveCardFromHandHandler;
import com.destrostudios.cards.shared.rules.cards.RemoveCardFromLibraryEvent;
import com.destrostudios.cards.shared.rules.cards.RemoveCardFromLibraryHandler;
import com.destrostudios.cards.shared.rules.cards.ShuffleAllLibrariesOnGameStartHandler;
import com.destrostudios.cards.shared.rules.cards.ShuffleLibraryEvent;
import com.destrostudios.cards.shared.rules.cards.ShuffleLibraryHandler;
import com.destrostudios.cards.shared.rules.game.GameStartEvent;
import java.util.function.IntUnaryOperator;

/**
 *
 * @author Philipp
 */
public class GameContext {

    private final EntityData data;
    private final EventDispatcher dispatcher;
    private final EventQueue events;
    private final IntUnaryOperator random;

    public GameContext(IntUnaryOperator random) {
        this.random = random;
        data = new SimpleEntityData();
        dispatcher = new EventDispatcher();
        events = new EventQueueImpl(dispatcher::fire);
        initListeners();
    }

    public GameContext(EntityData data, EventDispatcher dispatcher, EventQueue events, IntUnaryOperator random) {
        this.data = data;
        this.dispatcher = dispatcher;
        this.events = events;
        this.random = random;
        initListeners();
    }

    private void initListeners() {
        addGameEventHandler(DamageEvent.class, new DamageHandler());
        addGameEventHandler(SetHealthEvent.class, new SetHealthHandler());
        addGameEventHandler(GameStartEvent.class, new ShuffleAllLibrariesOnGameStartHandler());
        addGameEventHandler(ShuffleLibraryEvent.class, new ShuffleLibraryHandler());
        addGameEventHandler(DrawCardEvent.class, new DrawCardHandler());
        addGameEventHandler(AddCardToHandEvent.class, new AddCardToHandHandler());
        addGameEventHandler(RemoveCardFromHandEvent.class, new RemoveCardFromHandHandler());
        addGameEventHandler(AddCardToLibraryEvent.class, new AddCardToLibraryHandler());
        addGameEventHandler(RemoveCardFromLibraryEvent.class, new RemoveCardFromLibraryHandler());
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

    public EventDispatcher getDispatcher() {
        return dispatcher;
    }

    public EventQueue getEvents() {
        return events;
    }

    public IntUnaryOperator getRandom() {
        return random;
    }

}
