package com.destrostudios.cards.shared.rules.effects;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.battle.BattleEvent;
import com.destrostudios.cards.shared.rules.battle.DamageEvent;
import com.destrostudios.cards.shared.rules.battle.DestructionEvent;
import com.destrostudios.cards.shared.rules.battle.HealEvent;
import com.destrostudios.cards.shared.rules.buffs.AddBuffEvent;
import com.destrostudios.cards.shared.rules.cards.DrawCardEvent;
import com.destrostudios.cards.shared.rules.cards.zones.MoveToCreatureZoneEvent;
import com.destrostudios.cards.shared.rules.cards.zones.MoveToGraveyardEvent;
import com.destrostudios.cards.shared.rules.cards.zones.MoveToHandEvent;
import com.destrostudios.cards.shared.rules.expressions.Expressions;
import com.destrostudios.cards.shared.rules.game.turn.EndTurnEvent;
import com.destrostudios.cards.shared.rules.util.BuffUtil;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TriggerEffectImpactHandler extends GameEventHandler<TriggerEffectImpactEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(TriggerEffectImpactHandler.class);

    @Override
    public void handle(TriggerEffectImpactEvent event, NetworkRandom random) {
        LOG.debug("Triggering effect impact (source = {}, target = {}, effect = {})", inspect(event.source), inspect(event.target), inspect(event.effect));

        if (data.hasComponent(event.effect, Components.Effect.Zones.MOVE_TO_HAND)) {
            events.fire(new MoveToHandEvent(event.target), random);
        }

        if (data.hasComponent(event.effect, Components.Effect.Zones.MOVE_TO_CREATURE_ZONE)) {
            events.fire(new MoveToCreatureZoneEvent(event.target), random);
        }

        if (data.hasComponent(event.effect, Components.Effect.Zones.MOVE_TO_GRAVEYARD)) {
            events.fire(new MoveToGraveyardEvent(event.target), random);
        }

        String damageExpression = data.getComponent(event.effect, Components.Effect.DAMAGE);
        if (damageExpression != null) {
            int damage = evaluate(data, damageExpression, event);
            events.fire(new DamageEvent(event.target, damage), random);
        }

        String healExpression = data.getComponent(event.effect, Components.Effect.HEAL);
        if (healExpression != null) {
            int heal = evaluate(data, healExpression, event);
            events.fire(new HealEvent(event.target, heal), random);
        }

        String drawExpression = data.getComponent(event.effect, Components.Effect.DRAW);
        if (drawExpression != null) {
            int drawnCards = evaluate(data, drawExpression, event);
            for (int r = 0; r < drawnCards; r++) {
                events.fire(new DrawCardEvent(event.target), random);
            }
        }

        String gainManaExpression = data.getComponent(event.effect, Components.Effect.GAIN_MANA);
        if (gainManaExpression != null) {
            int gainedMana = evaluate(data, gainManaExpression, event);
            events.fire(new AddManaEvent(event.target, gainedMana), random);
        }

        if (data.hasComponent(event.effect, Components.Effect.DESTROY)) {
            events.fire(new DestructionEvent(event.target), random);
        }

        if (data.hasComponent(event.effect, Components.Effect.BATTLE)) {
            events.fire(new BattleEvent(event.source, event.target), random);
        }

        Components.AddBuff addBuff = data.getComponent(event.effect, Components.Effect.ADD_BUFF);
        if (addBuff != null) {
            int buff = (addBuff.isConstant() ? BuffUtil.createEvaluatedBuffCopy(data, addBuff.getBuff(), event.source, event.target) : addBuff.getBuff());
            events.fire(new AddBuffEvent(event.target, buff), random);
        }

        Components.Create create = data.getComponent(event.effect, Components.Effect.CREATE);
        if (create != null) {
            events.fire(new CreateEvent(event.source, event.target, create.getTemplate(), create.getLocation()), random);
        }

        if (data.hasComponent(event.effect, Components.Effect.END_TURN)) {
            int activePlayer = data.query(Components.Game.ACTIVE_PLAYER).unique();
            events.fire(new EndTurnEvent(activePlayer), random);
        }
    }

    private <T> T evaluate(EntityData data, String expression, TriggerEffectImpactEvent event) {
        return Expressions.evaluate(expression, Expressions.getContext_Event(data, event));
    }
}
