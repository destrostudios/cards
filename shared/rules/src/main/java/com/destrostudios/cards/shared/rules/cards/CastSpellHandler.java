package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameContext;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.util.CostUtil;
import com.destrostudios.cards.shared.rules.util.TriggerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CastSpellHandler extends GameEventHandler<CastSpellEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(CastSpellHandler.class);

    @Override
    public void handle(GameContext context, CastSpellEvent event) {
        EntityData data = context.getData();
        LOG.debug("Casting spell {} on {}", inspect(data, event.spell), inspect(data, event.targets));

        Integer manaCost = CostUtil.getEffectiveManaCost(data, event.spell);
        if (manaCost != null) {
            int owner = data.getComponent(event.source, Components.OWNED_BY);
            context.getEvents().fire(new PayManaEvent(owner, manaCost));
        }

        TriggerUtil.triggerIfPossible(data, data.getComponent(event.spell, Components.Spell.CAST_TRIGGERS), event.source, event.targets, event.options, context.getEvents());
    }
}
