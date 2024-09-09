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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class PlayerActionsGenerator {

    public static List<Event> generatePossibleActions(EntityData data, int player) {
        List<Event> possibleEvents = new ArrayList<>(64);
        if (data.hasComponent(player, Components.Player.ACTIVE_PLAYER)) {
            if (data.hasComponent(player, Components.Player.MULLIGAN)) {
                generateMulligans(data, player, possibleEvents::add);
            } else {
                generateSpellCasts(data, player, possibleEvents::add);
                possibleEvents.add(new EndTurnEvent(player));
            }
        }
        return possibleEvents;
    }

    private static void generateMulligans(EntityData data, int player, Consumer<Event> out) {
        IntList handCards = data.list(Components.Zone.PLAYER_HAND[player]);
        List<int[]> handCardsSubsets = ArrayUtil.getAllSubsets(handCards);
        for (int[] handCardsSubset : handCardsSubsets) {
            out.accept(new MulliganEvent(handCardsSubset));
        }
    }

    private static void generateSpellCasts(EntityData data, int player, Consumer<Event> out) {
        // Currently, only cards in hand and creature zone have castable spells (so only checking those speeds up the process a lot)
        generateSpellCasts(data, player, data.list(Components.Zone.PLAYER_HAND[player]), out);
        generateSpellCasts(data, player, data.list(Components.Zone.PLAYER_CREATURE_ZONE[player]), out);
    }

    private static void generateSpellCasts(EntityData data, int player, IntList ownedCards, Consumer<Event> out) {
        for (int card : ownedCards) {
            int[] spells = data.getComponent(card, Components.SPELLS);
            for (int spell : spells) {
                generateCastSpellEvents(data, player, card, spell, out);
            }
        }
    }

    private static void generateCastSpellEvents(EntityData data, int player, int card, int spell, Consumer<Event> out) {
        if (!SpellUtil.isCastable_WithoutSpellCondition(data, player, spell)) {
            return;
        }
        if (SpellUtil.isTargeted(data, spell)) {
            Components.Prefilters targetPrefilters = data.getComponent(spell, Components.Target.TARGET_PREFILTERS);
            IntList validTargets = TargetUtil.getPrefilteredEntities(data, card, targetPrefilters);
            validTargets.retain(target -> SpellUtil.isCastable_OnlySpellCondition(data, card, spell, new int[] { target }));
            if (data.hasComponent(spell, Components.Spell.TAUNTABLE) && validTargets.anyMatch(target -> data.hasComponent(target, Components.Ability.TAUNT))) {
                validTargets.retain(target -> data.hasComponent(target, Components.Ability.TAUNT));
            }
            if (validTargets.nonEmpty()) {
                SpellUtil.ValidTargetsAmount validTargetAmounts = SpellUtil.getValidTargetsAmount(data, spell);
                for (int amount = validTargetAmounts.minimum(); amount <= validTargetAmounts.maximum(); amount++) {
                    List<IntList> targetsSubsets = ArrayUtil.getSubsets(validTargets, amount);
                    for (IntList targets : targetsSubsets) {
                        out.accept(new CastSpellEvent(card, spell, targets.toArray()));
                    }
                }
                return;
            }
            boolean isTargetOptional = data.hasComponent(spell, Components.Spell.TARGET_OPTIONAL);
            if (!isTargetOptional) {
                return;
            }
        }
        if (SpellUtil.isCastable_OnlySpellCondition(data, card, spell, ArrayUtil.EMPTY)) {
            out.accept(new CastSpellEvent(card, spell, ArrayUtil.EMPTY));
        }
    }
}
