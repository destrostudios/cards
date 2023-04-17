package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.entities.IntList;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameContext;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShuffleLibraryHandler extends GameEventHandler<ShuffleLibraryEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(ShuffleLibraryHandler.class);

    @Override
    public void handle(GameContext context, ShuffleLibraryEvent event) {
        EntityData data = context.getData();
        LOG.debug("Shuffling library of player {}", inspect(data, event.player));
        IntList newLibraryCards = context.getData().getComponent(event.player, Components.Player.LIBRARY_CARDS).copy();
        newLibraryCards.shuffle(max -> context.getRandom().nextInt(max));
        context.getData().setComponent(event.player, Components.Player.LIBRARY_CARDS, newLibraryCards);
    }
}
