package com.destrostudios.cards.shared.rules.buffs;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameContext;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.cards.zones.RemovedFromHandEvent;
import com.destrostudios.cards.shared.rules.util.SpellUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RemoveDefaultCastFromHandSpellBuffsOnRemovedFromHandHandler extends GameEventHandler<RemovedFromHandEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(RemoveDefaultCastFromHandSpellBuffsOnRemovedFromHandHandler.class);

    @Override
    public void handle(GameContext context, RemovedFromHandEvent event) {
        EntityData data = context.getData();
        int defaultCastFromHandSpell = SpellUtil.getDefaultCastFromHandSpell(data, event.card);
        int[] buffs = data.getComponent(defaultCastFromHandSpell, Components.BUFFS);
        if (buffs != null) {
            LOG.debug("Removing all {} buffs from defaultCastFromHandSpell {} of {}", buffs.length, inspect(data, defaultCastFromHandSpell), inspect(data, event.card));
            for (int buff : buffs) {
                context.getEvents().fire(new RemoveBuffEvent(defaultCastFromHandSpell, buff));
            }
        }
    }
}
