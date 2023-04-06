package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.battle.ConditionsAffectedEvent;
import com.destrostudios.cards.shared.rules.util.ZoneUtil;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddCardToZoneHandler extends GameEventHandler<AddCardToZoneEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(AddCardToZoneHandler.class);

    @Override
    public void handle(AddCardToZoneEvent event, NetworkRandom random) {
        LOG.info("Adding " + inspect(event.card) + " to zone " + event.zone.getName());
        ZoneUtil.addCardToZone(data, event.card, event.zone);
        events.fire(new ConditionsAffectedEvent(), random);
    }
}
