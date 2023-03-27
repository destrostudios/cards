package com.destrostudios.cards.shared.rules;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.cards.PlaySpellEvent;
import com.destrostudios.cards.shared.rules.game.turn.EndTurnEvent;
import com.destrostudios.cards.shared.rules.util.SpellUtil;
import com.destrostudios.cards.shared.rules.util.TargetUtil;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.IntPredicate;
import java.util.stream.Collectors;

public class PlayerActionsGenerator {

    private static final int[] NO_TARGETS = new int[0];

    public List<Event> generatePossibleActions(EntityData data, int player) {
        List<Event> possibleEvents = new LinkedList<>();
        if (data.hasComponent(player, Components.Game.ACTIVE_PLAYER)) {
            generatePlaySpells(data, player, possibleEvents::add);
            possibleEvents.add(new EndTurnEvent(player));
        }
        return possibleEvents;
    }

    private void generatePlaySpells(EntityData data, int player, Consumer<Event> out) {
        List<Integer> ownedCardEntities = new LinkedList<>();
        // Currently, only cards in hand and on board have castable spells (so only checking those speeds up the process a lot)
        ownedCardEntities.addAll(data.query(Components.HAND).list(ownedBy(data, player)));
        ownedCardEntities.addAll(data.query(Components.BOARD).list(ownedBy(data, player)));
        for (int card : ownedCardEntities) {
            int[] spells = data.getComponent(card, Components.SPELLS);
            if (spells != null) {
                for (int spell : spells) {
                    generatePlaySpellEvents(data, card, spell, out);
                }
            }
        }
    }

    private void generatePlaySpellEvents(EntityData data, int card, int spell, Consumer<Event> out) {
        if (!SpellUtil.isCastable_WithoutSpellCondition(data, card, spell)) {
            return;
        }
        if (SpellUtil.isTargeted(data, spell)) {
            TargetPrefilter targetPrefilter = data.getComponent(spell, Components.Target.TARGET_PREFILTER);
            List<Integer> prefilteredTargets = TargetUtil.getPrefilteredTargets(data, targetPrefilter);
            List<Integer> validTargets = prefilteredTargets.stream()
                    .filter(target -> SpellUtil.isCastable_OnlySpellCondition(data, card, spell, new int[] { target }))
                    .collect(Collectors.toList());
            if (data.hasComponent(spell, Components.Spell.TAUNTABLE) && validTargets.stream().anyMatch(target -> data.hasComponent(target, Components.Ability.TAUNT))) {
                validTargets = validTargets.stream()
                        .filter(target -> data.hasComponent(target, Components.Ability.TAUNT))
                        .collect(Collectors.toList());
            }
            if (validTargets.size() > 0) {
                for (int target : validTargets) {
                    out.accept(new PlaySpellEvent(spell, new int[] { target }));
                }
                return;
            }
            boolean isTargetOptional = data.hasComponent(spell, Components.Spell.TARGET_OPTIONAL);
            if (!isTargetOptional) {
                return;
            }
        }
        if (SpellUtil.isCastable_OnlySpellCondition(data, card, spell, NO_TARGETS)) {
            out.accept(new PlaySpellEvent(spell, NO_TARGETS));
        }
    }

    private IntPredicate ownedBy(EntityData data, int player) {
        return x -> data.hasComponentValue(x, Components.OWNED_BY, player);
    }
}
