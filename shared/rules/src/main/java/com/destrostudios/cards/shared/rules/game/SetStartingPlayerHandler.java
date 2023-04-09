package com.destrostudios.cards.shared.rules.game;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.game.turn.StartTurnEvent;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SetStartingPlayerHandler extends GameEventHandler<GameStartEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(SetStartingPlayerHandler.class);

    @Override
    public void handle(GameStartEvent event, NetworkRandom random) {
        List<Integer> players = data.query(Components.NEXT_PLAYER).list();
        int player = players.get(random.nextInt(players.size()));
        LOG.debug("Starting player is " + inspect(player));
        events.fire(new StartTurnEvent(player), random);
    }
}
