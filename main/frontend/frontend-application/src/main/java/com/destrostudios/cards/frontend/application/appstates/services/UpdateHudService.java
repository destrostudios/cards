package com.destrostudios.cards.frontend.application.appstates.services;

import com.destrostudios.cards.frontend.application.SimpleGameClient;
import com.destrostudios.cards.frontend.application.appstates.IngameHudAppState;
import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.util.HealthUtil;

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
            int playerHealth = HealthUtil.getEffectiveHealth(entityData, playerEntity);
            ingameHudAppState.sePlayerHealth(playerIndex, playerHealth);
        }
    }

    private void updatePlayerMana() {
        int manaAmount = getPlayerMana(Components.MANA);
        ingameHudAppState.setPlayerMana(manaAmount);
    }

    private int getPlayerMana(ComponentDefinition<Integer> manaAmountComponent) {
        int playerEntity = gameClient.getPlayerEntity();
        return gameClient.getGame().getData().getOptionalComponent(playerEntity, manaAmountComponent).orElse(0);
    }
}
