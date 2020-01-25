package com.destrostudios.cards.backend.application;

import com.destrostudios.cards.backend.application.templates.CardPool;
import com.destrostudios.cards.backend.application.templates.TestLibraries;
import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.rules.Components;

import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author Philipp
 */
public class TestGameSetup {

    private final EntityData data;
    private final int[] players = new int[2];

    public TestGameSetup(EntityData data) {
        this.data = data;
    }

    public void apply() {
        int player1 = data.createEntity();
        int player2 = data.createEntity();
        initPlayer(player1, player2, "Player1", TestLibraries.red(), 20);
        initPlayer(player2, player1, "Player2", TestLibraries.red(), 20);
    }

    private void initPlayer(int player, int opponent, String name, CardPool cards, int librarySize) {
        data.setComponent(player, Components.DISPLAY_NAME, name);
        data.setComponent(player, Components.HEALTH, 20);
        data.setComponent(player, Components.NEXT_PLAYER, opponent);
        for (int i = 0; i < librarySize; i++) {
            int card = cards.selectRandomCard(ThreadLocalRandom.current()::nextInt).create(data);
            data.setComponent(card, Components.OWNED_BY, player);
            data.setComponent(card, Components.LIBRARY, i);
        }
    }

    public int[] getPlayers() {
        return players;
    }
}
