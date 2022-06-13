package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.effects.TriggerEffectTriggerIfPossibleEvent;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.IntStream;

public class PlaySpellHandler extends GameEventHandler<PlaySpellEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(PlaySpellHandler.class);

    @Override
    public void handle(PlaySpellEvent event, NetworkRandom random) {
        LOG.info("Casting spell {}", event.spell);

        int card = data.query(Components.SPELLS)
                .unique(currentCardEntity -> IntStream.of(data.getComponent(currentCardEntity, Components.SPELLS))
                .anyMatch(entity -> entity == event.spell)).getAsInt();

        Integer cost = data.getComponent(event.spell, Components.COST);
        if (cost != null) {
            events.fire(new PayCostEvent(card, cost), random);
        }

        int[] instantEffectTriggers = data.getComponent(event.spell, Components.Spell.INSTANT_EFFECT_TRIGGERS);
        if (instantEffectTriggers != null) {
            for (int effectTrigger : instantEffectTriggers) {
                events.fire(new TriggerEffectTriggerIfPossibleEvent(card, event.targets, effectTrigger), random);
            }
        }
    }
}
