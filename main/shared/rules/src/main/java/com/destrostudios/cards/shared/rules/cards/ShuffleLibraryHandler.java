package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import java.util.List;

/**
 * @author Philipp
 */
public class ShuffleLibraryHandler extends GameEventHandler<ShuffleLibraryEvent> {

    @Override
    public void handle(ShuffleLibraryEvent event) {
        List<Integer> libraryCards = data.query(Components.LIBRARY).list((cardEntity) -> data.getComponent(cardEntity, Components.OWNED_BY) == event.player);
        
        for (int i = libraryCards.size(); i > 0; i--) {
            int cardEntity = libraryCards.get(random.applyAsInt(i));
            data.setComponent(cardEntity, Components.LIBRARY, i - 1);
        }
    }
}
