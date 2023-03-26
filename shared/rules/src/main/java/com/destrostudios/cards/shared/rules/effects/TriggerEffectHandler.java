package com.destrostudios.cards.shared.rules.effects;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.battle.BattleEvent;
import com.destrostudios.cards.shared.rules.battle.DamageEvent;
import com.destrostudios.cards.shared.rules.battle.DestructionEvent;
import com.destrostudios.cards.shared.rules.battle.HealEvent;
import com.destrostudios.cards.shared.rules.buffs.AddBuffEvent;
import com.destrostudios.cards.shared.rules.cards.DrawCardEvent;
import com.destrostudios.cards.shared.rules.cards.zones.AddCardToBoardEvent;
import com.destrostudios.cards.shared.rules.cards.zones.AddCardToGraveyardEvent;
import com.destrostudios.cards.shared.rules.cards.zones.AddCardToHandEvent;
import com.destrostudios.cards.shared.rules.expressions.Expressions;
import com.destrostudios.cards.shared.rules.util.BuffUtil;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TriggerEffectHandler extends GameEventHandler<TriggerEffectEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(TriggerEffectHandler.class);

    @Override
    public void handle(TriggerEffectEvent event, NetworkRandom random) {
        LOG.info("Triggering effect (source={}, target={}, effect={})", event.source, event.target, event.effect);

        // TODO: Create own subevents/handlers/however-we-want-it for everything

        if (data.hasComponent(event.effect, Components.Effect.Zones.ADD_TO_BOARD)) {
            events.fire(new AddCardToBoardEvent(event.target), random);
        }

        if (data.hasComponent(event.effect, Components.Effect.Zones.ADD_TO_GRAVEYARD)) {
            events.fire(new AddCardToGraveyardEvent(event.target), random);
        }

        if (data.hasComponent(event.effect, Components.Effect.Zones.ADD_TO_HAND)) {
            events.fire(new AddCardToHandEvent(event.target), random);
        }

        String damageExpression = data.getComponent(event.effect, Components.Effect.DAMAGE);
        if (damageExpression != null) {
            int damage = Expressions.evaluate(data, damageExpression, event.source, event.target);
            events.fire(new DamageEvent(event.target, damage), random);
        }

        String healExpression = data.getComponent(event.effect, Components.Effect.HEAL);
        if (healExpression != null) {
            int heal = Expressions.evaluate(data, healExpression, event.source, event.target);
            events.fire(new HealEvent(event.target, heal), random);
        }

        if (data.hasComponent(event.effect, Components.Effect.BATTLE)) {
            events.fire(new BattleEvent(event.source, event.target), random);
        }

        String drawExpression = data.getComponent(event.effect, Components.Effect.DRAW);
        if (drawExpression != null) {
            int drawnCards = Expressions.evaluate(data, drawExpression, event.source, event.target);
            for (int i = 0; i < drawnCards; i++) {
                events.fire(new DrawCardEvent(event.target), random);
            }
        }

        String gainManaExpression = data.getComponent(event.effect, Components.Effect.GAIN_MANA);
        if (gainManaExpression != null) {
            int gainedMana = Expressions.evaluate(data, gainManaExpression, event.source, event.target);
            events.fire(new AddManaEvent(event.target, gainedMana), random);
        }

        if (data.hasComponent(event.effect, Components.Effect.DESTROY)) {
            events.fire(new DestructionEvent(event.target), random);
        }

        Components.AddBuff addBuff = data.getComponent(event.effect, Components.Effect.ADD_BUFF);
        if (addBuff != null) {
            int buff = (addBuff.isEvalutated() ? BuffUtil.createEvaluatedBuffCopy(data, addBuff.getBuff(), event.source, event.target) : addBuff.getBuff());
            events.fire(new AddBuffEvent(event.target, buff), random);
        }

        String[] templates = data.getComponent(event.effect, Components.Effect.SUMMON);
        if (templates != null) {
            for (String template : templates) {
                events.fire(new SummonEvent(event.target, template), random);
            }
        }
    }
}
