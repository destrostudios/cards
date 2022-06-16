package com.destrostudios.cards.shared.rules;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.cards.PlaySpellEvent;
import com.destrostudios.cards.shared.rules.game.turn.EndTurnEvent;
import com.destrostudios.cards.shared.rules.util.SpellUtil;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.IntPredicate;
import java.util.stream.Collectors;

public class PlayerActionsGenerator {

    public List<Event> generatePossibleActions(EntityData data, int player) {
        List<Event> possibleEvents = new LinkedList<>();
        if (data.hasComponent(player, Components.Game.ACTIVE_PLAYER)) {
            List<Integer> allTargets = data.query(Components.TARGETABLE).list();
            generatePlaySpells(data, player, allTargets, possibleEvents::add);
            possibleEvents.add(new EndTurnEvent(player));
        }
        return possibleEvents;
    }

    private void generatePlaySpells(EntityData data, int player, List<Integer> allTargets, Consumer<Event> out) {
        List<Integer> ownedCardEntities = data.query(Components.OWNED_BY).list(ownedBy(data, player));
        for (int card : ownedCardEntities) {
            int[] spells = data.getComponent(card, Components.SPELLS);
            if (spells != null) {
                for (int spell : spells) {
                    generatePlaySpellEvents(data, card, spell, allTargets, out);
                }
            }
        }
    }

    private void generatePlaySpellEvents(EntityData data, int card, int spell, List<Integer> allTargets, Consumer<Event> out) {
        int[] noTargets = new int[0];
        boolean isCastableIgnoringTargets = SpellUtil.isCastable(data, card, spell, noTargets);
        if (isCastableIgnoringTargets) {
            boolean targeted = SpellUtil.isTargeted(data, spell);
            List<Integer> validTargets = new LinkedList<>();
            if (targeted) {
                for (int target : allTargets) {
                    int[] targets = new int[] { target };
                    if (SpellUtil.isCastable(data, card, spell, targets)) {
                        validTargets.add(target);
                    }
                }
                if (data.hasComponent(spell, Components.Spell.TAUNTABLE) && validTargets.stream().anyMatch(target -> data.hasComponent(target, Components.Ability.TAUNT))) {
                    validTargets = validTargets.stream()
                            .filter(target -> data.hasComponent(target, Components.Ability.TAUNT))
                            .collect(Collectors.toList());
                }
            }
            if (validTargets.size() > 0) {
                for (int target : validTargets) {
                    int[] targets = new int[] { target };
                    out.accept(new PlaySpellEvent(spell, targets));
                }
            } else if ((!targeted) || data.hasComponent(spell, Components.Spell.TARGET_OPTIONAL)) {
                out.accept(new PlaySpellEvent(spell, noTargets));
            }
        }
    }

    private IntPredicate ownedBy(EntityData data, int player) {
        return x -> data.hasComponentValue(x, Components.OWNED_BY, player);
    }
}
