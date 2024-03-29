package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.entities.IntList;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameContext;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.battle.ConditionsAffectedEvent;
import com.destrostudios.cards.shared.rules.game.turn.StartTurnEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MulliganHandler extends GameEventHandler<MulliganEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(MulliganHandler.class);

    @Override
    public void handle(GameContext context, MulliganEvent event) {
        EntityData data = context.getData();
        int player = data.unique(Components.Player.ACTIVE_PLAYER);
        LOG.debug("Player {} is mulliganing away cards {}", inspect(data, player), inspect(data, event.cards));
        if (event.cards.length > 0) {
            IntList handCards = data.getComponent(player, Components.Player.HAND_CARDS);
            IntList libraryCards = data.getComponent(player, Components.Player.LIBRARY_CARDS);
            IntList newHandCards = handCards.copy();
            IntList newLibraryCards = libraryCards.copy();
            int newCardsCount = Math.min(event.cards.length, libraryCards.size());
            for (int i = 0; i < newCardsCount; i++) {
                // Library is still shuffled from the initial shuffling, so we can simply take any N cards
                int newCard = newLibraryCards.removeLast();
                LOG.debug("Player {} is getting mulliganed new card {}", inspect(data, player), inspect(data, newCard));
                data.removeComponent(newCard, Components.Zone.LIBRARY);
                data.removeComponent(newCard, Components.Zone.PLAYER_LIBRARY[player]);
                data.setComponent(newCard, Components.Zone.HAND);
                data.setComponent(newCard, Components.Zone.PLAYER_HAND[player]);
                newHandCards.add(newCard);
            }
            for (int card : event.cards) {
                data.removeComponent(card, Components.Zone.HAND);
                data.removeComponent(card, Components.Zone.PLAYER_HAND[player]);
                data.setComponent(card, Components.Zone.LIBRARY);
                data.setComponent(card, Components.Zone.PLAYER_LIBRARY[player]);
                newHandCards.removeFirstUnsafe(card);
                newLibraryCards.add(card);
            }
            newLibraryCards.shuffle(max -> context.getRandom().nextInt(max));
            data.setComponent(player, Components.Player.LIBRARY_CARDS, newLibraryCards);
            data.setComponent(player, Components.Player.HAND_CARDS, newHandCards);
            context.getEvents().fire(new ConditionsAffectedEvent());
        }
        data.removeComponent(player, Components.Player.MULLIGAN);
        data.removeComponent(player, Components.Player.ACTIVE_PLAYER);
        int nextPlayer = data.getComponent(player, Components.NEXT_PLAYER);
        if (data.hasComponent(nextPlayer, Components.Player.MULLIGAN)) {
            LOG.debug("Setting next player {} ready for mulligan", inspect(data, nextPlayer));
            data.setComponent(nextPlayer, Components.Player.ACTIVE_PLAYER);
        } else {
            LOG.debug("Mulligan phase finished");
            context.getEvents().fire(new StartTurnEvent(nextPlayer));
        }
    }
}
