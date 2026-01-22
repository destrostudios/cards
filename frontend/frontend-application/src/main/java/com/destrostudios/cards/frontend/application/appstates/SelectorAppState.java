package com.destrostudios.cards.frontend.application.appstates;

import com.destrostudios.cards.frontend.application.gui.GuiUtil;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.*;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Label;

public abstract class SelectorAppState extends MyBaseAppState {

    public SelectorAppState(String description, Runnable onCancel, boolean hasSubmitButton) {
        this.description = description;
        this.onCancel = onCancel;
        this.hasSubmitButton = hasSubmitButton;
    }
    private String description;
    protected Runnable onCancel;
    protected boolean hasSubmitButton;
    protected Node rootNode;
    protected Node guiNode;
    protected Button buttonSubmit;

    @Override
    public void initialize(AppStateManager stateManager, Application application) {
        super.initialize(stateManager, application);
        rootNode = new Node();
        mainApplication.getRootNode().attachChild(rootNode);

        guiNode = new Node();
        mainApplication.getGuiNode().attachChild(guiNode);

        initGui();
    }

    private void initGui() {
        Geometry background = new Geometry("background", new Quad(1, 1));
        Material materialBackground = new Material(mainApplication.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        materialBackground.setColor("Color", new ColorRGBA(0, 0, 0, 0.7f));
        materialBackground.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        background.setMaterial(materialBackground);
        background.setLocalTranslation(-6, 1, 5);
        background.setLocalRotation(new Quaternion().fromAngleAxis(-1 * FastMath.HALF_PI, Vector3f.UNIT_X));
        background.setLocalScale(12, 7, 1);
        rootNode.attachChild(background);

        int width = mainApplication.getContext().getSettings().getWidth();
        int height = mainApplication.getContext().getSettings().getHeight();
        initGui(width, height);
    }

    protected void initGui(int width, int height) {
        Label labelDescription = new Label(description);
        labelDescription.setFontSize(24);
        float labelX = ((width / 2f) - (labelDescription.getPreferredSize().getX() / 2));
        float labelY = (height - 163);
        labelDescription.setLocalTranslation(labelX, labelY, 0);
        labelDescription.setColor(ColorRGBA.White);
        guiNode.attachChild(labelDescription);

        float buttonWidth = 200;
        float buttonHeight = 100;
        float buttonGapX = 30;

        float buttonX = (width / 2f) - ((getButtonCount() == 1) ? (buttonWidth / 2) : ((buttonGapX / 2) + buttonWidth));
        float buttonY = 280;

        if (onCancel != null) {
            Button buttonCancel = GuiUtil.createButton("Cancel", buttonWidth, buttonHeight, _ -> onCancel());
            buttonCancel.setLocalTranslation(buttonX, buttonY, 0);
            guiNode.attachChild(buttonCancel);
        }
        if (hasSubmitButton) {
            if (onCancel != null) {
                buttonX += buttonWidth + buttonGapX;
            }
            buttonSubmit = GuiUtil.createButton("Submit", buttonWidth, buttonHeight, _ -> onSubmit());
            buttonSubmit.setLocalTranslation(buttonX, buttonY, 0);
            guiNode.attachChild(buttonSubmit);
        }
    }

    protected Vector3f getCenter() {
        Vector3f center = new Vector3f(0, 3.8f, 1.32f);
        if (getButtonCount() > 0) {
            center.setZ(center.getZ() - 0.2f);
        }
        return center;
    }

    private int getButtonCount() {
        int buttonCount = 0;
        if (onCancel != null) {
            buttonCount++;
        }
        if (hasSubmitButton) {
            buttonCount++;
        }
        return buttonCount;
    }

    protected void onCancel() {
        if (onCancel != null) {
            onCancel.run();
        }
        close();
    }

    protected void onSubmit() {
        close();
    }

    private void close() {
        mainApplication.getStateManager().detach(this);
    }

    @Override
    public void cleanup() {
        super.cleanup();
        mainApplication.getRootNode().detachChild(rootNode);
        mainApplication.getGuiNode().detachChild(guiNode);
    }
}
