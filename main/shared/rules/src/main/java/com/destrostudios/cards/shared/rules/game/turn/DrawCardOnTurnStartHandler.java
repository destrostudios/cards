package com.destrostudios.cards.shared.rules.game.turn;

import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.cards.DrawCardEvent;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;

public class DrawCardOnTurnStartHandler extends GameEventHandler<StartTurnEvent> {

    @Override
    public void handle(StartTurnEvent event, NetworkRandom random) {
        events.fire(new DrawCardEvent(event.player), random);
    }
}
