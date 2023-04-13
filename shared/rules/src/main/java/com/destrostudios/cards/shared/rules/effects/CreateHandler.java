package com.destrostudios.cards.shared.rules.effects;

import com.destrostudios.cards.shared.entities.templates.EntityTemplate;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.cards.zones.MoveToCreatureZoneEvent;
import com.destrostudios.cards.shared.rules.cards.zones.MoveToHandEvent;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateHandler extends GameEventHandler<CreateEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(CreateHandler.class);

    @Override
    public void handle(CreateEvent event, NetworkRandom random) {
        LOG.debug("Creating \"{}\" in {} for {} (source = {})", event.template, event.location, inspect(event.player), inspect(event.source));
        int card = EntityTemplate.createFromTemplate(data, event.template);
        data.setComponent(card, Components.FOIL, data.getComponent(event.source, Components.FOIL));
        data.setComponent(card, Components.OWNED_BY, event.player);
        switch (event.location) {
            case CREATURE_ZONE -> events.fire(new MoveToCreatureZoneEvent(card), random);
            case HAND -> events.fire(new MoveToHandEvent(card), random);
        }
    }
}
