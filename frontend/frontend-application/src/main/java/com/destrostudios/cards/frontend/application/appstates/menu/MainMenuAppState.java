package com.destrostudios.cards.frontend.application.appstates.menu;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.simsilica.lemur.Button;

public class MainMenuAppState extends MenuAppState {

    @Override
    public void initialize(AppStateManager stateManager, Application application) {
        super.initialize(stateManager, application);
        Button buttonPlay = addButton("Play", 200, BUTTON_HEIGHT_DEFAULT, button -> switchTo(new PlayAppState()));
        buttonPlay.setLocalTranslation(50, 250, 0);
    }
}
