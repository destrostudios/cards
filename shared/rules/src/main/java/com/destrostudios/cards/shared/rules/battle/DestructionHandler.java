package com.destrostudios.cards.shared.rules.battle;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.cards.zones.AddCardToGraveyardEvent;
import com.destrostudios.cards.shared.rules.game.GameOverEvent;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class DestructionHandler extends GameEventHandler<DestructionEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(DestructionHandler.class);

    @Override
    public void handle(DestructionEvent event, NetworkRandom random) {
        LOG.info("{} destroyed", event.target);
        Optional<Integer> nextPlayer = data.getOptionalComponent(event.target, Components.NEXT_PLAYER);
        if (nextPlayer.isPresent()) {
            int winner = nextPlayer.get();
            LOG.info("Game over: Winner = " + winner);
            events.fire(new GameOverEvent(winner), random);
        } else {
            events.fire(new AddCardToGraveyardEvent(event.target), random);
        }
    }
}
