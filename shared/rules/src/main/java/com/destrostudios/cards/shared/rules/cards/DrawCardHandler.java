package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.cards.zones.AddCardToHandEvent;
import com.destrostudios.cards.shared.rules.cards.zones.RemoveCardFromLibraryEvent;
import com.destrostudios.cards.shared.rules.util.ZoneUtil;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DrawCardHandler extends GameEventHandler<DrawCardEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(DrawCardHandler.class);

    @Override
    public void handle(DrawCardEvent event, NetworkRandom random) {
        Integer drawnCard = ZoneUtil.getTopLibraryCard(data, event.player);
        if (drawnCard != null) {
            LOG.debug("Player {} is drawing card {}", inspect(event.player), inspect(drawnCard));
            events.fire(new RemoveCardFromLibraryEvent(drawnCard), random);
            events.fire(new AddCardToHandEvent(drawnCard), random);
        } else {
            // TODO: Fatigue?
            LOG.debug("Player {} tried to draw a card but has none left", inspect(event.player));
            event.cancel();
        }
    }
}
