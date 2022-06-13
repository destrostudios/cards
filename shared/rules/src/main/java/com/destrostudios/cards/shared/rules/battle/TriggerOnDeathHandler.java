package com.destrostudios.cards.shared.rules.battle;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.effects.TriggerEffectTriggerIfPossibleEvent;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TriggerOnDeathHandler extends GameEventHandler<DestructionEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(TriggerOnDeathHandler.class);

    @Override
    public void handle(DestructionEvent event, NetworkRandom random) {
        LOG.info("Checking death triggers of {}", event.target);
        int[] deathEffectTriggers = data.getComponent(event.target, Components.DEATH_EFFECT_TRIGGERS);
        if (deathEffectTriggers != null) {
            for (int effectTrigger : deathEffectTriggers) {
                events.fire(new TriggerEffectTriggerIfPossibleEvent(event.target, new int[] { event.target }, effectTrigger), random);
            }
        }
    }
}
