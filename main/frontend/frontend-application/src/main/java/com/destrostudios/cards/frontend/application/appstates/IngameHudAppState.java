package com.destrostudios.cards.frontend.application.appstates;

import com.destrostudios.cards.shared.rules.game.phases.TurnPhase;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;

public class IngameHudAppState extends MyBaseAppState {

    private BitmapText[] textPlayerHealth = new BitmapText[2];
    private BitmapText textCurrentPlayerAndPhase;

    @Override
    public void initialize(AppStateManager stateManager, Application application) {
        super.initialize(stateManager, application);
        BitmapFont guiFont = mainApplication.getAssetManager().loadFont("Interface/Fonts/Default.fnt");
        float margin = 10;
        textPlayerHealth[0] = new BitmapText(guiFont);
        textPlayerHealth[1] = new BitmapText(guiFont);
        textCurrentPlayerAndPhase = new BitmapText(guiFont);

        float x = margin;
        float y = mainApplication.getSettings().getHeight() - margin;
        textPlayerHealth[0].setLocalTranslation(x, y, 0);
        y -= textPlayerHealth[0].getLineHeight() + margin;
        textPlayerHealth[1].setLocalTranslation(x, y, 0);
        y -= textPlayerHealth[1].getLineHeight() + margin;
        textCurrentPlayerAndPhase.setLocalTranslation(x, y, 0);

        mainApplication.getGuiNode().attachChild(textPlayerHealth[0]);
        mainApplication.getGuiNode().attachChild(textPlayerHealth[1]);
        mainApplication.getGuiNode().attachChild(textCurrentPlayerAndPhase);
    }

    public void setCurrentPlayerAndPhase(int playerIndex, TurnPhase turnPhase) {
        textCurrentPlayerAndPhase.setText(turnPhase.name() + " (Player #"+ (playerIndex + 1) + ")");
    }

    public void sePlayerHealth(int playerIndex, int health) {
        textPlayerHealth[playerIndex].setText("PlayerName#" + (playerIndex + 1) + " - " + health + " HP");
    }
}
