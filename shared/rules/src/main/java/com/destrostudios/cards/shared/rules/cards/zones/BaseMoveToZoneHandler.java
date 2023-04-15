package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.entities.IntList;
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

    private ComponentDefinition<Void> cardZone;
    private ComponentDefinition<IntList> playerZone;
    private ComponentDefinition<Void>[] otherCardZones;
    private ComponentDefinition<IntList>[] otherPlayerZones;

    protected void handle(int card, NetworkRandom random) {
        LOG.debug("Moving {} to zone {}", inspect(card), cardZone.getName());

        int owner = data.getComponent(card, Components.OWNED_BY);

        for (int i = 0; i < otherCardZones.length; i++) {
            if (data.hasComponent(card, otherCardZones[i])) {
                ZoneUtil.removeFromZone(data, card, owner, otherCardZones[i], otherPlayerZones[i]);
                if (otherCardZones[i] == Components.CREATURE_ZONE) {
                    data.removeComponent(card, Components.BOARD);
                    events.fire(new RemovedFromCreatureZoneEvent(card), random);
                } else if (otherCardZones[i] == Components.HAND) {
                    events.fire(new RemovedFromHandEvent(card), random);
                }
                break;
            }
        }

        ZoneUtil.addToZone(data, card, owner, cardZone, playerZone);

        events.fire(new ConditionsAffectedEvent(), random);
    }
}
