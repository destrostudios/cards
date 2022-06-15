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

    private final EntityData data;

    public PlayerActionsGenerator(EntityData data) {
        this.data = data;
    }

    public List<Event> generatePossibleActions(int player) {
        List<Event> possibleEvents = new LinkedList<>();
        if (data.hasComponent(player, Components.Game.ACTIVE_PLAYER)) {
            List<Integer> allTargets = data.query(Components.TARGETABLE).list();
            generatePlaySpells(player, allTargets, possibleEvents::add);
            possibleEvents.add(new EndTurnEvent(player));
        }
        return possibleEvents;
    }

    private void generatePlaySpells(int player, List<Integer> allTargets, Consumer<Event> out) {
        List<Integer> ownedCardEntities = data.query(Components.OWNED_BY).list(ownedBy(player));
        for (int card : ownedCardEntities) {
            int[] spells = data.getComponent(card, Components.SPELLS);
            if (spells != null) {
                for (int spell : spells) {
                    generatePlaySpellEvents(card, spell, allTargets, out);
                }
            }
        }
    }

    private void generatePlaySpellEvents(int card, int spell, List<Integer> allTargets, Consumer<Event> out) {
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
            int[] targets = new int[0];
            if (SpellUtil.isCastable(data, card, spell, targets)) {
                out.accept(new PlaySpellEvent(spell, targets));
            }
        }
    }

    private IntPredicate ownedBy(int player) {
        return x -> data.hasComponentValue(x, Components.OWNED_BY, player);
    }
}
