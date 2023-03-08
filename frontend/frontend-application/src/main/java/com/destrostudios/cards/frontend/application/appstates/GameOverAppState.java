package com.destrostudios.cards.frontend.application.appstates;

import com.destrostudios.cards.frontend.application.appstates.menu.PlayAppState;
import com.jme3.math.ColorRGBA;

public class GameOverAppState extends OverlayAppState {

    public GameOverAppState(boolean isWinner) {
        super(
            isWinner ? "Victory" : "Defeat",
            isWinner ? ColorRGBA.Green : ColorRGBA.Red,
            true
        );
    }

    @Override
    protected void close() {
        super.close();
        mainApplication.getStateManager().detach(mainApplication.getStateManager().getState(IngameAppState.class));
        mainApplication.getStateManager().attach(new PlayAppState());
    }
}
