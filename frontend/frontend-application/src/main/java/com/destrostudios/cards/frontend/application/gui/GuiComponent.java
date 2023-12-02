package com.destrostudios.cards.frontend.application.gui;

import com.destrostudios.cards.frontend.application.FrontendJmeApplication;
import com.destrostudios.gametools.network.shared.modules.NetworkModule;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Label;
import lombok.Getter;

public class GuiComponent {

    protected FrontendJmeApplication mainApplication;
    protected int width;
    protected int height;
    @Getter
    protected Node guiNode;

    public void init(FrontendJmeApplication mainApplication) {
        this.mainApplication = mainApplication;
        width = mainApplication.getContext().getSettings().getWidth();
        height = mainApplication.getContext().getSettings().getHeight();
        guiNode = new Node();
    }

    protected void addLabel(String text, float y) {
        Label label = new Label(text);
        label.setFontSize(16);
        label.setLocalTranslation(0, y, 0);
        label.setColor(ColorRGBA.White);
        guiNode.attachChild(label);
    }

    protected void setButtonSelected(Button button, boolean selected) {
        GuiUtil.setButtonBackground(button, (selected ? GuiUtil.BUTTON_COLOR_SELECTED : null));
    }

    protected <M extends NetworkModule> M getModule(Class<M> moduleClass) {
        return mainApplication.getToolsClient().getModule(moduleClass);
    }
}
