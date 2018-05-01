package com.destrostudios.cards.shared.rules;

import com.destrostudios.cards.shared.entities.EntityData;
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
import com.destrostudios.cards.shared.rules.cards.ShuffleLibraryEvent;
import com.destrostudios.cards.shared.rules.cards.ShuffleLibraryHandler;
import com.destrostudios.cards.shared.rules.cards.ShuffleAllLibrariesOnGameStartHandler;
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
        data = new EntityData();
        dispatcher = new EventDispatcher();
        events = new EventQueueImpl(dispatcher::fire);
        initListeners();
    }

    private void initListeners() {
        dispatcher.addListeners(DamageEvent.class, new DamageHandler(data, events));
        dispatcher.addListeners(SetHealthEvent.class, new SetHealthHandler(data, events));
        dispatcher.addListeners(GameStartEvent.class, new ShuffleAllLibrariesOnGameStartHandler(data, events));
        dispatcher.addListeners(ShuffleLibraryEvent.class, new ShuffleLibraryHandler(data, events, random));
        dispatcher.addListeners(DrawCardEvent.class, new DrawCardHandler(data, events));
        dispatcher.addListeners(AddCardToHandEvent.class, new AddCardToHandHandler(data, events));
        dispatcher.addListeners(RemoveCardFromHandEvent.class, new RemoveCardFromHandHandler(data, events));
        dispatcher.addListeners(AddCardToLibraryEvent.class, new AddCardToLibraryHandler(data, events));
        dispatcher.addListeners(RemoveCardFromLibraryEvent.class, new RemoveCardFromLibraryHandler(data, events));
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
