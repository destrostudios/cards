package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.util.CostUtil;
import com.destrostudios.cards.shared.rules.util.TriggerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CastSpellHandler extends GameEventHandler<CastSpellEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(CastSpellHandler.class);

    @Override
    public void handle(CastSpellEvent event) {
        LOG.debug("Casting spell {} on {}", inspect(event.spell), inspect(event.targets));

        Integer manaCost = CostUtil.getEffectiveManaCost(data, event.spell);
        if (manaCost != null) {
            int owner = data.getComponent(event.source, Components.OWNED_BY);
            events.fire(new PayManaEvent(owner, manaCost));
        }

        TriggerUtil.trigger(data.getComponent(event.spell, Components.Spell.CAST_TRIGGERS), event.source, event.targets, events);
    }
}
