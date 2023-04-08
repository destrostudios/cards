package com.destrostudios.cards.shared.rules.effects;

import com.destrostudios.cards.shared.entities.templates.EntityTemplate;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.cards.zones.AddCardToCreatureZoneEvent;
import com.destrostudios.cards.shared.rules.cards.zones.AddCardToHandEvent;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateHandler extends GameEventHandler<CreateEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(CreateHandler.class);

    @Override
    public void handle(CreateEvent event, NetworkRandom random) {
        LOG.info("Creating \"" + event.template + "\" in " + event.location + " for " + inspect(event.player));
        int creature = EntityTemplate.createFromTemplate(data, event.template);
        data.setComponent(creature, Components.OWNED_BY, event.player);
        switch (event.location) {
            case CREATURE_ZONE -> events.fire(new AddCardToCreatureZoneEvent(creature), random);
            case HAND -> events.fire(new AddCardToHandEvent(creature), random);
        }
    }
}
