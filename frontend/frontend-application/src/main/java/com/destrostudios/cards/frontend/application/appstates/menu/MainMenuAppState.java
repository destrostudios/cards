package com.destrostudios.cards.frontend.application.appstates.menu;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.math.ColorRGBA;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Command;
import com.simsilica.lemur.Label;

public class MainMenuAppState extends MenuAppState {

    @Override
    public void initialize(AppStateManager stateManager, Application application) {
        super.initialize(stateManager, application);
        addTitle();
        addButton(0, "Play", b -> switchTo(new PlayAppState()));
        addButton(1, "Collection", b -> switchTo(new DeckAppState()));
        // addButton(2, "Packs", b -> switchTo(new PlayAppState()));
        addButton(3, "Exit", b -> System.exit(0));
    }

    private void addTitle() {
        Label label = new Label("Cards");
        label.setFontSize(64);
        float x = ((width / 2f) - (label.getPreferredSize().getX() / 2));
        float y = (height - 100);
        label.setLocalTranslation(x, y, 0);
        label.setColor(ColorRGBA.White);
        guiNode.attachChild(label);
    }

    private void addButton(int index, String text, Command<Button> command) {
        float margin = 50;
        float buttonWidth = ((width - (5 * margin)) / 4);
        float buttonHeight = 100;
        float x = (margin + (index * (margin + buttonWidth)));
        float y = (margin + buttonHeight);
        Button button = addButton(text, buttonWidth, buttonHeight, command);
        button.setLocalTranslation(x, y, 0);
    }
}
