package com.destrostudios.cards.frontend.application.appstates;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.scene.Node;

public class IngameHudAppState extends MyBaseAppState {

    private float margin = 10;

    private Node guiNode = new Node();
    private BitmapText textActivePlayer;
    private BitmapText textPlayerManaLabel;
    private BitmapText textPlayerManaAmount;

    @Override
    public void initialize(AppStateManager stateManager, Application application) {
        super.initialize(stateManager, application);
        BitmapFont guiFont = mainApplication.getAssetManager().loadFont("Interface/Fonts/Default.fnt");
        textActivePlayer = new BitmapText(guiFont);
        textPlayerManaLabel = new BitmapText(guiFont);
        textPlayerManaLabel.setText("Your Mana: ");
        textPlayerManaAmount = new BitmapText(guiFont);
        textPlayerManaAmount.setText("-");

        float x = margin;
        float y = mainApplication.getContext().getSettings().getHeight() - margin;
        textActivePlayer.setLocalTranslation(x, y, 0);
        y -= textActivePlayer.getLineHeight() + margin;
        textPlayerManaLabel.setLocalTranslation(x, y, 0);
        x += textPlayerManaLabel.getLineWidth();
        textPlayerManaAmount.setLocalTranslation(x, y, 0);

        guiNode.attachChild(textActivePlayer);
        guiNode.attachChild(textPlayerManaLabel);
        guiNode.attachChild(textPlayerManaAmount);
        mainApplication.getGuiNode().attachChild(guiNode);
    }

    @Override
    public void cleanup() {
        super.cleanup();
        mainApplication.getGuiNode().detachChild(guiNode);
    }

    public void setActivePlayer(String playerName) {
        textActivePlayer.setText(playerName + "'s turn");
    }

    public void setPlayerMana(int manaAmount) {
        textPlayerManaAmount.setText("" + manaAmount);
    }
}
