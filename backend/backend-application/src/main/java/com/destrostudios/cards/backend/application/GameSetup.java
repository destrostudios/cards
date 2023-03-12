package com.destrostudios.cards.backend.application;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.entities.templates.EntityTemplate;
import com.destrostudios.cards.shared.model.Card;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.PlayerInfo;
import com.destrostudios.cards.shared.rules.StartGameInfo;
import com.destrostudios.cards.shared.rules.cards.Foil;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class GameSetup {

    private List<Card> cards;
    private EntityData data;
    private StartGameInfo startGameInfo;

    public void apply() {
        int player1 = data.createEntity();
        int player2 = data.createEntity();
        initPlayer(player1, player2, startGameInfo.getPlayer1());
        initPlayer(player2, player1, startGameInfo.getPlayer2());
    }

    private void initPlayer(int player, int opponent, PlayerInfo playerInfo) {
        data.setComponent(player, Components.NAME, playerInfo.getLogin());
        data.setComponent(player, Components.NEXT_PLAYER, opponent);
        data.setComponent(player, Components.Stats.HEALTH, 30);
        data.setComponent(player, Components.BOARD);
        List<Integer> library;
        if (playerInfo.getLibraryTemplates().isEmpty()) {
            library = createLibrary(data);
        } else {
            library = playerInfo.getLibraryTemplates().stream().map(template -> EntityTemplate.createFromTemplate(data, template)).collect(Collectors.toList());
        }
        int i = 0;
        for (int card : library) {
            setRandomFoil(card);
            data.setComponent(card, Components.OWNED_BY, player);
            data.setComponent(card, Components.LIBRARY, i);
            i++;
        }
    }

    private List<Integer> createLibrary(EntityData data) {
        return cards.stream().map(card -> EntityTemplate.createFromTemplate(data, card.getPath())).collect(Collectors.toList());
    }

    private void setRandomFoil(int card) {
        switch ((int) (Math.random() * 7)) {
            case 0: data.setComponent(card, Components.FOIL, Foil.ARTWORK); break;
            case 1: data.setComponent(card, Components.FOIL, Foil.FULL); break;
        }
    }
}
