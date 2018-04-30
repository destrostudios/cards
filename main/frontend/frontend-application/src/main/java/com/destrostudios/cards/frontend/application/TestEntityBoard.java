package com.destrostudios.cards.frontend.application;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.entities.EntityPool;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.turns.TurnPhase;

import java.util.Random;

/**
 *
 * @author Philipp
 */
public class TestEntityBoard {

    public static EntityData getTestEntityData() {
        Random random = new Random(453);
        EntityPool entities = new EntityPool(random);

        EntityData data = new EntityData();

        int player1 = entities.create();
        data.set(player1, Components.DISPLAY_NAME, "player1");
        data.set(player1, Components.TURN_PHASE, TurnPhase.MAIN);

        int player2 = entities.create();
        data.set(player2, Components.DISPLAY_NAME, "player2");

        data.set(player1, Components.NEXT_PLAYER, player2);
        data.set(player2, Components.NEXT_PLAYER, player1);

        int minion1 = entities.create();
        data.set(minion1, Components.HEALTH, 15);
        data.set(minion1, Components.DISPLAY_NAME, "Shyvana");
        data.set(minion1, Components.ATTACK, 2);
        data.set(minion1, Components.OWNED_BY, player2);
        data.set(minion1, Components.BOARD, null);
        data.set(minion1, Components.CREATURE_ZONE, 0);

        int minion2 = entities.create();
        data.set(minion2, Components.HEALTH, 5);
        data.set(minion2, Components.DISPLAY_NAME, "Aether Adept");
        data.set(minion2, Components.ATTACK, 5);
        data.set(minion2, Components.OWNED_BY, player2);
        data.set(minion2, Components.BOARD, null);
        data.set(minion2, Components.CREATURE_ZONE, 1);

        int hero = entities.create();
        data.set(hero, Components.HEALTH, 215);
        data.set(hero, Components.ATTACK, 3);
        data.set(hero, Components.DISPLAY_NAME, "hero");
        data.set(hero, Components.ARMOR, 1);
        data.set(hero, Components.OWNED_BY, player1);

        int librarySize = 2;
        for (int i = 0; i < 2 * librarySize; i++) {
            int card = entities.create();
            data.set(card, Components.CARD_TEMPLATE, i);
            data.set(card, Components.DISPLAY_NAME, "Shyvana");
            data.set(card, Components.OWNED_BY, i < librarySize ? player1 : player2);
            data.set(card, Components.LIBRARY, i % librarySize);
        }

        return data;
    }
}
