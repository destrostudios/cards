package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.util.CostUtil;
import com.destrostudios.cards.shared.rules.util.TriggerUtil;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.IntStream;

public class CastSpellHandler extends GameEventHandler<CastSpellEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(CastSpellHandler.class);

    @Override
    public void handle(CastSpellEvent event, NetworkRandom random) {
        LOG.info("Casting spell {} on {}", event.spell, event.targets);

        int card = data.query(Components.SPELLS)
                .unique(currentCardEntity -> IntStream.of(data.getComponent(currentCardEntity, Components.SPELLS))
                .anyMatch(entity -> entity == event.spell)).getAsInt();

        Integer manaCost = CostUtil.getEffectiveManaCost(data, event.spell);
        if (manaCost != null) {
            int owner = data.getComponent(card, Components.OWNED_BY);
            events.fire(new PayManaEvent(owner, manaCost), random);
        }

        TriggerUtil.trigger(data.getComponent(event.spell, Components.Spell.CAST_TRIGGERS), card, event.targets, events, random);
    }
}
