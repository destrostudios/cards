package com.destrostudios.cards.shared.rules.buffs;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.entities.IntList;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameContext;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.cards.zones.RemovedFromHandEvent;
import com.destrostudios.cards.shared.rules.util.SpellUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RemoveDefaultPlaySpellBuffsOnRemovedFromHandHandler extends GameEventHandler<RemovedFromHandEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(RemoveDefaultPlaySpellBuffsOnRemovedFromHandHandler.class);

    @Override
    public void handle(GameContext context, RemovedFromHandEvent event) {
        EntityData data = context.getData();
        int defaultPlaySpell = SpellUtil.getDefaultPlaySpell(data, event.card);
        IntList buffs = data.getComponent(defaultPlaySpell, Components.BUFFS);
        if (buffs != null) {
            LOG.debug("Removing all {} buffs from default play spell {} of {}", buffs.size(), inspect(data, defaultPlaySpell), inspect(data, event.card));
            for (int buff : buffs) {
                context.getEvents().fire(new RemoveBuffEvent(defaultPlaySpell, buff));
            }
        }
    }
}
