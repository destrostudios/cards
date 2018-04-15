package com.etherblood.cards.rules.battle;

import com.etherblood.cards.entities.EntityData;
import com.etherblood.cards.events.EventHandler;
import com.etherblood.cards.events.EventQueue;
import org.slf4j.Logger;

/**
 *
 * @author Philipp
 */
public class ArmorEventHandler implements EventHandler<DamageEvent> {

    private final EntityData data;
    private final EventQueue events;
    private final Logger log;
    private final int armorKey;

    public ArmorEventHandler(EntityData data, EventQueue events, Logger log, int armorKey) {
        this.data = data;
        this.events = events;
        this.log = log;
        this.armorKey = armorKey;
    }

    @Override
    public void onEvent(DamageEvent event) {
        int armor = data.getOrElse(event.target, armorKey, 0);
        log.info("reducing damage by {}", armor);
        event.damage -= armor;
    }

}
