package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.battle.ConditionsAffectedEvent;
import com.destrostudios.cards.shared.rules.util.ZoneUtil;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AllArgsConstructor
public abstract class BaseMoveToZoneHandler<T extends Event> extends GameEventHandler<T> {

    private static final Logger LOG = LoggerFactory.getLogger(BaseMoveToZoneHandler.class);

    private ComponentDefinition<Integer> zone;
    private ComponentDefinition<Integer>[] otherZones;

    protected void handle(int card, NetworkRandom random) {
        LOG.debug("Moving {} to zone {}", inspect(card), zone.getName());

        int owner = data.getComponent(card, Components.OWNED_BY);

        for (ComponentDefinition<Integer> otherZone : otherZones) {
            Integer cardZoneIndex = data.getComponent(card, otherZone);
            if (cardZoneIndex != null) {
                data.removeComponent(card, otherZone);
                for (int otherCard : data.list(otherZone, c -> ((data.getComponent(c, Components.OWNED_BY) == owner) && (data.getComponent(c, otherZone) > cardZoneIndex)))) {
                    data.setComponent(otherCard, otherZone, data.getComponent(otherCard, otherZone) - 1);
                }
                if (otherZone == Components.CREATURE_ZONE) {
                    data.removeComponent(card, Components.BOARD);
                    events.fire(new RemovedFromCreatureZoneEvent(card), random);
                } else if (otherZone == Components.HAND) {
                    events.fire(new RemovedFromHandEvent(card), random);
                }
                break;
            }
        }

        ZoneUtil.addToZone(data, card, owner, zone);

        events.fire(new ConditionsAffectedEvent(), random);
    }
}
