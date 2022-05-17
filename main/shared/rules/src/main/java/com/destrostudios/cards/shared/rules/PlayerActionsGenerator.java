package com.destrostudios.cards.shared.rules;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.battle.AttackEvent;
import com.destrostudios.cards.shared.rules.cards.PlaySpellEvent;
import com.destrostudios.cards.shared.rules.game.turn.EndTurnEvent;
import com.destrostudios.cards.shared.rules.util.SpellUtil;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.IntPredicate;

public class PlayerActionsGenerator {

    private final EntityData data;

    public PlayerActionsGenerator(EntityData data) {
        this.data = data;
    }

    public List<Event> generatePossibleActions(int player) {
        List<Event> possibleEvents = new LinkedList<>();
        if (data.hasComponent(player, Components.Game.ACTIVE_PLAYER)) {
            generatePlaySpells(player, possibleEvents::add);
            generateAttacks(player, possibleEvents::add);
            possibleEvents.add(new EndTurnEvent(player));
        }
        return possibleEvents;
    }

    private void generatePlaySpells(int player, Consumer<Event> out) {
        List<Integer> ownedCardEntities = data.query(Components.OWNED_BY).list(ownedBy(player));
        for (int card : ownedCardEntities) {
            int[] spells = data.getComponent(card, Components.SPELL_ENTITIES);
            if (spells != null) {
                for (int spell : spells) {
                    generatePlaySpellEvents(card, spell, out);
                }
            }
        }
    }

    private void generatePlaySpellEvents(int card, int spell, Consumer<Event> out) {
        if (SpellUtil.isTargeted(data, spell)) {
            for (int target : data.query(Components.OWNED_BY).list()) {
                int[] targets = new int[] { target };
                if (SpellUtil.isCastable(data, card, spell, targets)) {
                    out.accept(new PlaySpellEvent(spell, targets));
                }
            }
        } else {
            int[] targets = new int[0];
            if (SpellUtil.isCastable(data, card, spell, targets)) {
                out.accept(new PlaySpellEvent(spell, targets));
            }
        }
    }

    private void generateAttacks(int player, Consumer<Event> out) {
        for (int targetPlayer : data.query(Components.NEXT_PLAYER).list(x -> x != player)) {
            for (int attacker : data.query(Components.CREATURE_ZONE).list(ownedBy(player).and(x -> !data.hasComponent(x, Components.HAS_ATTACKED)))) {
                List<Integer> targetCreatures = data.query(Components.CREATURE_ZONE).list(ownedBy(targetPlayer));
                if (targetCreatures.size() > 0) {
                    for (int targetCreature : targetCreatures) {
                        out.accept(new AttackEvent(attacker, targetCreature));
                    }
                } else {
                    out.accept(new AttackEvent(attacker, targetPlayer));
                }
            }
        }
    }

    private IntPredicate ownedBy(int player) {
        return x -> data.hasComponentValue(x, Components.OWNED_BY, player);
    }
}
