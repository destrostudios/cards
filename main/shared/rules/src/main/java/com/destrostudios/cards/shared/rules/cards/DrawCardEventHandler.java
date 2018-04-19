package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.entities.ComponentValue;
import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.events.EventHandler;
import com.destrostudios.cards.shared.events.EventQueue;
import java.util.Comparator;
import java.util.Optional;
import org.slf4j.Logger;

/**
 *
 * @author Philipp
 */
public class DrawCardEventHandler implements EventHandler<DrawCardEvent> {

    private final EntityData data;
    private final EventQueue events;
    private final Logger log;
    private final int libraryKey, ownedByKey;

    public DrawCardEventHandler(EntityData data, EventQueue events, Logger log, int libraryKey, int ownedByKey) {
        this.data = data;
        this.events = events;
        this.log = log;
        this.libraryKey = libraryKey;
        this.ownedByKey = ownedByKey;
    }

    @Override
    public void onEvent(DrawCardEvent event) {
        Optional<ComponentValue> max = data.entityComponentValues(libraryKey, x -> data.hasValue(x.getEntity(), ownedByKey, event.player)).stream()
                .max(Comparator.comparingInt(x -> x.getComponentValue()));
        if(max.isPresent()) {
            event.card = max.get().getEntity();
            log.info("player {} is drawing card {}", event.player, event.card);
            events.response(new RemoveCardFromLibraryEvent(event.card));
            events.response(new AddCardToHandEvent(event.card));
        } else {
            //fatigue
            log.info("player {} tried to draw a card but has none left", event.player);
            event.cancel();
        }
    }

}
