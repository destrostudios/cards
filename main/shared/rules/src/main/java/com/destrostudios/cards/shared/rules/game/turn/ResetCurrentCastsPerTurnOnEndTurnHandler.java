package com.destrostudios.cards.shared.rules.game.turn;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;

public class ResetCurrentCastsPerTurnOnEndTurnHandler extends GameEventHandler<EndTurnEvent> {

    @Override
    public void handle(EndTurnEvent event, NetworkRandom random) {
        for (int entity : data.query(Components.Spell.CURRENT_CASTS_PER_TURN).list()) {
            data.removeComponent(entity, Components.Spell.CURRENT_CASTS_PER_TURN);
        }
    }
}
