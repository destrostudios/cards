package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IncreaseCurrentCastsPerTurnHandler extends GameEventHandler<PlaySpellEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(IncreaseCurrentCastsPerTurnHandler.class);

    @Override
    public void handle(PlaySpellEvent event, NetworkRandom random) {
        int currentCasts = data.getOptionalComponent(event.spell, Components.Spell.CURRENT_CASTS_PER_TURN).orElse(0) + 1;
        LOG.info("Increasing current spell casts per turn of {} to {}", event.spell, currentCasts);
        data.setComponent(event.spell, Components.Spell.CURRENT_CASTS_PER_TURN, currentCasts);
    }
}