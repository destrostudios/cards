package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.entities.IntList;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.battle.ConditionsAffectedEvent;
import com.destrostudios.cards.shared.rules.game.turn.StartTurnEvent;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MulliganHandler extends GameEventHandler<MulliganEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(MulliganHandler.class);

    @Override
    public void handle(MulliganEvent event, NetworkRandom random) {
        int player = data.unique(Components.Player.ACTIVE_PLAYER);
        LOG.debug("Player {} is mulliganing away cards {}", inspect(player), inspect(event.cards));
        if (event.cards.length > 0) {
            IntList handCards = data.getComponent(player, Components.Player.HAND_CARDS);
            IntList libraryCards = data.getComponent(player, Components.Player.LIBRARY_CARDS);
            IntList newHandCards = handCards.copy();
            IntList newLibraryCards = libraryCards.copy();
            int newCardsCount = Math.min(event.cards.length, libraryCards.size());
            for (int i = 0; i < newCardsCount; i++) {
                // Library is still shuffled from the initial shuffling, so we can simply take any N cards
                int newCard = newLibraryCards.removeLast();
                LOG.debug("Player {} is getting mulliganed new card {}", inspect(player), inspect(newCard));
                data.removeComponent(newCard, Components.LIBRARY);
                data.setComponent(newCard, Components.HAND);
                newHandCards.add(newCard);
            }
            for (int card : event.cards) {
                data.removeComponent(card, Components.HAND);
                data.setComponent(card, Components.LIBRARY);
                newHandCards.removeFirstUnsafe(card);
                newLibraryCards.add(card);
            }
            newLibraryCards.shuffle(random::nextInt);
            data.setComponent(player, Components.Player.LIBRARY_CARDS, newLibraryCards);
            data.setComponent(player, Components.Player.HAND_CARDS, newHandCards);
            events.fire(new ConditionsAffectedEvent(), random);
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
