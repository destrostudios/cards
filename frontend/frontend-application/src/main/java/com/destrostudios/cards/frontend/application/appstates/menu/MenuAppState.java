package com.destrostudios.cards.frontend.application.appstates.menu;

import com.destrostudios.cards.frontend.application.appstates.MyBaseAppState;
import com.jme3.app.Application;
import com.jme3.app.state.AppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Command;
import com.simsilica.lemur.HAlignment;
import com.simsilica.lemur.VAlignment;

public class MenuAppState extends MyBaseAppState {

    public MenuAppState() {
        this.guiNode = new Node();
    }
    protected static final float BUTTON_HEIGHT_DEFAULT = 50;
    protected int width;
    protected int height;
    protected Node guiNode;

    @Override
    public void initialize(AppStateManager stateManager, Application application){
        super.initialize(stateManager, application);
        mainApplication.getGuiNode().attachChild(guiNode);
        width = mainApplication.getContext().getSettings().getWidth();
        height = mainApplication.getContext().getSettings().getHeight();
    }

    protected Button addButton(String text, float width, float height, Command<Button> command) {
        Button button = new Button(text);
        button.setPreferredSize(new Vector3f(width, height, 0));
        button.setTextHAlignment(HAlignment.Center);
        button.setTextVAlignment(VAlignment.Center);
        button.setFontSize(16);
        button.setColor(ColorRGBA.White);
        button.setFocusColor(ColorRGBA.White);
        button.addCommands(Button.ButtonAction.Up, command);
        guiNode.attachChild(button);
        return button;
    }

    protected void switchTo(AppState appState) {
        mainApplication.getStateManager().detach(this);
        mainApplication.getStateManager().attach(appState);
    }

    @Override
    public void cleanup() {
        super.cleanup();
        mainApplication.getGuiNode().detachChild(guiNode);
    }
}