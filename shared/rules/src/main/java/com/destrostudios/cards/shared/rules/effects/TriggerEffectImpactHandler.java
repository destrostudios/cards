package com.destrostudios.cards.shared.rules.effects;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.events.EventQueue;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameContext;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.battle.BattleEvent;
import com.destrostudios.cards.shared.rules.battle.DamageEvent;
import com.destrostudios.cards.shared.rules.battle.DestructionEvent;
import com.destrostudios.cards.shared.rules.battle.HealEvent;
import com.destrostudios.cards.shared.rules.buffs.AddBuffEvent;
import com.destrostudios.cards.shared.rules.buffs.RemoveBuffEvent;
import com.destrostudios.cards.shared.rules.cards.CastSpellEvent;
import com.destrostudios.cards.shared.rules.cards.DiscardEvent;
import com.destrostudios.cards.shared.rules.cards.DrawCardEvent;
import com.destrostudios.cards.shared.rules.cards.ShuffleLibraryEvent;
import com.destrostudios.cards.shared.rules.cards.zones.MoveToCreatureZoneEvent;
import com.destrostudios.cards.shared.rules.cards.zones.MoveToGraveyardEvent;
import com.destrostudios.cards.shared.rules.cards.zones.MoveToHandEvent;
import com.destrostudios.cards.shared.rules.cards.zones.MoveToLibraryEvent;
import com.destrostudios.cards.shared.rules.expressions.Expressions;
import com.destrostudios.cards.shared.rules.game.turn.EndTurnEvent;
import com.destrostudios.cards.shared.rules.util.BuffUtil;
import com.destrostudios.cards.shared.rules.util.SpellUtil;
import com.destrostudios.cards.shared.rules.util.TriggerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TriggerEffectImpactHandler extends GameEventHandler<TriggerEffectImpactEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(TriggerEffectImpactHandler.class);

    @Override
    public void handle(GameContext context, TriggerEffectImpactEvent event) {
        EntityData data = context.getData();
        EventQueue<GameContext> events = context.getEvents();
        LOG.debug("Triggering effect impact (source = {}, target = {}, effect = {})", inspect(data, event.source), inspect(data, event.target), inspect(data, event.effect));

        if (data.hasComponent(event.effect, Components.Effect.Zones.MOVE_TO_CREATURE_ZONE)) {
            events.fire(new MoveToCreatureZoneEvent(event.target));
        } else if (data.hasComponent(event.effect, Components.Effect.Zones.MOVE_TO_GRAVEYARD)) {
            events.fire(new MoveToGraveyardEvent(event.target));
        } else if (data.hasComponent(event.effect, Components.Effect.Zones.MOVE_TO_HAND)) {
            events.fire(new MoveToHandEvent(event.target));
        } else if (data.hasComponent(event.effect, Components.Effect.Zones.MOVE_TO_LIBRARY)) {
            events.fire(new MoveToLibraryEvent(event.target));
        }

        String damageExpression = data.getComponent(event.effect, Components.Effect.DAMAGE);
        if (damageExpression != null) {
            int damage = evaluate(data, damageExpression, event);
            events.fire(new DamageEvent(event.source, event.target, damage));
        }

        String healExpression = data.getComponent(event.effect, Components.Effect.HEAL);
        if (healExpression != null) {
            int heal = evaluate(data, healExpression, event);
            events.fire(new HealEvent(event.source, event.target, heal));
        }

        String drawExpression = data.getComponent(event.effect, Components.Effect.DRAW);
        if (drawExpression != null) {
            int drawnCards = evaluate(data, drawExpression, event);
            for (int r = 0; r < drawnCards; r++) {
                events.fire(new DrawCardEvent(event.target));
            }
        }

        if (data.hasComponent(event.effect, Components.Effect.DISCARD)) {
            events.fire(new DiscardEvent(event.target));
        }

        String gainManaExpression = data.getComponent(event.effect, Components.Effect.GAIN_MANA);
        if (gainManaExpression != null) {
            int gainedMana = evaluate(data, gainManaExpression, event);
            events.fire(new AddManaEvent(event.target, gainedMana));
        }

        if (data.hasComponent(event.effect, Components.Effect.DESTROY)) {
            events.fire(new DestructionEvent(event.target));
        }

        if (data.hasComponent(event.effect, Components.Effect.CAST_ATTACK)) {
            int sourceDefaultAttackSpell = SpellUtil.getDefaultAttackSpell(data, event.source);
            events.fire(new CastSpellEvent(event.source, sourceDefaultAttackSpell, new int[] { event.target }));
        }

        if (data.hasComponent(event.effect, Components.Effect.BATTLE)) {
            events.fire(new BattleEvent(event.source, event.target));
        }

        Components.AddBuff addBuff = data.getComponent(event.effect, Components.Effect.ADD_BUFF);
        if (addBuff != null) {
            int buff = (addBuff.isConstant() ? BuffUtil.createEvaluatedBuffCopy(data, addBuff.getBuff(), event) : addBuff.getBuff());
            events.fire(new AddBuffEvent(event.target, buff));
        }

        Integer buffToRemove = data.getComponent(event.effect, Components.Effect.REMOVE_BUFF);
        if (buffToRemove != null) {
            events.fire(new RemoveBuffEvent(event.target, buffToRemove));
        }

        Components.Create create = data.getComponent(event.effect, Components.Effect.CREATE);
        if (create != null) {
            events.fire(new CreateEvent(event.source, event.target, create.getTemplate(), create.getLocation()));
        }

        Components.TriggerDelayed triggerDelayed = data.getComponent(event.effect, Components.Effect.TRIGGER_DELAYED);
        if (triggerDelayed != null) {
            TriggerUtil.triggerDelayed(data, event.source, event.target, triggerDelayed);
        }

        boolean shuffleLibrary = data.hasComponent(event.effect, Components.Effect.SHUFFLE_LIBRARY);
        boolean endTurn = data.hasComponent(event.effect, Components.Effect.END_TURN);
        if (shuffleLibrary || endTurn) {
            int activePlayer = data.unique(Components.Player.ACTIVE_PLAYER);

            if (shuffleLibrary) {
                events.fire(new ShuffleLibraryEvent(activePlayer));
            }

            if (endTurn) {
                events.fire(new EndTurnEvent(activePlayer));
            }
        }
    }

    private <T> T evaluate(EntityData data, String expression, TriggerEffectImpactEvent event) {
        return Expressions.evaluate(expression, Expressions.getContext_Provider(data, event));
    }
}
