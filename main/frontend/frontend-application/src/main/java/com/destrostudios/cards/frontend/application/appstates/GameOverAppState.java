package com.destrostudios.cards.frontend.application.appstates;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;

public class GameOverAppState extends MyBaseAppState {

    public GameOverAppState(boolean isWinner) {
        this.isWinner = isWinner;
    }
    private boolean isWinner;
    private Node guiNode = new Node();
    private BitmapText textGameOver;

    @Override
    public void initialize(AppStateManager stateManager, Application application) {
        super.initialize(stateManager, application);
        stateManager.detach(getAppState(IngameHudAppState.class));

        int width = mainApplication.getSettings().getWidth();
        int height = mainApplication.getSettings().getHeight();

        Geometry background = new Geometry("background", new Quad(width, height));
        Material materialBackground = new Material(mainApplication.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        materialBackground.setColor("Color", new ColorRGBA(0, 0, 0, 0.7f));
        materialBackground.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        background.setMaterial(materialBackground);
        mainApplication.getGuiNode().attachChild(background);

        BitmapFont guiFont = mainApplication.getAssetManager().loadFont("Interface/Fonts/Default.fnt");
        textGameOver = new BitmapText(guiFont);
        textGameOver.setSize(100);
        if (isWinner) {
            textGameOver.setColor(ColorRGBA.Green);
            textGameOver.setText("Victory");
        }
        else {
            textGameOver.setColor(ColorRGBA.Red);
            textGameOver.setText("Defeat");
        }
        float x = ((width / 2f) - (textGameOver.getLineWidth() / 2));
        float y = ((height / 2f) + (textGameOver.getHeight() / 2));
        textGameOver.setLocalTranslation(x, y, 0);
        guiNode.attachChild(textGameOver);

        mainApplication.getGuiNode().attachChild(guiNode);
    }

    @Override
    public void cleanup() {
        super.cleanup();
        mainApplication.getGuiNode().detachChild(guiNode);
    }
}