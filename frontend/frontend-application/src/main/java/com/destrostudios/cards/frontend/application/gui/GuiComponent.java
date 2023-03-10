package com.destrostudios.cards.frontend.application.gui;

import com.destrostudios.cards.frontend.application.FrontendJmeApplication;
import com.destrostudios.gametools.network.shared.modules.NetworkModule;
import com.jme3.scene.Node;
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

    protected <M extends NetworkModule> M getModule(Class<M> moduleClass) {
        return mainApplication.getToolsClient().getModule(moduleClass);
    }
}
