package com.destrostudios.cards.shared.rules.moves;

import com.destrostudios.cards.shared.entities.ComponentValue;
import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.entities.collections.IntArrayList;
import com.destrostudios.cards.shared.events.ActionEvent;
import com.destrostudios.cards.shared.rules.battle.DeclareAttackEvent;
import com.destrostudios.cards.shared.rules.battle.DeclareBlockEvent;
import com.destrostudios.cards.shared.rules.turns.TurnPhase;
import com.destrostudios.cards.shared.rules.turns.main.EndMainPhaseEvent;
import com.destrostudios.cards.shared.rules.turns.respond.EndRespondPhaseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 *
 * @author Philipp
 */
public class MoveGenerator {

    private final EntityData data;
    private final int phaseKey, attackerKey, blockerKey, ownedByKey, declaredAttackerKey;

    public MoveGenerator(EntityData data, int phaseKey, int attackerKey, int blockerKey, int ownedByKey, int declaredAttackerKey) {
        this.data = data;
        this.phaseKey = phaseKey;
        this.attackerKey = attackerKey;
        this.blockerKey = blockerKey;
        this.ownedByKey = ownedByKey;
        this.declaredAttackerKey = declaredAttackerKey;
    }

    public List<ActionEvent> generateAvailableMoves(int player) {
        List<ActionEvent> result = new ArrayList<>();
        generateAvailableMoves(player, result::add);
        return result;
    }

    public void generateAvailableMoves(int player, Consumer<ActionEvent> eventConsumer) {
        int phase = data.getOrElse(player, phaseKey, -1);
        if (phase != -1) {
            switch (TurnPhase.values()[phase]) {
                case RESPOND:
                    IntArrayList availableBlockers = data.entitiesWithComponent(blockerKey, x -> data.hasValue(x, ownedByKey, player));
                    List<ComponentValue> declaredAttacks = data.entityComponentValues(declaredAttackerKey, x -> data.hasValue(x.getComponentValue(), ownedByKey, player));
                    for (ComponentValue declaredAttack : declaredAttacks) {
                        int index = availableBlockers.indexOf(declaredAttack.getComponentValue());
                        if (index != -1) {
                            availableBlockers.swapRemoveAt(index);
                        }
                    }
                    for (int availableBlocker : availableBlockers) {
                        for (ComponentValue declaredAttack : declaredAttacks) {
                            eventConsumer.accept(new DeclareBlockEvent(availableBlocker, declaredAttack.getEntity()));
                        }
                    }

                    eventConsumer.accept(new EndRespondPhaseEvent(player));
                    break;
                case MAIN:
                    IntArrayList availableAttackers = data.entitiesWithComponent(attackerKey, x -> data.hasValue(x, ownedByKey, player), x -> !data.has(x, declaredAttackerKey));
                    IntArrayList availableTargets = data.entitiesWithComponent(blockerKey, x -> !data.hasValue(x, ownedByKey, player));
                    for (int availableTarget : availableTargets) {
                        for (int availableAttacker : availableAttackers) {
                            eventConsumer.accept(new DeclareAttackEvent(availableAttacker, availableTarget));
                        }
                    }
                    eventConsumer.accept(new EndMainPhaseEvent(player));
                    break;

            }
        }
    }

}
