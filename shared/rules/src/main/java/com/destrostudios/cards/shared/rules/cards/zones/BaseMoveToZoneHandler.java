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
    private ComponentDefinition<Void>[] cardPlayerZone;
    private ComponentDefinition<Void>[] otherCardZones;
    private ComponentDefinition<Void>[][] otherCardPlayerZones;
    private ComponentDefinition<IntList> playerZoneCards;
    private ComponentDefinition<IntList>[] otherPlayerZonesCards;

    protected void handle(int card) {
        LOG.debug("Moving {} to zone {}", inspect(card), cardZone.getName());

        int owner = data.getComponent(card, Components.OWNED_BY);

        for (int i = 0; i < otherCardZones.length; i++) {
            ComponentDefinition<Void> otherCardZone = otherCardZones[i];
            if (data.hasComponent(card, otherCardZone)) {
                ZoneUtil.removeFromZone(data, card, owner, otherCardZone, otherCardPlayerZones[i][owner], otherPlayerZonesCards[i]);
                if (otherCardZone == Components.Zone.CREATURE_ZONE) {
                    data.removeComponent(card, Components.BOARD);
                    events.fire(new RemovedFromCreatureZoneEvent(card));
                } else if (otherCardZone == Components.Zone.HAND) {
                    events.fire(new RemovedFromHandEvent(card));
                }
                break;
            }
        }

        ZoneUtil.addToZone(data, card, owner, cardZone, cardPlayerZone[owner], playerZoneCards);

        events.fire(new ConditionsAffectedEvent());
    }
}
