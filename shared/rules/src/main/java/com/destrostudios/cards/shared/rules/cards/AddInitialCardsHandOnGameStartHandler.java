package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.entities.IntList;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameConstants;
import com.destrostudios.cards.shared.rules.GameContext;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.cards.zones.MoveToHandEvent;
import com.destrostudios.cards.shared.rules.game.GameStartEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddInitialCardsHandOnGameStartHandler extends GameEventHandler<GameStartEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(AddInitialCardsHandOnGameStartHandler.class);

    @Override
    public void handle(GameContext context, GameStartEvent event) {
        EntityData data = context.getData();
        for (int player : data.list(Components.NEXT_PLAYER)) {
            int initialHandSize = GameConstants.INITIAL_HAND_SIZE + (data.hasComponent(player, Components.Player.ACTIVE_PLAYER) ? 0 : 1);
            LOG.debug("Adding initial {} cards to hand of player {}", initialHandSize, inspect(data, player));
            IntList libraryCards = data.getComponent(player, Components.Player.LIBRARY_CARDS);
            int lastDrawnLibraryCardIndex = Math.max(0, libraryCards.size() - initialHandSize);
            for (int i = libraryCards.size() - 1; i >= lastDrawnLibraryCardIndex; i--) {
                int card = libraryCards.get(i);
                context.getEvents().fire(new MoveToHandEvent(card));
            }
        }
    }
}
