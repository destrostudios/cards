package com.destrostudios.cards.frontend.application.appstates;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.scene.Node;

public class IngameHudAppState extends MyBaseAppState {

    private float margin = 10;

    private Node guiNode = new Node();
    private BitmapText[] textPlayerHealth = new BitmapText[2];
    private BitmapText textCurrentPlayer;
    private BitmapText textPlayerManaLabel;
    private BitmapText textPlayerManaAmount;

    @Override
    public void initialize(AppStateManager stateManager, Application application) {
        super.initialize(stateManager, application);
        BitmapFont guiFont = mainApplication.getAssetManager().loadFont("Interface/Fonts/Default.fnt");
        textPlayerHealth[0] = new BitmapText(guiFont);
        textPlayerHealth[1] = new BitmapText(guiFont);
        textCurrentPlayer = new BitmapText(guiFont);
        textPlayerManaLabel = new BitmapText(guiFont);
        textPlayerManaLabel.setText("Your Mana: ");
        textPlayerManaAmount = new BitmapText(guiFont);
        textPlayerManaAmount.setText("-");

        float x = margin;
        float y = mainApplication.getSettings().getHeight() - margin;
        textPlayerHealth[0].setLocalTranslation(x, y, 0);
        y -= textPlayerHealth[0].getLineHeight() + margin;
        textPlayerHealth[1].setLocalTranslation(x, y, 0);
        y -= textPlayerHealth[1].getLineHeight() + margin;
        textCurrentPlayer.setLocalTranslation(x, y, 0);
        y -= textCurrentPlayer.getLineHeight() + margin;
        textPlayerManaLabel.setLocalTranslation(x, y, 0);
        x += textPlayerManaLabel.getLineWidth();
        textPlayerManaAmount.setLocalTranslation(x, y, 0);

        guiNode.attachChild(textPlayerHealth[0]);
        guiNode.attachChild(textPlayerHealth[1]);
        guiNode.attachChild(textCurrentPlayer);
        guiNode.attachChild(textPlayerManaLabel);
        guiNode.attachChild(textPlayerManaAmount);
        mainApplication.getGuiNode().attachChild(guiNode);
    }

    @Override
    public void cleanup() {
        super.cleanup();
        mainApplication.getGuiNode().detachChild(guiNode);
    }

    public void setCurrentPlayer(int playerIndex) {
        textCurrentPlayer.setText("Player #" + (playerIndex + 1));
    }

    public void sePlayerHealth(int playerIndex, int health) {
        textPlayerHealth[playerIndex].setText("PlayerName#" + (playerIndex + 1) + " - " + health + " HP");
    }

    public void setPlayerMana(int manaAmount) {
        textPlayerManaAmount.setText("" + manaAmount);
    }
}
