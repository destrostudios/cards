package com.destrostudios.cards.shared.rules;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.entities.IntList;
import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.cards.CastSpellEvent;
import com.destrostudios.cards.shared.rules.cards.MulliganEvent;
import com.destrostudios.cards.shared.rules.game.turn.EndTurnEvent;
import com.destrostudios.cards.shared.rules.util.ArrayUtil;
import com.destrostudios.cards.shared.rules.util.SpellUtil;
import com.destrostudios.cards.shared.rules.util.TargetUtil;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.IntPredicate;

public class PlayerActionsGenerator {

    private static final int[] NO_TARGETS = new int[0];

    public List<Event> generatePossibleActions(EntityData data, int player) {
        List<Event> possibleEvents = new LinkedList<>();
        if (data.hasComponent(player, Components.Game.ACTIVE_PLAYER)) {
            if (data.hasComponent(player, Components.Game.MULLIGAN)) {
                generateMulligans(data, player, possibleEvents::add);
            } else {
                generateSpellCasts(data, player, possibleEvents::add);
                possibleEvents.add(new EndTurnEvent(player));
            }
        }
        return possibleEvents;
    }

    private void generateMulligans(EntityData data, int player, Consumer<Event> out) {
        IntList handCards = data.query(Components.HAND).list(ownedBy(data, player));
        List<int[]> handCardsSubsets = ArrayUtil.getAllSubsets(handCards);
        for (int[] handCardsSubset : handCardsSubsets) {
            out.accept(new MulliganEvent(handCardsSubset));
        }
    }

    private void generateSpellCasts(EntityData data, int player, Consumer<Event> out) {
        IntList ownedCardEntities = new IntList();
        // Currently, only cards in hand and creature zone have castable spells (so only checking those speeds up the process a lot)
        ownedCardEntities.addAll(data.query(Components.HAND).list(ownedBy(data, player)));
        ownedCardEntities.addAll(data.query(Components.CREATURE_ZONE).list(ownedBy(data, player)));
        for (int card : ownedCardEntities) {
            int[] spells = data.getComponent(card, Components.SPELLS);
            if (spells != null) {
                for (int spell : spells) {
                    generateCastSpellEvents(data, card, spell, out);
                }
            }
        }
    }

    private void generateCastSpellEvents(EntityData data, int card, int spell, Consumer<Event> out) {
        if (!SpellUtil.isCastable_WithoutSpellCondition(data, card, spell)) {
            return;
        }
        if (SpellUtil.isTargeted(data, spell)) {
            Prefilter[] targetPrefilters = data.getComponent(spell, Components.Target.TARGET_PREFILTERS);
            IntList validTargets = TargetUtil.getPrefilteredEntities(data, card, targetPrefilters);
            validTargets.retain(target -> SpellUtil.isCastable_OnlySpellCondition(data, card, spell, new int[] { target }));
            if (data.hasComponent(spell, Components.Spell.TAUNTABLE) && validTargets.anyMatch(target -> data.hasComponent(target, Components.Ability.TAUNT))) {
                validTargets.retain(target -> data.hasComponent(target, Components.Ability.TAUNT));
            }
            if (validTargets.size() > 0) {
                for (int target : validTargets) {
                    out.accept(new CastSpellEvent(card, spell, new int[] { target }));
                }
                return;
            }
            boolean isTargetOptional = data.hasComponent(spell, Components.Spell.TARGET_OPTIONAL);
            if (!isTargetOptional) {
                return;
            }
        }
        if (SpellUtil.isCastable_OnlySpellCondition(data, card, spell, NO_TARGETS)) {
            out.accept(new CastSpellEvent(card, spell, NO_TARGETS));
        }
    }

    private IntPredicate ownedBy(EntityData data, int player) {
        return x -> data.hasComponentValue(x, Components.OWNED_BY, player);
    }
}
