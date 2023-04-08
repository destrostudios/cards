package com.destrostudios.cards.shared.rules.util;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.rules.Components;

import java.util.List;

public class ZoneUtil {

    public static void addCardToZone(EntityData data, int card, ComponentDefinition<Integer> zone) {
        int owner = data.getComponent(card, Components.OWNED_BY);
        data.setComponent(card, zone, data.query(zone).count(c -> data.getComponent(c, Components.OWNED_BY) == owner));
        if (zone == Components.CREATURE_ZONE) {
            data.setComponent(card, Components.BOARD);
        }
    }

    public static Integer getTopLibraryCard(EntityData data, int player) {
        List<Integer> libraryCards = data.query(Components.LIBRARY).list();
        Integer topCard = null;
        int topCardIndex = -1;
        for (int card : libraryCards) {
            if (data.getComponent(card, Components.OWNED_BY) == player) {
                int cardIndex = data.getComponent(card, Components.LIBRARY);
                if (cardIndex > topCardIndex) {
                    topCard = card;
                    topCardIndex = cardIndex;
                }
            }
        }
        return topCard;
    }
}
