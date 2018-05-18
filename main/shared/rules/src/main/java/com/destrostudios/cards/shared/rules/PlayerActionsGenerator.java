package com.destrostudios.cards.shared.rules;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.entities.collections.IntArrayList;
import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.battle.DamageEvent;
import com.destrostudios.cards.shared.rules.cards.DrawCardEvent;
import com.destrostudios.cards.shared.rules.cards.PlayCardFromHandEvent;
import com.destrostudios.cards.shared.rules.game.EndTurnEvent;

import java.util.LinkedList;
import java.util.List;

public class PlayerActionsGenerator {

    public static List<Event> generatePossibleActions(EntityData entityData, int playerEntity) {
        List<Event> possibleEvents = new LinkedList<>();
        // TODO: Replace with actual action generation
        if (entityData.hasComponent(playerEntity, Components.ACTIVE_PLAYER)) {
            IntArrayList ownedCardEntities = entityData.entities(Components.OWNED_BY, cardEntity -> entityData.hasComponentValue(cardEntity, Components.OWNED_BY, playerEntity));
            for (int cardEntity : ownedCardEntities) {
                Integer cardZoneIndex = entityData.getComponent(cardEntity, Components.HAND_CARDS);
                if (cardZoneIndex != null) {
                    possibleEvents.add(new PlayCardFromHandEvent(cardEntity));
                } else {
                    cardZoneIndex = entityData.getComponent(cardEntity, Components.CREATURE_ZONE);
                    if (cardZoneIndex != null) {
                        possibleEvents.add(new DamageEvent(cardEntity, 1));
                    }
                }
            }
            possibleEvents.add(new DrawCardEvent(playerEntity));
            possibleEvents.add(new EndTurnEvent(playerEntity));
        }
        return possibleEvents;
    }
}
