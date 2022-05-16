package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.effects.CheckEffectTriggerEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.IntStream;

public class PlaySpellHandler extends GameEventHandler<PlaySpellEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(PlaySpellHandler.class);

    @Override
    public void handle(PlaySpellEvent event) {
        LOG.info("Casting spell {}", event.spell);

        int card = data.query(Components.SPELL_ENTITIES)
                .unique(currentCardEntity -> IntStream.of(data.getComponent(currentCardEntity, Components.SPELL_ENTITIES))
                .anyMatch(entity -> entity == event.spell)).getAsInt();

        Integer cost = data.getComponent(event.spell, Components.COST);
        if (cost != null) {
            events.fire(new PayCostEvent(card, cost));
        }

        int[] instantEffectTriggers = data.getComponent(event.spell, Components.Spell.INSTANT_EFFECT_TRIGGERS);
        if (instantEffectTriggers != null) {
            for (int effectTrigger : instantEffectTriggers) {
                events.fire(new CheckEffectTriggerEvent(card, event.targets, effectTrigger));
            }
        }
    }
}
