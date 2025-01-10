package com.destrostudios.cards.shared.rules;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.entities.IntList;
import com.destrostudios.cards.shared.rules.actions.Action;
import com.destrostudios.cards.shared.rules.actions.CastSpellAction;
import com.destrostudios.cards.shared.rules.actions.EndTurnAction;
import com.destrostudios.cards.shared.rules.actions.MulliganAction;
import com.destrostudios.cards.shared.rules.effects.DiscoverPool;
import com.destrostudios.cards.shared.rules.effects.EffectOptions;
import com.destrostudios.cards.shared.rules.util.ArrayUtil;
import com.destrostudios.cards.shared.rules.util.SpellUtil;
import com.destrostudios.cards.shared.rules.util.TargetUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class PlayerActionsGenerator {

    public static List<Action> generatePossibleActions(EntityData data, int player) {
        List<Action> possibleActions = new ArrayList<>(64);
        if (data.hasComponent(player, Components.Player.ACTIVE_PLAYER)) {
            if (data.hasComponent(player, Components.Player.MULLIGAN)) {
                generateMulligans(data, player, possibleActions::add);
            } else {
                generateSpellCasts(data, player, possibleActions::add);
                possibleActions.add(new EndTurnAction(player));
            }
        }
        return possibleActions;
    }

    private static void generateMulligans(EntityData data, int player, Consumer<Action> out) {
        IntList handCards = data.list(Components.Zone.PLAYER_HAND[player]);
        List<int[]> handCardsSubsets = ArrayUtil.getAllSubsets(handCards);
        for (int[] handCardsSubset : handCardsSubsets) {
            out.accept(new MulliganAction(handCardsSubset));
        }
    }

    private static void generateSpellCasts(EntityData data, int player, Consumer<Action> out) {
        // Currently, only cards in hand and creature zone have castable spells (so only checking those speeds up the process a lot)
        generateSpellCasts(data, player, data.list(Components.Zone.PLAYER_HAND[player]), out);
        generateSpellCasts(data, player, data.list(Components.Zone.PLAYER_CREATURE_ZONE[player]), out);
    }

    private static void generateSpellCasts(EntityData data, int player, IntList ownedCards, Consumer<Action> out) {
        for (int card : ownedCards) {
            int[] spells = data.getComponent(card, Components.SPELLS);
            for (int spell : spells) {
                generateCastSpellActions(data, player, card, spell, out);
            }
        }
    }

    private static void generateCastSpellActions(EntityData data, int player, int card, int spell, Consumer<Action> out) {
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
                        out.accept(new CastSpellAction(card, spell, targets.toArray(), null));
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
            DiscoverPool discoverPool = SpellUtil.getDiscoverEffectPool(data, spell);
            if (discoverPool != null) {
                for (int i = 0; i < GameConstants.DISCOVER_OPTIONS; i++) {
                    out.accept(new CastSpellAction(card, spell, ArrayUtil.EMPTY, EffectOptions.builder().discoverIndex(i).build()));
                }
            } else {
                out.accept(new CastSpellAction(card, spell, ArrayUtil.EMPTY, null));
            }
        }
    }
}
