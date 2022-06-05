package com.destrostudios.cards.shared.rules.game.turn;

import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.PlayerActionsGenerator;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;

import java.util.List;

public class ExecuteBotActionsOnTurnStartHandler extends GameEventHandler<StartTurnEvent> {

    private PlayerActionsGenerator playerActionsGenerator;

    @Override
    public void handle(StartTurnEvent event, NetworkRandom random) {
        if (event.player == 1) {
            if (playerActionsGenerator == null) {
                playerActionsGenerator = new PlayerActionsGenerator(data);
            }
            List<Event> possibleActions = playerActionsGenerator.generatePossibleActions(event.player);
            events.fire(possibleActions.get(0), random);
        }
    }
}
