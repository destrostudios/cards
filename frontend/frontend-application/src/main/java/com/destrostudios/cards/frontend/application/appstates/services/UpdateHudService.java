package com.destrostudios.cards.frontend.application.appstates.services;

import com.destrostudios.cards.frontend.application.appstates.IngameHudAppState;
import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.util.StatsUtil;

public class UpdateHudService {

    public UpdateHudService(GameService gameService, IngameHudAppState ingameHudAppState) {
        this.gameService = gameService;
        this.ingameHudAppState = ingameHudAppState;
    }
    private GameService gameService;
    private IngameHudAppState ingameHudAppState;

    public void update() {
        updateHealths();
        updatePlayerMana();
    }

    private void updateHealths() {
        EntityData data = gameService.getGameContext().getData();
        for (int playerEntity : data.query(Components.NEXT_PLAYER).list()) {
            // TODO: Map (Currently, it's exactly entity 0 and 1)
            int playerIndex = playerEntity;
            String name = data.getComponent(playerEntity, Components.NAME);
            int health = StatsUtil.getEffectiveHealth(data, playerEntity);
            ingameHudAppState.sePlayerInfo(playerIndex, name, health);
        }
    }

    private void updatePlayerMana() {
        int manaAmount = getPlayerMana(Components.MANA);
        ingameHudAppState.setPlayerMana(manaAmount);
    }

    private int getPlayerMana(ComponentDefinition<Integer> manaAmountComponent) {
        return gameService.getGameContext().getData().getOptionalComponent(gameService.getPlayerEntity(), manaAmountComponent).orElse(0);
    }
}
