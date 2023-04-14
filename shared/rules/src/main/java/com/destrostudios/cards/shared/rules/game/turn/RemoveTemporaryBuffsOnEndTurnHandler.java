package com.destrostudios.cards.shared.rules.game.turn;

import com.destrostudios.cards.shared.entities.IntList;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.buffs.RemoveBuffEvent;
import com.destrostudios.cards.shared.rules.util.ArrayUtil;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RemoveTemporaryBuffsOnEndTurnHandler extends GameEventHandler<EndTurnEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(RemoveTemporaryBuffsOnEndTurnHandler.class);

    @Override
    public void handle(EndTurnEvent event, NetworkRandom random) {
        IntList buffs = data.list(Components.Buff.UNTIL_END_OF_TURN);
        IntList targets = data.list(Components.BUFFS);
        for (int buff : buffs) {
            for (int target : targets) {
                if (ArrayUtil.contains(data, target, Components.BUFFS, buff)) {
                    LOG.debug("Removing temporary buff {} from {} at end of turn", inspect(buff), inspect(target));
                    events.fire(new RemoveBuffEvent(target, buff), random);
                }
            }
        }
    }
}
