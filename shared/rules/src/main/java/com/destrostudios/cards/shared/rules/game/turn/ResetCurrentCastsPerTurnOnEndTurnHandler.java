package com.destrostudios.cards.shared.rules.game.turn;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameContext;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResetCurrentCastsPerTurnOnEndTurnHandler extends GameEventHandler<EndTurnEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(ResetCurrentCastsPerTurnOnEndTurnHandler.class);

    @Override
    public void handle(GameContext context, EndTurnEvent event) {
        EntityData data = context.getData();
        LOG.debug("Resetting current casts per turn at end of turn");
        for (int entity : data.list(Components.Spell.CURRENT_CASTS_PER_TURN)) {
            data.removeComponent(entity, Components.Spell.CURRENT_CASTS_PER_TURN);
        }
    }
}
