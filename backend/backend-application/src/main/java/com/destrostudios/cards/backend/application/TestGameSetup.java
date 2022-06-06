package com.destrostudios.cards.backend.application;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.PlayerInfo;
import com.destrostudios.cards.shared.rules.StartGameInfo;
import com.destrostudios.cards.shared.rules.cards.Foil;

public class TestGameSetup {

    private final EntityData data;
    private final StartGameInfo startGameInfo;

    public TestGameSetup(EntityData data, StartGameInfo startGameInfo) {
        this.data = data;
        this.startGameInfo = startGameInfo;
    }

    public void apply() {
        int player1 = data.createEntity();
        int player2 = data.createEntity();
        initPlayer(player1, player2, startGameInfo.getPlayer1());
        initPlayer(player2, player1, startGameInfo.getPlayer2());
    }

    private void initPlayer(int player, int opponent, PlayerInfo playerInfo) {
        data.setComponent(player, Components.NAME, playerInfo.getLogin());
        data.setComponent(player, Components.HEALTH, 30);
        data.setComponent(player, Components.NEXT_PLAYER, opponent);
        int[] deck = TestLibraries.getLibrary(data);
        for (int i = 0; i < deck.length; i++) {
            int card = deck[i];
            setRandomFoil(card);
            data.setComponent(card, Components.OWNED_BY, player);
            data.setComponent(card, Components.LIBRARY, i);
        }
    }

    private void setRandomFoil(int card) {
        switch ((int) (Math.random() * 7)) {
            case 0: data.setComponent(card, Components.FOIL, Foil.ARTWORK); break;
            case 1: data.setComponent(card, Components.FOIL, Foil.FULL); break;
        }
    }
}
