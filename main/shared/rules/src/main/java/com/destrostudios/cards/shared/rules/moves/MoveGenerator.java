package com.destrostudios.cards.shared.rules.moves;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.entities.collections.IntArrayList;
import com.destrostudios.cards.shared.events.ActionEvent;
import com.destrostudios.cards.shared.rules.Components;
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
    private final int phaseKey = Components.TURN_PHASE, attackerKey = Components.ATTACK, blockerKey = Components.HEALTH, ownedByKey = Components.OWNED_BY, declaredAttackerKey = Components.DECLARED_ATTACK;

    public MoveGenerator(EntityData data) {
        this.data = data;
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
                    IntArrayList availableBlockers = data.entities(blockerKey, x -> data.hasValue(x, ownedByKey, player));
                    IntArrayList declaredAttackers = data.entities(declaredAttackerKey, x -> data.hasValue(data.get(x, declaredAttackerKey), ownedByKey, player));
                    for (int attacker : declaredAttackers) {
                        int index = availableBlockers.indexOf(data.get(attacker, declaredAttackerKey));
                        if (index != -1) {
                            availableBlockers.swapRemoveAt(index);
                        }
                    }
                    for (int availableBlocker : availableBlockers) {
                        for (int declaredAttacker : declaredAttackers) {
                            eventConsumer.accept(new DeclareBlockEvent(availableBlocker, declaredAttacker));
                        }
                    }

                    eventConsumer.accept(new EndRespondPhaseEvent(player));
                    break;
                case MAIN:
                    IntArrayList availableAttackers = data.entities(attackerKey, x -> data.hasValue(x, ownedByKey, player), x -> !data.has(x, declaredAttackerKey));
                    IntArrayList availableTargets = data.entities(blockerKey, x -> !data.hasValue(x, ownedByKey, player));
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
