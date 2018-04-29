package com.destrostudios.cards.shared.rules.battle;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.events.EventHandler;
import com.destrostudios.cards.shared.events.EventQueue;
import com.destrostudios.cards.shared.rules.Components;
import org.slf4j.Logger;

/**
 *
 * @author Philipp
 */
public class ArmorEventHandler implements EventHandler<DamageEvent> {

    private final EntityData data;
    private final EventQueue events;
    private final Logger log;
    private final int armorKey = Components.ARMOR;

    public ArmorEventHandler(EntityData data, EventQueue events, Logger log) {
        this.data = data;
        this.events = events;
        this.log = log;
    }

    @Override
    public void onEvent(DamageEvent event) {
        int armor = data.getOrElse(event.target, armorKey, 0);
        log.info("reducing damage by {}", armor);
        event.damage -= armor;
    }

}
