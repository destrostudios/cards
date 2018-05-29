package com.destrostudios.cards.shared.rules.game;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.game.phases.main.StartMainPhaseEvent;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class SetStartingPlayerHandler extends GameEventHandler<GameStartEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(SetStartingPlayerHandler.class);

    @Override
    public void handle(GameStartEvent event) {
        List<Integer> players = data.query(Components.NEXT_PLAYER).list();
        int player = players.get(random.applyAsInt(players.size()));
        LOG.info("starting player is {}.", player);
        events.fireChainEvent(new StartMainPhaseEvent(player));
    }
}
