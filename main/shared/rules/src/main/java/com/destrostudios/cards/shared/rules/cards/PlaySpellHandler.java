package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.cards.zones.AddCardToBoardEvent;
import com.destrostudios.cards.shared.rules.cards.zones.AddCardToGraveyardEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.IntStream;

public class PlaySpellHandler extends GameEventHandler<PlaySpellEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(PlaySpellHandler.class);

    // TODO: Cleanup and extract all this somehow when we have an actual structure for the components and know what we want to do where
    @Override
    public void handle(PlaySpellEvent event) {
        LOG.info("Casting spell {}", event.spell);

        int card = data.query(Components.SPELL_ENTITIES)
                .unique(currentCardEntity -> IntStream.of(data.getComponent(currentCardEntity, Components.SPELL_ENTITIES))
                .anyMatch(entity -> entity == event.spell)).getAsInt();

        Integer costEntity = data.getComponent(event.spell, Components.Spell.COST_ENTITY);
        if (costEntity != null) {
            events.fireSubEvent(new PayCostEvent(card, costEntity));
        }

        // Effect
        // TODO: Consider targets
        if (data.hasComponent(event.spell, Components.Spell.Effect.ADD_TO_BOARD)) {
            events.fireSubEvent(new AddCardToBoardEvent(card));
        }
        else if (data.hasComponent(event.spell, Components.SPELL_CARD)) {
            events.fireSubEvent(new AddCardToGraveyardEvent(card));
        }
    }
}
