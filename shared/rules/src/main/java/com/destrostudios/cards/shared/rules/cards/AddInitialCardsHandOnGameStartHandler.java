package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameConstants;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.cards.zones.AddCardToHandEvent;
import com.destrostudios.cards.shared.rules.game.GameStartEvent;
import com.destrostudios.cards.shared.rules.util.ZoneUtil;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class AddInitialCardsHandOnGameStartHandler extends GameEventHandler<GameStartEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(AddInitialCardsHandOnGameStartHandler.class);

    @Override
    public void handle(GameStartEvent event, NetworkRandom random) {
        for (int player : data.query(Components.NEXT_PLAYER).list()) {
            int initialHandSize = GameConstants.INITIAL_HAND_SIZE + (data.hasComponent(player, Components.Game.ACTIVE_PLAYER) ? 0 : 1);
            LOG.debug("Adding initial {} cards to hand of player {}", initialHandSize, inspect(player));
            List<Integer> remainingLibraryCards = data.query(Components.LIBRARY).list(card -> data.getComponent(card, Components.OWNED_BY) == player);
            for (int i = 0; i < initialHandSize; i++) {
                Integer card = ZoneUtil.getTopMostCard(data, remainingLibraryCards, Components.LIBRARY);
                if (card != null) {
                    remainingLibraryCards.remove(card);
                    events.fire(new AddCardToHandEvent(card), random);
                }
            }
        }
    }
}
