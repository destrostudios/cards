package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.entities.IntList;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.cards.zones.MoveToHandEvent;
import com.destrostudios.cards.shared.rules.cards.zones.MoveToLibraryEvent;
import com.destrostudios.cards.shared.rules.game.turn.StartTurnEvent;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MulliganHandler extends GameEventHandler<MulliganEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(MulliganHandler.class);

    @Override
    public void handle(MulliganEvent event, NetworkRandom random) {
        int player = data.query(Components.Player.ACTIVE_PLAYER).unique();
        LOG.debug("Player {} is mulliganing away cards {}", inspect(player), inspect(event.cards));
        if (event.cards.length > 0) {
            IntList remainingLibraryCards = data.query(Components.LIBRARY).list(card -> data.getComponent(card, Components.OWNED_BY) == player);
            IntList newCards = new IntList(event.cards.length);
            for (int i = 0; i < event.cards.length; i++) {
                int newCard = remainingLibraryCards.removeAt(random.nextInt(remainingLibraryCards.size()));
                newCards.add(newCard);
            }
            LOG.debug("Player {} is getting mulliganed new cards {}", inspect(player), inspect(newCards));
            for (int card : event.cards) {
                events.fire(new MoveToLibraryEvent(card), random);
            }
            events.fire(new ShuffleLibraryEvent(player), random);
            for (int card : newCards) {
                events.fire(new MoveToHandEvent(card), random);
            }
        }
        data.removeComponent(player, Components.Player.MULLIGAN);
        data.removeComponent(player, Components.Player.ACTIVE_PLAYER);
        int nextPlayer = data.getComponent(player, Components.NEXT_PLAYER);
        if (data.hasComponent(nextPlayer, Components.Player.MULLIGAN)) {
            LOG.debug("Setting next player {} ready for mulligan", inspect(nextPlayer));
            data.setComponent(nextPlayer, Components.Player.ACTIVE_PLAYER);
        } else {
            LOG.debug("Mulligan phase finished");
            events.fire(new StartTurnEvent(nextPlayer), random);
        }
    }
}
