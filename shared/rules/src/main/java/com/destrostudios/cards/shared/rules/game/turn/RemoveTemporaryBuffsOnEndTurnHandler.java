package com.destrostudios.cards.shared.rules.game.turn;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.buffs.RemoveBuffEvent;
import com.destrostudios.cards.shared.rules.util.ArrayUtil;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class RemoveTemporaryBuffsOnEndTurnHandler extends GameEventHandler<EndTurnEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(RemoveTemporaryBuffsOnEndTurnHandler.class);

    @Override
    public void handle(EndTurnEvent event, NetworkRandom random) {
        List<Integer> buffs = data.query(Components.Buff.UNTIL_END_OF_TURN).list();
        List<Integer> targets = data.query(Components.BUFFS).list();
        for (int buff : buffs) {
            for (int target : targets) {
                if (ArrayUtil.contains(data, target, Components.BUFFS, buff)) {
                    LOG.info("Removing temporary buff " + inspect(buff) + " from " + inspect(target) + " at end of turn");
                    events.fire(new RemoveBuffEvent(target, buff), random);
                }
            }
        }
    }
}
