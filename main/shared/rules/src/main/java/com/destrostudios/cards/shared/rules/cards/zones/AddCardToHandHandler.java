package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddCardToHandHandler extends GameEventHandler<AddCardToHandEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(AddCardToHandHandler.class);

    @Override
    public void handle(AddCardToHandEvent event) {
        events.fireSubEvent(new AddCardToZoneEvent(event.card, Components.HAND_CARDS));
        LOG.info("added {} to hand", event.card);
    }
}
