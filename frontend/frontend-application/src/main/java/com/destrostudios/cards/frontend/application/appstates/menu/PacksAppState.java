package com.destrostudios.cards.frontend.application.appstates.menu;

import com.destrostudios.cards.frontend.application.appstates.LoadingAppState;
import com.destrostudios.cards.frontend.application.gui.GuiUtil;
import com.destrostudios.cards.frontend.application.modules.GameDataClientModule;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Command;

public class PacksAppState extends MenuAppState {

    private Button buttonOpen;

    @Override
    public void initialize(AppStateManager stateManager, Application application) {
        super.initialize(stateManager, application);
        addButtons();
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
        GameDataClientModule gameDataClientModule = getModule(GameDataClientModule.class);
        Integer packsCount = ((gameDataClientModule.getUser() != null) ? gameDataClientModule.getUser().getPacks() : null);
        if (packsCount != null) {
            buttonOpen.setText((packsCount > 0) ? "Open pack (" + packsCount + ")" : "No packs left");
        }
        GuiUtil.setButtonEnabled(buttonOpen, ((packsCount != null) && (packsCount > 0)));
    }

    private void addButtons() {
        float margin = 50;
        float buttonWidth = 200;
        float buttonHeight = 100;
        float x = ((width / 2f) - (buttonWidth / 2));
        float y = (margin + buttonHeight);
        buttonOpen = addButton(null, x, y, buttonWidth, buttonHeight, b -> openPack());
        x = margin;
        addButton("Back", x, y, buttonWidth, buttonHeight, b -> switchTo(new MainMenuAppState()));
    }

    private Button addButton(String text, float x, float y, float buttonWidth, float buttonHeight, Command<Button> command) {
        Button button = addButton(text, buttonWidth, buttonHeight, command);
        button.setLocalTranslation(x, y, 0);
        return button;
    }

    private void openPack() {
        getModule(GameDataClientModule.class).openPack();
        waitForPackResult();
    }

    private void waitForPackResult() {
        mainApplication.getStateManager().attach(new LoadingAppState() {

            @Override
            protected boolean shouldClose() {
                return (getModule(GameDataClientModule.class).getPackResult() != null);
            }

            @Override
            protected void close() {
                super.close();
            }
        });
    }
}
