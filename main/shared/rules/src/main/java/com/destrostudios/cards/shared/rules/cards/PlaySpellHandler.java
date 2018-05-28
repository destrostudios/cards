package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;

import java.util.stream.IntStream;

public class PlaySpellHandler extends GameEventHandler<PlaySpellEvent> {

    @Override
    public void handle(PlaySpellEvent event) {
        // TODO: Cleanup all this somehow when we have an actual structure for the components and know what we want to do where
        int card = data.query(Components.SPELL_ENTITIES)
                .unique(currentCardEntity -> IntStream.of(data.getComponent(currentCardEntity, Components.SPELL_ENTITIES))
                .anyMatch(entity -> entity == event.spell)).getAsInt();
        if (data.hasComponent(card, Components.HAND_CARDS)) {
            events.fireSubEvent(new RemoveCardFromHandEvent(card));
        }
        if (data.hasComponent(event.spell, Components.Spell.Effect.ADD_TO_BOARD)) {
            events.fireSubEvent(new AddCardToBoardEvent(card));
        }
        else {
            // TODO: Own event etc. etc.
            int playerEntity = data.getComponent(card, Components.OWNED_BY);
            data.setComponent(card, Components.GRAVEYARD, data.query(Components.GRAVEYARD).count(graveyardCard -> data.hasComponentValue(graveyardCard, Components.OWNED_BY, playerEntity)));
        }
    }
}
