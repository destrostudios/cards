package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameContext;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.cards.zones.MoveToHandEvent;
import com.destrostudios.cards.shared.rules.game.GameOverEvent;
import com.destrostudios.cards.shared.rules.util.ZoneUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DrawCardHandler extends GameEventHandler<DrawCardEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(DrawCardHandler.class);

    @Override
    public void handle(GameContext context, DrawCardEvent event) {
        EntityData data = context.getData();
        Integer card = ZoneUtil.getTopLibraryCard(data, event.player);
        if (card != null) {
            LOG.debug("Player {} is drawing card {}", inspect(data, event.player), inspect(data, card));
            context.getEvents().fire(new MoveToHandEvent(card));
        } else {
            LOG.debug("Player {} tried to draw a card but has none left", inspect(data, event.player));
            int opponent = data.getComponent(event.player, Components.NEXT_PLAYER);
            context.getEvents().fireWithoutParent(new GameOverEvent(opponent));
            event.cancel();
        }
    }
}
