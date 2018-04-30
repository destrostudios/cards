package com.destrostudios.cards.shared.rules.moves;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.entities.collections.IntArrayList;
import com.destrostudios.cards.shared.events.Event;
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

    public MoveGenerator(EntityData data) {
        this.data = data;
    }

    public List<Event> generateAvailableMoves(int player) {
        List<Event> result = new ArrayList<>();
        generateAvailableMoves(player, result::add);
        return result;
    }

    public void generateAvailableMoves(int player, Consumer<Event> eventConsumer) {
        TurnPhase phase = data.get(player, Components.TURN_PHASE);
        if (phase != null) {
            switch (phase) {
                case RESPOND:
                    IntArrayList availableBlockers = data.entities(Components.HEALTH, x -> data.hasValue(x, Components.OWNED_BY, player));
                    IntArrayList declaredAttackers = data.entities(Components.DECLARED_ATTACK, x -> data.hasValue(data.get(x, Components.DECLARED_ATTACK), Components.OWNED_BY, player));
                    for (int attacker : declaredAttackers) {
                        int index = availableBlockers.indexOf(data.get(attacker, Components.DECLARED_ATTACK));
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
                    IntArrayList availableAttackers = data.entities(Components.ATTACK, x -> data.hasValue(x, Components.OWNED_BY, player), x -> !data.has(x, Components.DECLARED_ATTACK));
                    IntArrayList availableTargets = data.entities(Components.HEALTH, x -> !data.hasValue(x, Components.OWNED_BY, player));
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
