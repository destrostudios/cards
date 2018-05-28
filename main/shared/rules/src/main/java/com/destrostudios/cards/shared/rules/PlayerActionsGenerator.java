package com.destrostudios.cards.shared.rules;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.battle.*;
import com.destrostudios.cards.shared.rules.cards.*;
import com.destrostudios.cards.shared.rules.game.*;

import java.util.LinkedList;
import java.util.List;
import java.util.function.IntPredicate;

public class PlayerActionsGenerator {

    public static List<Event> generatePossibleActions(EntityData entityData, int playerEntity) {
        List<Event> possibleEvents = new LinkedList<>();
        // TODO: Replace with actual action generation
        if (entityData.hasComponent(playerEntity, Components.ACTIVE_PLAYER)) {
            IntPredicate ownedByPlayer = cardEntity -> entityData.hasComponentValue(cardEntity, Components.OWNED_BY, playerEntity);
            List<Integer> ownedCardEntities = entityData.query(Components.OWNED_BY).list(ownedByPlayer);
            for (int cardEntity : ownedCardEntities) {
                int[] spellEntities = entityData.getComponent(cardEntity, Components.SPELL_ENTITIES);
                if (spellEntities != null) {
                    for (int spellEntity : spellEntities) {
                        if ((entityData.hasComponent(spellEntity, Components.Spell.CastCondition.FROM_HAND) && entityData.hasComponent(cardEntity, Components.HAND_CARDS))
                         || (entityData.hasComponent(spellEntity, Components.Spell.CastCondition.FROM_BOARD) && entityData.hasComponent(cardEntity, Components.BOARD))) {
                            possibleEvents.add(new PlaySpellEvent(spellEntity));
                        }
                    }
                }
            }
            possibleEvents.add(new DrawCardEvent(playerEntity));
            possibleEvents.add(new EndTurnEvent(playerEntity));

            for (int attacker : entityData.query(Components.CREATURE_ZONE).list(ownedByPlayer)) {
                for (int defender : entityData.query(Components.CREATURE_ZONE).list(cardEntity -> !entityData.hasComponentValue(cardEntity, Components.OWNED_BY, playerEntity))) {
                    possibleEvents.add(new BattleEvent(attacker, defender));
                }
            }
        }
        return possibleEvents;
    }
}
