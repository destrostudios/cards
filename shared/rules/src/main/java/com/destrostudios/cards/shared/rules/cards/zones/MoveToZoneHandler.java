package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.battle.ConditionsAffectedEvent;
import com.destrostudios.cards.shared.rules.util.ZoneUtil;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MoveToZoneHandler extends GameEventHandler<MoveToZoneEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(MoveToZoneHandler.class);

    @Override
    public void handle(MoveToZoneEvent event, NetworkRandom random) {
        LOG.debug("Moving {} to zone {}", inspect(event.card), event.zone.getName());
        ZoneUtil.addCardToZone(data, event.card, event.zone);
        events.fire(new ConditionsAffectedEvent(), random);
    }
}
