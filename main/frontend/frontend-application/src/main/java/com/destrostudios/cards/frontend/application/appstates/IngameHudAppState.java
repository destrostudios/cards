package com.destrostudios.cards.frontend.application.appstates;

import com.destrostudios.cards.shared.rules.game.phases.TurnPhase;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

public class IngameHudAppState extends MyBaseAppState {

    private float margin = 10;

    private BitmapText[] textPlayerHealth = new BitmapText[2];
    private BitmapText textCurrentPlayerAndPhase;
    private BitmapText textPlayerManaLabel;
    private BitmapText textPlayerManaNoneLabel;
    private BitmapText[] textPlayerManaAmounts = new BitmapText[6];

    @Override
    public void initialize(AppStateManager stateManager, Application application) {
        super.initialize(stateManager, application);
        BitmapFont guiFont = mainApplication.getAssetManager().loadFont("Interface/Fonts/Default.fnt");
        textPlayerHealth[0] = new BitmapText(guiFont);
        textPlayerHealth[1] = new BitmapText(guiFont);
        textCurrentPlayerAndPhase = new BitmapText(guiFont);
        textPlayerManaLabel = new BitmapText(guiFont);
        textPlayerManaLabel.setText("Your Mana: ");
        textPlayerManaNoneLabel = new BitmapText(guiFont);
        textPlayerManaNoneLabel.setText("-");
        for (int i = 0; i < textPlayerManaAmounts.length; i++) {
            textPlayerManaAmounts[i] = new BitmapText(guiFont);
        }
        textPlayerManaAmounts[0].setColor(ColorRGBA.Gray);
        textPlayerManaAmounts[1].setColor(ColorRGBA.White);
        textPlayerManaAmounts[2].setColor(ColorRGBA.Red);
        textPlayerManaAmounts[3].setColor(ColorRGBA.Green);
        textPlayerManaAmounts[4].setColor(ColorRGBA.Blue);
        textPlayerManaAmounts[5].setColor(ColorRGBA.Black);

        float x = margin;
        float y = mainApplication.getSettings().getHeight() - margin;
        textPlayerHealth[0].setLocalTranslation(x, y, 0);
        y -= textPlayerHealth[0].getLineHeight() + margin;
        textPlayerHealth[1].setLocalTranslation(x, y, 0);
        y -= textPlayerHealth[1].getLineHeight() + margin;
        textCurrentPlayerAndPhase.setLocalTranslation(x, y, 0);
        y -= textCurrentPlayerAndPhase.getLineHeight() + margin;
        textPlayerManaLabel.setLocalTranslation(x, y, 0);
        x += textPlayerManaLabel.getLineWidth();
        textPlayerManaNoneLabel.setLocalTranslation(x, y, 0);

        mainApplication.getGuiNode().attachChild(textPlayerHealth[0]);
        mainApplication.getGuiNode().attachChild(textPlayerHealth[1]);
        mainApplication.getGuiNode().attachChild(textCurrentPlayerAndPhase);
        mainApplication.getGuiNode().attachChild(textPlayerManaLabel);
        mainApplication.getGuiNode().attachChild(textPlayerManaNoneLabel);
        for (BitmapText textPlayerManaAmount : textPlayerManaAmounts) {
            mainApplication.getGuiNode().attachChild(textPlayerManaAmount);
        }
    }

    public void setCurrentPlayerAndPhase(int playerIndex, TurnPhase turnPhase) {
        textCurrentPlayerAndPhase.setText(turnPhase.name() + " (Player #"+ (playerIndex + 1) + ")");
    }

    public void sePlayerHealth(int playerIndex, int health) {
        textPlayerHealth[playerIndex].setText("PlayerName#" + (playerIndex + 1) + " - " + health + " HP");
    }

    public void sePlayerMana(int neutralMana, int whiteMana, int redMana, int greenMana, int blueMana, int blackMana) {
        float x = 0;
        x = updatePlayerManaAmount(textPlayerManaAmounts[0], neutralMana, x);
        x = updatePlayerManaAmount(textPlayerManaAmounts[1], whiteMana, x);
        x = updatePlayerManaAmount(textPlayerManaAmounts[2], redMana, x);
        x = updatePlayerManaAmount(textPlayerManaAmounts[3], greenMana, x);
        x = updatePlayerManaAmount(textPlayerManaAmounts[4], blueMana, x);
        x = updatePlayerManaAmount(textPlayerManaAmounts[5], blackMana, x);
        textPlayerManaNoneLabel.setCullHint((x > 0) ? Spatial.CullHint.Always : Spatial.CullHint.Inherit);
    }

    private float updatePlayerManaAmount(BitmapText textPlayerManaAmount,  int manaAmount, float x) {
        if (manaAmount > 0) {
            Vector3f textPosition = textPlayerManaLabel.getLocalTranslation().add(textPlayerManaLabel.getLineWidth() + x, 0, 0);
            textPlayerManaAmount.setLocalTranslation(textPosition);
            textPlayerManaAmount.setText("" + manaAmount);
            textPlayerManaAmount.setCullHint(Spatial.CullHint.Inherit);
            return (x + textPlayerManaAmount.getLineWidth() + margin);
        }
        textPlayerManaAmount.setCullHint(Spatial.CullHint.Always);
        return x;
    }
}
