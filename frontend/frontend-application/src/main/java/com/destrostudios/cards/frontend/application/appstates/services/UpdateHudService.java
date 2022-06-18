package com.destrostudios.cards.frontend.application.appstates.services;

import com.destrostudios.cards.frontend.application.appstates.IngameHudAppState;
import com.destrostudios.cards.shared.rules.Components;

public class UpdateHudService {

    public UpdateHudService(GameService gameService, IngameHudAppState ingameHudAppState) {
        this.gameService = gameService;
        this.ingameHudAppState = ingameHudAppState;
    }
    private GameService gameService;
    private IngameHudAppState ingameHudAppState;

    public void update() {
        updatePlayerMana();
    }

    private void updatePlayerMana() {
        int manaAmount = gameService.getGameContext().getData().getOptionalComponent(gameService.getPlayerEntity(), Components.MANA).orElse(0);
        ingameHudAppState.setPlayerMana(manaAmount);
    }
}
