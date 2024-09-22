package com.destrostudios.cards.shared.rules.util;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.entities.IntList;
import com.destrostudios.cards.shared.rules.Components;

public class ZoneUtil {

    public static void addToZone(EntityData data, int card, int owner, ComponentDefinition<Void> cardZone, ComponentDefinition<Void> cardPlayerZone, ComponentDefinition<IntList> playerZone) {
        // Enable check to catch any refactoring issues
        /*if (data.hasComponent(card, cardZone) || data.hasComponent(card, cardPlayerZone) || data.getComponent(owner, playerZone).contains(card)) {
            throw new RuntimeException("Card " + card + " is already in zone " + cardZone.getName());
        }*/
        data.setComponent(card, cardZone);
        data.setComponent(card, cardPlayerZone);
        IntList newPlayerCards = data.getComponent(owner, playerZone).copy();
        newPlayerCards.add(card);
        data.setComponent(owner, playerZone, newPlayerCards);
    }

    public static void removeFromZone(EntityData data, int card, int owner, ComponentDefinition<Void> cardZone, ComponentDefinition<Void> cardPlayerZone, ComponentDefinition<IntList> playerZone) {
        // Enable check to catch any refactoring issues
        /*if (!data.hasComponent(card, cardZone) || !data.hasComponent(card, cardPlayerZone) || !data.getComponent(owner, playerZone).contains(card)) {
            throw new RuntimeException("Card " + card + " is not in zone " + cardZone.getName());
        }*/
        data.removeComponent(card, cardZone);
        data.removeComponent(card, cardPlayerZone);
        IntList newPlayerCards = data.getComponent(owner, playerZone).copy();
        newPlayerCards.removeFirstUnsafe(card);
        data.setComponent(owner, playerZone, newPlayerCards);
    }

    public static Integer getTopLibraryCard(EntityData data, int player) {
        IntList libraryCards = data.getComponent(player, Components.Player.LIBRARY_CARDS);
        return (libraryCards.nonEmpty() ? libraryCards.get(libraryCards.size() - 1) : null);
    }
}
