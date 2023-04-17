package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameContext;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IncreaseCurrentCastsPerTurnHandler extends GameEventHandler<CastSpellEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(IncreaseCurrentCastsPerTurnHandler.class);

    @Override
    public void handle(GameContext context, CastSpellEvent event) {
        EntityData data = context.getData();
        int currentCasts = data.getOptionalComponent(event.spell, Components.Spell.CURRENT_CASTS_PER_TURN).orElse(0) + 1;
        LOG.debug("Increasing current spell casts per turn of {} to {}", inspect(data, event.spell), currentCasts);
        data.setComponent(event.spell, Components.Spell.CURRENT_CASTS_PER_TURN, currentCasts);
    }
}
