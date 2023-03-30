package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.battle.ConditionsAffectedEvent;
import com.destrostudios.cards.shared.rules.util.ZoneUtil;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;

public class AddCardToZoneHandler extends GameEventHandler<AddCardToZoneEvent> {

    @Override
    public void handle(AddCardToZoneEvent event, NetworkRandom random) {
        ZoneUtil.addCardToZone(data, event.card, event.zone);
        events.fire(new ConditionsAffectedEvent(), random);
    }
}
