package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.util.CostUtil;
import com.destrostudios.cards.shared.rules.util.SpellUtil;
import com.destrostudios.cards.shared.rules.util.TriggerUtil;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CastSpellHandler extends GameEventHandler<CastSpellEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(CastSpellHandler.class);

    @Override
    public void handle(CastSpellEvent event, NetworkRandom random) {
        LOG.info("Casting spell {} on {}", event.spell, event.targets);

        int caster = SpellUtil.getCaster(data, event.spell);

        Integer manaCost = CostUtil.getEffectiveManaCost(data, event.spell);
        if (manaCost != null) {
            int owner = data.getComponent(caster, Components.OWNED_BY);
            events.fire(new PayManaEvent(owner, manaCost), random);
        }

        TriggerUtil.trigger(data.getComponent(event.spell, Components.Spell.CAST_TRIGGERS), caster, event.targets, events, random);
    }
}
