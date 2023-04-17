package com.destrostudios.cards.shared.rules.game.turn;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.entities.IntList;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameContext;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.buffs.RemoveBuffEvent;
import com.destrostudios.cards.shared.rules.util.ArrayUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RemoveTemporaryBuffsOnEndTurnHandler extends GameEventHandler<EndTurnEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(RemoveTemporaryBuffsOnEndTurnHandler.class);

    @Override
    public void handle(GameContext context, EndTurnEvent event) {
        EntityData data = context.getData();
        IntList buffs = data.list(Components.Buff.UNTIL_END_OF_TURN);
        IntList targets = data.list(Components.BUFFS);
        for (int buff : buffs) {
            for (int target : targets) {
                if (ArrayUtil.contains(data, target, Components.BUFFS, buff)) {
                    LOG.debug("Removing temporary buff {} from {} at end of turn", inspect(data, buff), inspect(data, target));
                    context.getEvents().fire(new RemoveBuffEvent(target, buff));
                }
            }
        }
    }
}
