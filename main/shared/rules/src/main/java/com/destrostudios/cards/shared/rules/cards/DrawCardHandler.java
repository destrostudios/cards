package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Philipp
 */
public class DrawCardHandler extends GameEventHandler<DrawCardEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(DrawCardHandler.class);

    @Override
    public void handle(DrawCardEvent event) {
        List<Integer> library = data.query(Components.LIBRARY).list(entity -> data.hasComponentValue(entity, Components.OWNED_BY, event.player));
        if (!library.isEmpty()) {
            int card = library.get(0);
            for (int i = 1; i < library.size(); i++) {
                int candidate = library.get(i);
                if (data.getComponent(candidate, Components.LIBRARY) > data.getComponent(card, Components.LIBRARY)) {
                    card = candidate;
                }
            }
            LOG.info("player {} is drawing card {}", event.player, card);
            events.fireSubEvent(new RemoveCardFromLibraryEvent(card));
            events.fireSubEvent(new AddCardToHandEvent(card));
        } else {
            //fatigue
            LOG.info("player {} tried to draw a card but has none left", event.player);
            event.cancel();
        }
    }

}
