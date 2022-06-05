package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;

import java.util.List;

public class ShuffleLibraryHandler extends GameEventHandler<ShuffleLibraryEvent> {

    @Override
    public void handle(ShuffleLibraryEvent event, NetworkRandom random) {
        List<Integer> libraryCards = data.query(Components.LIBRARY).list(hasComponentValue(Components.OWNED_BY, event.player));
        
        for (int i = libraryCards.size(); i > 0; i--) {
            int cardIndex = random.nextInt(i);
            int cardEntity = libraryCards.remove(cardIndex);
            data.setComponent(cardEntity, Components.LIBRARY, i - 1);
        }
    }
}
