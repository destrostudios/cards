package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.cards.zones.AddCardToHandEvent;
import com.destrostudios.cards.shared.rules.cards.zones.AddCardToLibraryEvent;
import com.destrostudios.cards.shared.rules.game.turn.StartTurnEvent;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

public class MulliganHandler extends GameEventHandler<MulliganEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(MulliganHandler.class);

    @Override
    public void handle(MulliganEvent event, NetworkRandom random) {
        int player = data.query(Components.Game.ACTIVE_PLAYER).unique();
        LOG.debug("Player {} is mulliganing away cards {}", inspect(player), inspect(event.cards));
        if (event.cards.length > 0) {
            List<Integer> remainingLibraryCards = data.query(Components.LIBRARY).list(card -> data.getComponent(card, Components.OWNED_BY) == player);
            LinkedList<Integer> newCards = new LinkedList<>();
            for (int i = 0; i < event.cards.length; i++) {
                int newCard = remainingLibraryCards.remove(random.nextInt(remainingLibraryCards.size()));
                newCards.add(newCard);
            }
            LOG.debug("Player {} is getting mulliganed new cards {}", inspect(player), inspect(newCards));
            for (int card : event.cards) {
                events.fire(new AddCardToLibraryEvent(card), random);
            }
            events.fire(new ShuffleLibraryEvent(player), random);
            for (int card : newCards) {
                events.fire(new AddCardToHandEvent(card), random);
            }
        }
        data.removeComponent(player, Components.Game.MULLIGAN);
        data.removeComponent(player, Components.Game.ACTIVE_PLAYER);
        int nextPlayer = data.getComponent(player, Components.NEXT_PLAYER);
        if (data.hasComponent(nextPlayer, Components.Game.MULLIGAN)) {
            LOG.debug("Setting next player {} ready for mulligan", inspect(nextPlayer));
            data.setComponent(nextPlayer, Components.Game.ACTIVE_PLAYER);
        } else {
            LOG.debug("Mulligan phase finished");
            events.fire(new StartTurnEvent(nextPlayer), random);
        }
    }
}
