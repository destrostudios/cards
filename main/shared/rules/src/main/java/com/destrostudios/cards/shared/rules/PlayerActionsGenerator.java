package com.destrostudios.cards.shared.rules;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.battle.DeclareAttackEvent;
import com.destrostudios.cards.shared.rules.battle.DeclareBlockEvent;
import com.destrostudios.cards.shared.rules.cards.PlaySpellEvent;
import com.destrostudios.cards.shared.rules.game.phases.TurnPhase;
import com.destrostudios.cards.shared.rules.game.phases.attack.EndAttackPhaseEvent;
import com.destrostudios.cards.shared.rules.game.phases.block.EndBlockPhaseEvent;
import com.destrostudios.cards.shared.rules.game.phases.main.EndMainPhaseOneEvent;
import com.destrostudios.cards.shared.rules.game.phases.main.EndMainPhaseTwoEvent;
import com.destrostudios.cards.shared.rules.util.MixedManaAmount;

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
        generateBlocks(playerEntity, possibleEvents::add);
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
            int[] spellEntities = data.getOptionalComponent(cardEntity, Components.SPELL_ENTITIES).orElseGet(() -> new int[0]);
            for (int spellEntity : spellEntities) {
                if (isSpellCastable(cardEntity, spellEntity, phase)) {
                    List<PlaySpellEvent> playSpellEvents = generatePlaySpellEvents(player, spellEntity);
                    for (PlaySpellEvent playSpellEvent : playSpellEvents) {
                        out.accept(playSpellEvent);
                    }
                }
            }
        }
    }

    private boolean isSpellCastable(int card, int spell, TurnPhase phase) {
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

        if ((data.hasComponent(spell, Components.Spell.CastCondition.FROM_HAND) && (!data.hasComponent(card, Components.HAND_CARDS))
         || (data.hasComponent(spell, Components.Spell.CastCondition.FROM_BOARD) && (!data.hasComponent(card, Components.BOARD))))) {
            return false;
        }

        Integer costEntity = data.getComponent(spell, Components.Spell.COST_ENTITY);
        if (costEntity != null) {
            if (data.hasComponent(costEntity, Components.Cost.TAP) && data.hasComponent(card, Components.TAPPED)) {
                return false;
            }
        }

        return true;
    }

    private List<PlaySpellEvent> generatePlaySpellEvents(int player, int spellEntity) {
        LinkedList<PlaySpellEvent> playSpellEvents = new LinkedList<>();

        Integer costEntity = data.getComponent(spellEntity, Components.Spell.COST_ENTITY);
        boolean manaPermutationsNeeded = false;
        MixedManaAmount costManaAmount = null;
        if (costEntity != null) {
            costManaAmount = getMixedManaAmount(costEntity);
            if (costManaAmount.isNotEmpty()) {
                manaPermutationsNeeded = true;
            }
        }
        if (manaPermutationsNeeded) {
            MixedManaAmount playerManaAmount = getMixedManaAmount(player);
            List<MixedManaAmount> payableManaPermutations = ManaPermutations.generatePayablePermutations(playerManaAmount, costManaAmount);
            for (MixedManaAmount payableManaPermutation : payableManaPermutations) {
                playSpellEvents.add(new PlaySpellEvent(spellEntity, payableManaPermutation));
            }
        }
        else {
            playSpellEvents.add(new PlaySpellEvent(spellEntity));
        }

        return playSpellEvents;
    }

    private MixedManaAmount getMixedManaAmount(int entity) {
        int neutralMana = getManaAmount(entity, Components.ManaAmount.NEUTRAL);
        int whiteMana = getManaAmount(entity, Components.ManaAmount.WHITE);
        int redMana = getManaAmount(entity, Components.ManaAmount.RED);
        int greenMana = getManaAmount(entity, Components.ManaAmount.GREEN);
        int blueMana = getManaAmount(entity, Components.ManaAmount.BLUE);
        int blackMana = getManaAmount(entity, Components.ManaAmount.BLACK);
        return new MixedManaAmount(neutralMana, whiteMana, redMana, greenMana, blueMana, blackMana);
    }

    private int getManaAmount(int entity, ComponentDefinition<Integer> manaComponent) {
        return data.getOptionalComponent(entity, manaComponent).orElse(0);
    }

    private void generateAttacks(int player, Consumer<Event> out) {
        if (data.hasComponentValue(player, Components.Game.TURN_PHASE, TurnPhase.ATTACK)) {
            for (int attacker : data.query(Components.CREATURE_ZONE).list(ownedBy(player).and(x -> !data.hasComponent(x, Components.DECLARED_ATTACK)))) {
                for (int targetPlayer : data.query(Components.NEXT_PLAYER).list(x -> x != player)) {
                    out.accept(new DeclareAttackEvent(attacker, targetPlayer));
                }
            }
        }
    }

    private void generateBlocks(int player, Consumer<Event> out) {
        if (data.hasComponentValue(player, Components.Game.TURN_PHASE, TurnPhase.BLOCK)) {
            for (int defender : data.query(Components.CREATURE_ZONE).list(ownedBy(player)
                    .and(x -> !data.hasComponent(x, Components.TAPPED))
                    .and(x -> !data.hasComponent(x, Components.DECLARED_BLOCK)))) {
                for (int attacker : data.query(Components.CREATURE_ZONE).list(x -> data.hasComponentValue(x, Components.DECLARED_ATTACK, player))) {
                    out.accept(new DeclareBlockEvent(defender, attacker));
                }
            }
        }
    }

    private void generateEndPhase(int player, Consumer<Event> out) {
        data.getOptionalComponent(player, Components.Game.TURN_PHASE).ifPresent(phase -> {
            switch (phase) {
                case MAIN_ONE:
                    out.accept(new EndMainPhaseOneEvent(player));
                    break;
                case ATTACK:
                    out.accept(new EndAttackPhaseEvent(player));
                    break;
                case BLOCK:
                    out.accept(new EndBlockPhaseEvent(player));
                    break;
                case MAIN_TWO:
                    out.accept(new EndMainPhaseTwoEvent(player));
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
