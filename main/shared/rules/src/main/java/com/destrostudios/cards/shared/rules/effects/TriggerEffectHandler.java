package com.destrostudios.cards.shared.rules.effects;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.battle.DamageEvent;
import com.destrostudios.cards.shared.rules.cards.TapEvent;
import com.destrostudios.cards.shared.rules.cards.UntapEvent;
import com.destrostudios.cards.shared.rules.cards.zones.AddCardToBoardEvent;
import com.destrostudios.cards.shared.rules.cards.zones.AddCardToGraveyardEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TriggerEffectHandler extends GameEventHandler<TriggerEffectEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(TriggerEffectHandler.class);

    @Override
    public void handle(TriggerEffectEvent event) {
        LOG.info("Triggering effect (source={}, target={}, effect={})", event.source, event.target, event.effect);

        // TODO: Create own subevents/handlers/however-we-want-it for everything

        if (data.hasComponent(event.effect, Components.Spell.Effect.Zones.ADD_TO_BOARD)) {
            events.fire(new AddCardToBoardEvent(event.target));
        }

        if (data.hasComponent(event.effect, Components.Spell.Effect.Zones.ADD_TO_GRAVEYARD)) {
            events.fire(new AddCardToGraveyardEvent(event.target));
        }

        Integer damage = data.getComponent(event.effect, Components.Spell.Effect.DAMAGE);
        if (damage != null) {
            events.fire(new DamageEvent(event.target, damage));
        }

        Integer gainManaEntity = data.getComponent(event.effect, Components.Spell.Effect.GAIN_MANA);
        if (gainManaEntity != null) {
            int player = data.getComponent(event.source, Components.OWNED_BY);
            events.fire(new AddManaEvent(player, gainManaEntity));
        }

        if (data.hasComponent(event.effect, Components.Spell.Effect.TAP)) {
            events.fire(new TapEvent(event.target));
        }

        if (data.hasComponent(event.effect, Components.Spell.Effect.UNTAP)) {
            events.fire(new UntapEvent(event.target));
        }
    }
}
