package com.destrostudios.cards.shared.rules.battle;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.events.EventHandler;
import com.destrostudios.cards.shared.events.EventQueue;
import com.destrostudios.cards.shared.rules.Components;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class ArmorEventHandler implements EventHandler<DamageEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(ArmorEventHandler.class);

    private final EntityData data;
    private final EventQueue events;
    private final int armorKey = Components.ARMOR;

    public ArmorEventHandler(EntityData data, EventQueue events) {
        this.data = data;
        this.events = events;
    }

    @Override
    public void onEvent(DamageEvent event) {
        int armor = data.getOrElse(event.target, armorKey, 0);
        LOG.info("reducing damage by {}", armor);
        event.damage -= armor;
    }

}
