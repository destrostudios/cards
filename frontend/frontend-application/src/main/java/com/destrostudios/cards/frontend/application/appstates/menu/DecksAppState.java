package com.destrostudios.cards.frontend.application.appstates.menu;

import com.destrostudios.cards.frontend.application.appstates.LoadingAppState;
import com.destrostudios.cards.frontend.application.gui.GuiUtil;
import com.destrostudios.cards.frontend.application.modules.GameDataClientModule;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Command;

public class DecksAppState extends MenuAppState {

    private ModeAndDeckSelector modeAndDeckSelector;
    private Button buttonEdit;
    private Button buttonDelete;

    @Override
    public void initialize(AppStateManager stateManager, Application application) {
        super.initialize(stateManager, application);
        addTitle("Collection");
        modeAndDeckSelector = new ModeAndDeckSelector(true);
        addComponent(modeAndDeckSelector, 50, (height - GuiUtil.BUTTON_HEIGHT_DEFAULT));
        addButtons();
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
        boolean isDeckSelected = (modeAndDeckSelector.getDeck() != null);
        GuiUtil.setButtonEnabled(buttonEdit, isDeckSelected);
        GuiUtil.setButtonEnabled(buttonDelete, isDeckSelected);
    }

    private void addButtons() {
        float margin = 50;
        float buttonWidth = 200;
        float buttonHeight = 100;
        float x = (width - margin - buttonWidth);
        float y = (margin + buttonHeight);
        buttonDelete = addButton("Delete", x, y, buttonWidth, buttonHeight, b -> deleteDeck());
        x -= buttonWidth;
        buttonEdit = addButton("Edit", x, y, buttonWidth, buttonHeight, b -> editDeck());
        x -= buttonWidth;
        addButton("Create", x, y, buttonWidth, buttonHeight, b -> createDeck());
        x = margin;
        addButton("Back", x, y, buttonWidth, buttonHeight, b -> switchTo(new MainMenuAppState()));
    }

    private Button addButton(String text, float x, float y, float buttonWidth, float buttonHeight, Command<Button> command) {
        Button button = addButton(text, buttonWidth, buttonHeight, command);
        button.setLocalTranslation(x, y, 0);
        return button;
    }

    private void createDeck() {
        getModule(GameDataClientModule.class).createDeck(modeAndDeckSelector.getMode());
        waitForUpdatedDecks();
    }

    private void editDeck() {
        switchTo(new DeckAppState(modeAndDeckSelector.getMode(), modeAndDeckSelector.getDeck()));
    }

    private void deleteDeck() {
        getModule(GameDataClientModule.class).deleteDeck(modeAndDeckSelector.getMode(), modeAndDeckSelector.getDeck());
        waitForUpdatedDecks();
    }

    private void waitForUpdatedDecks() {
        mainApplication.getStateManager().attach(new LoadingAppState() {

            @Override
            protected boolean shouldClose() {
                return (getModule(GameDataClientModule.class).getUser() != null);
            }

            @Override
            protected void close() {
                super.close();
                modeAndDeckSelector.updateDecks();
            }
        });
    }
}
