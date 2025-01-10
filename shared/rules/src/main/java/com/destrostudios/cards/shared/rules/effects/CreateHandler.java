package com.destrostudios.cards.shared.rules.effects;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.entities.templates.EntityTemplate;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameContext;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.cards.zones.MoveToCreatureZoneEvent;
import com.destrostudios.cards.shared.rules.cards.zones.MoveToHandEvent;
import com.destrostudios.cards.shared.rules.util.TriggerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateHandler extends GameEventHandler<CreateEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(CreateHandler.class);

    @Override
    public void handle(GameContext context, CreateEvent event) {
        EntityData data = context.getData();
        LOG.debug("Creating \"{}\" in {} for {} (source = {})", event.template, event.location, inspect(data, event.player), inspect(data, event.source));
        int card = data.createEntity();
        EntityTemplate.loadTemplate(data, card, event.template);
        data.setComponent(card, Components.FOIL, data.getComponent(event.source, Components.FOIL));
        data.setComponent(card, Components.OWNED_BY, event.player);
        switch (event.location) {
            case CREATURE_ZONE -> context.getEvents().fire(new MoveToCreatureZoneEvent(card));
            case HAND -> context.getEvents().fire(new MoveToHandEvent(card));
        }
        TriggerUtil.triggerIfPossible(data, event.triggers, event.source, new int[] { card }, null, context.getEvents());
    }
}
