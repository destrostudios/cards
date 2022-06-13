package com.destrostudios.cards.shared.rules.effects;

import com.destrostudios.cards.shared.entities.templates.EntityTemplate;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.cards.zones.AddCardToCreatureZoneEvent;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SummonHandler extends GameEventHandler<SummonEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(SummonHandler.class);

    @Override
    public void handle(SummonEvent event, NetworkRandom random) {
        LOG.info("Summoning \"" + event.template + "\" for {}", event.template, event.player);
        int creature = EntityTemplate.createFromTemplate(data, event.template);
        data.setComponent(creature, Components.OWNED_BY, event.player);
        events.fire(new AddCardToCreatureZoneEvent(creature), random);
    }
}
