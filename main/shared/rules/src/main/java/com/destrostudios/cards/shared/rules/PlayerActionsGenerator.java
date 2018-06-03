package com.destrostudios.cards.shared.rules;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.battle.BattleEvent;
import com.destrostudios.cards.shared.rules.cards.PlaySpellEvent;
import com.destrostudios.cards.shared.rules.game.phases.TurnPhase;
import com.destrostudios.cards.shared.rules.game.phases.attack.EndAttackPhaseEvent;
import com.destrostudios.cards.shared.rules.game.phases.block.EndBlockPhaseEvent;
import com.destrostudios.cards.shared.rules.game.phases.main.EndMainPhaseOneEvent;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.IntPredicate;

public class PlayerActionsGenerator {

    private final EntityData data;

    public PlayerActionsGenerator(EntityData data) {
        this.data = data;
    }

    public List<Event> generatePossibleActions(int playerEntity) {
        List<Event> possibleEvents = new LinkedList<>();
        generatePlaySpells(playerEntity, possibleEvents::add);
        generateAttacks(playerEntity, possibleEvents::add);
        generateBlockers(playerEntity, possibleEvents::add);
        generateEndPhase(playerEntity, possibleEvents::add);
        return possibleEvents;
    }


    private void generatePlaySpells(int player, Consumer<Event> out) {
        TurnPhase phase = data.getComponent(player, Components.Game.TURN_PHASE);

        if (phase != TurnPhase.MAIN_ONE && phase != TurnPhase.MAIN_TWO) {
            return;
        }

        List<Integer> ownedCardEntities = data.query(Components.OWNED_BY).list(ownedBy(player));
        for (int cardEntity : ownedCardEntities) {
            boolean isHandCard = data.hasComponent(cardEntity, Components.HAND_CARDS);
            boolean isBoardCard = data.hasComponent(cardEntity, Components.BOARD);

            int[] spellEntities = data.getOptionalComponent(cardEntity, Components.SPELL_ENTITIES).orElseGet(() -> new int[0]);
            for (int spellEntity : spellEntities) {
                if (isSpellCastable(spellEntity, phase, isHandCard, isBoardCard)) {
                    out.accept(new PlaySpellEvent(spellEntity));
                }
            }
        }
    }

    private boolean isSpellCastable(int spell, TurnPhase phase, boolean isHandCard, boolean isBoardCard) {
        switch (phase) {
            case ATTACK:
                if (!data.hasComponent(spell, Components.Spell.CastCondition.ATTACK_PHASE)) {
                    return false;
                }
                break;
            case BLOCK:
                if (!data.hasComponent(spell, Components.Spell.CastCondition.BLOCK_PHASE)) {
                    return false;
                }
                break;
            case MAIN_ONE:
            case MAIN_TWO:
                if (!data.hasComponent(spell, Components.Spell.CastCondition.MAIN_PHASE)) {
                    return false;
                }
                break;
            default:
                throw new AssertionError(phase.name());
        }

        return (isHandCard && data.hasComponent(spell, Components.Spell.CastCondition.FROM_HAND))
                || (isBoardCard && data.hasComponent(spell, Components.Spell.CastCondition.FROM_BOARD));
    }

    private void generateAttacks(int player, Consumer<Event> out) {
        if (data.hasComponentValue(player, Components.Game.TURN_PHASE, TurnPhase.ATTACK)) {
            for (int attacker : data.query(Components.CREATURE_ZONE).list(ownedBy(player))) {
                for (int defender : data.query(Components.CREATURE_ZONE).list(ownedBy(player).negate())) {
                    out.accept(new BattleEvent(attacker, defender));
                }
            }
        }
    }

    private void generateBlockers(int playerEntity, Consumer<Event> out) {
        // TODO: 03.06.2018 to be implemented
        if (data.hasComponentValue(playerEntity, Components.Game.TURN_PHASE, TurnPhase.BLOCK)) {

        }
    }

    private void generateEndPhase(int player, Consumer<Event> out) {
        data.getOptionalComponent(player, Components.Game.TURN_PHASE).ifPresent(phase -> {
            switch (phase) {
                case ATTACK:
                    out.accept(new EndAttackPhaseEvent(player));
                    break;
                case BLOCK:
                    out.accept(new EndBlockPhaseEvent(player));
                    break;
                case MAIN_ONE:
                    out.accept(new EndMainPhaseOneEvent(player));
                    break;
                case MAIN_TWO:

                    break;
                default:
                    throw new AssertionError(phase.name());
            }
        });
    }


    private IntPredicate ownedBy(int player) {
        return x -> data.hasComponentValue(x, Components.OWNED_BY, player);
    }
}
