package com.destrostudios.cards.frontend.application.appstates.menu;

import com.destrostudios.cards.frontend.application.gui.GuiUtil;
import com.destrostudios.cards.frontend.application.modules.GameDataClientModule;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Command;

public class MainMenuAppState extends MenuAppState {

    @Override
    public void initialize(AppStateManager stateManager, Application application) {
        super.initialize(stateManager, application);
        addTitle("Cards");
        addButton(0, "Play", b -> switchTo(new PlayAppState()));
        addButton(1, "Collection", b -> switchTo(new DecksAppState()));
        int totalPacks = getModule(GameDataClientModule.class).getTotalPacks();
        Button buttonPacks = addButton(2, "Packs" + ((totalPacks > 0) ? " (" + totalPacks + ")" : ""), b -> switchTo(new PacksAppState()));
        GuiUtil.setButtonBackground(buttonPacks, (totalPacks > 0) ? GuiUtil.BUTTON_COLOR_HIGHLIGHT : null);
        addButton(3, "Exit", b -> mainApplication.stop());
    }

    private Button addButton(int index, String text, Command<Button> command) {
        float margin = 50;
        float buttonWidth = ((width - (5 * margin)) / 4);
        float buttonHeight = 100;
        float x = (margin + (index * (margin + buttonWidth)));
        float y = (margin + buttonHeight);
        Button button = addButton(text, buttonWidth, buttonHeight, command);
        button.setLocalTranslation(x, y, 0);
        return button;
    }
}
