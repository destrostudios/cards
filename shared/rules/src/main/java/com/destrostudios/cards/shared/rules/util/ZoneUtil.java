package com.destrostudios.cards.shared.rules.util;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.entities.IntList;
import com.destrostudios.cards.shared.rules.Components;

public class ZoneUtil {

    public static void addCardToZone(EntityData data, int card, ComponentDefinition<Integer> zone) {
        int owner = data.getComponent(card, Components.OWNED_BY);
        data.setComponent(card, zone, data.query(zone).count(c -> data.getComponent(c, Components.OWNED_BY) == owner));
        if (zone == Components.CREATURE_ZONE) {
            data.setComponent(card, Components.BOARD);
        }
    }

    public static Integer getTopLibraryCard(EntityData data, int player) {
        IntList libraryCards = data.query(Components.LIBRARY).list(card -> data.getComponent(card, Components.OWNED_BY) == player);
        return getTopMostCard(data, libraryCards, Components.LIBRARY);
    }

    public static Integer getTopMostCard(EntityData data, IntList cards, ComponentDefinition<Integer> zoneComponent) {
        Integer topCard = null;
        int topCardIndex = -1;
        for (int card : cards) {
            int cardIndex = data.getComponent(card, zoneComponent);
            if (cardIndex > topCardIndex) {
                topCard = card;
                topCardIndex = cardIndex;
            }
        }
        return topCard;
    }
}
