package com.destrostudios.cards.shared.rules.effects;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.rules.CreateLocation;
import com.destrostudios.cards.shared.rules.GameContext;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DiscoverHandler extends GameEventHandler<DiscoverEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(DiscoverHandler.class);

    @Override
    public void handle(GameContext context, DiscoverEvent event) {
        EntityData data = context.getData();
        String discoverTemplate = context.getDiscoverTemplates(event.pool)[event.discoverIndex];
        LOG.debug("Discovering \"{}\" for {} (source = {})", discoverTemplate, inspect(data, event.player), inspect(data, event.source));
        context.getEvents().fire(new CreateEvent(event.source, event.player, discoverTemplate, CreateLocation.HAND, event.triggers));
    }
}
