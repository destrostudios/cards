package com.destrostudios.cards.frontend.application.appstates.services;

import com.destrostudios.cards.frontend.application.gui.GuiUtil;
import com.jme3.scene.Node;
import com.simsilica.lemur.Button;
import lombok.Getter;

public class IngameGuiService {

    public IngameGuiService(Node parentNode, Runnable tryEndTurn) {
        this.parentNode = parentNode;
        this.tryEndTurn = tryEndTurn;
    }
    private Node parentNode;
    private Runnable tryEndTurn;
    @Getter
    private Node guiNode;
    private Button buttonEndTurn;

    public void init(int width, int height) {
        guiNode = new Node();

        float buttonWidth = 0.12f * width;
        float buttonHeight = 0.06f * height;
        float buttonMarginRight = 0.02f * width;
        float buttonX = width - buttonMarginRight - buttonWidth;
        float buttonY = 0.605f * height;
        buttonEndTurn = GuiUtil.createButton("End turn", buttonWidth, buttonHeight, _ -> tryEndTurn.run());
        buttonEndTurn.setLocalTranslation(buttonX, buttonY, 0);
        guiNode.attachChild(buttonEndTurn);
    }

    public void setAttached(boolean attached) {
        if (attached) {
            parentNode.attachChild(guiNode);
        } else {
            parentNode.detachChild(guiNode);
        }
    }

    public void update(boolean canEndTurn) {
        GuiUtil.setButtonEnabled(buttonEndTurn, canEndTurn);
    }
}
