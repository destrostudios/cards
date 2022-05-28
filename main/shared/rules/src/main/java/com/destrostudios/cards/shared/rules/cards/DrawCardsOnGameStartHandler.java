package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.game.GameStartEvent;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Philipp
 */
public class DrawCardsOnGameStartHandler extends GameEventHandler<GameStartEvent> {

    private static final int HAND_SIZE = 5;
    private static final Logger LOG = LoggerFactory.getLogger(DrawCardsOnGameStartHandler.class);

    @Override
    public void handle(GameStartEvent event, NetworkRandom random) {
        LOG.info("drawing initial hands");
        for (int player : data.query(Components.NEXT_PLAYER).list()) {
            for (int i = 0; i < HAND_SIZE; i++) {
                events.fire(new DrawCardEvent(player), random);
            }
        }
    }
}
