package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.entities.collections.IntArrayList;
import com.destrostudios.cards.shared.events.EventHandler;
import com.destrostudios.cards.shared.events.EventQueue;
import com.destrostudios.cards.shared.rules.Components;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Philipp
 */
public class DrawCardHandler implements EventHandler<DrawCardEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(DrawCardHandler.class);

    private final EntityData data;
    private final EventQueue events;

    public DrawCardHandler(EntityData data, EventQueue events) {
        this.data = data;
        this.events = events;
    }

    @Override
    public void onEvent(DrawCardEvent event) {
        IntArrayList library = data.entities(Components.LIBRARY, entity -> data.hasValue(entity, Components.OWNED_BY, event.player));
        if (library.size() != 0) {
            int card = library.get(0);
            for (int i = 1; i < library.size(); i++) {
                int candidate = library.get(i);
                if (data.getComponent(candidate, Components.LIBRARY) > data.getComponent(card, Components.LIBRARY)) {
                    card = candidate;
                }
            }
            LOG.info("player {} is drawing card {}", event.player, card);
            events.fireSubevent(new RemoveCardFromLibraryEvent(card));
            events.fireSubevent(new AddCardToHandEvent(card));
        } else {
            //fatigue
            LOG.info("player {} tried to draw a card but has none left", event.player);
            event.cancel();
        }
    }

}
