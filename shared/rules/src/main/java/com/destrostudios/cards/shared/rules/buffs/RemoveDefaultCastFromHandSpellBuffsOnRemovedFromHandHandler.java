package com.destrostudios.cards.shared.rules.buffs;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.cards.zones.RemovedFromHandEvent;
import com.destrostudios.cards.shared.rules.util.SpellUtil;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RemoveDefaultCastFromHandSpellBuffsOnRemovedFromHandHandler extends GameEventHandler<RemovedFromHandEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(RemoveDefaultCastFromHandSpellBuffsOnRemovedFromHandHandler.class);

    @Override
    public void handle(RemovedFromHandEvent event, NetworkRandom random) {
        int defaultCastFromHandSpell = SpellUtil.getDefaultCastFromHandSpell(data, event.card);
        int[] buffs = data.getComponent(defaultCastFromHandSpell, Components.BUFFS);
        if (buffs != null) {
            LOG.debug("Removing all {} buffs from defaultCastFromHandSpell {} of {}", buffs.length, inspect(defaultCastFromHandSpell), inspect(event.card));
            for (int buff : buffs) {
                events.fire(new RemoveBuffEvent(defaultCastFromHandSpell, buff), random);
            }
        }
    }
}
