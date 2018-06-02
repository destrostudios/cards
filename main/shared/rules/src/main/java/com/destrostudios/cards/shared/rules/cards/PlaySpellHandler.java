package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;

import java.util.stream.IntStream;

public class PlaySpellHandler extends GameEventHandler<PlaySpellEvent> {

    // TODO: Cleanup and extract all this somehow when we have an actual structure for the components and know what we want to do where
    @Override
    public void handle(PlaySpellEvent event) {
        int card = data.query(Components.SPELL_ENTITIES)
                .unique(currentCardEntity -> IntStream.of(data.getComponent(currentCardEntity, Components.SPELL_ENTITIES))
                .anyMatch(entity -> entity == event.spell)).getAsInt();

        if (data.hasComponent(card, Components.HAND_CARDS)) {
            events.fireSubEvent(new RemoveCardFromHandEvent(card));
        }

        // Cost
        Integer costEntity = data.getComponent(event.spell, Components.Spell.COST_ENTITY);
        if (costEntity != null) {
            if (data.hasComponent(costEntity, Components.Cost.TAP)) {
                data.setComponent(card, Components.TAPPED);
            }
        }

        // Effect
        // TODO: Consider targets
        if (data.hasComponent(event.spell, Components.Spell.Effect.ADD_TO_BOARD)) {
            events.fireSubEvent(new AddCardToBoardEvent(card));
        }
        else if (data.hasComponent(event.spell, Components.SPELL_CARD)) {
            // TODO: Own event etc. etc.
            int playerEntity = data.getComponent(card, Components.OWNED_BY);
            data.setComponent(card, Components.GRAVEYARD, data.query(Components.GRAVEYARD).count(graveyardCard -> data.hasComponentValue(graveyardCard, Components.OWNED_BY, playerEntity)));
        }
    }
}
