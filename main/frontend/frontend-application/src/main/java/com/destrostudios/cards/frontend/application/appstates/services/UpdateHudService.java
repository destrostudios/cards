package com.destrostudios.cards.frontend.application.appstates.services;

import com.destrostudios.cards.frontend.application.SimpleGameClient;
import com.destrostudios.cards.frontend.application.appstates.IngameHudAppState;
import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.rules.Components;

public class UpdateHudService {

    public UpdateHudService(SimpleGameClient gameClient, IngameHudAppState ingameHudAppState) {
        this.gameClient = gameClient;
        this.ingameHudAppState = ingameHudAppState;
    }
    private SimpleGameClient gameClient;
    private IngameHudAppState ingameHudAppState;

    public void update() {
        updateHealths();
        updatePlayerMana();
    }

    private void updateHealths() {
        EntityData entityData = gameClient.getGame().getData();
        for (int playerEntity : entityData.query(Components.NEXT_PLAYER).list()) {
            // TODO: Map (Currently, it's exactly entity 0 and 1)
            int playerIndex = playerEntity;
            int health = entityData.getComponent(playerEntity, Components.HEALTH);
            ingameHudAppState.sePlayerHealth(playerIndex, health);
        }
    }

    private void updatePlayerMana() {
        int neutralMana = getPlayerMana(Components.ManaAmount.NEUTRAL);
        int whiteMana = getPlayerMana(Components.ManaAmount.WHITE);
        int redMana = getPlayerMana(Components.ManaAmount.RED);
        int greenMana = getPlayerMana(Components.ManaAmount.GREEN);
        int blueMana = getPlayerMana(Components.ManaAmount.BLUE);
        int blackMana = getPlayerMana(Components.ManaAmount.BLACK);
        ingameHudAppState.sePlayerMana(neutralMana, whiteMana, redMana, greenMana, blueMana, blackMana);
    }

    private int getPlayerMana(ComponentDefinition<Integer> manaAmountComponent) {
        int playerEntity = gameClient.getPlayerEntity();
        return gameClient.getGame().getData().getOptionalComponent(playerEntity, manaAmountComponent).orElse(0);
    }
}
