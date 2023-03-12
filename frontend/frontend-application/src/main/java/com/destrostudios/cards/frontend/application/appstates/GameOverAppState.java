package com.destrostudios.cards.frontend.application.appstates;

import com.destrostudios.cards.frontend.application.appstates.menu.PlayAppState;
import com.destrostudios.cards.frontend.application.modules.GameDataClientModule;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
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
    public void initialize(AppStateManager stateManager, Application application) {
        super.initialize(stateManager, application);
        // User might've gotten packs as a result of the game
        getModule(GameDataClientModule.class).refreshUser();
    }

    @Override
    protected void close() {
        super.close();
        mainApplication.getStateManager().detach(mainApplication.getStateManager().getState(IngameAppState.class));
        mainApplication.getStateManager().attach(new LoadingAppState() {

            @Override
            protected boolean shouldClose() {
                return (getModule(GameDataClientModule.class).getUser() != null);
            }

            @Override
            protected void close() {
                super.close();
                mainApplication.getStateManager().attach(new PlayAppState());
            }
        });
    }
}
