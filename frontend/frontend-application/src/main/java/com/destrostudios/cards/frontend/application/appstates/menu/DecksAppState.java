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
        modeAndDeckSelector = new ModeAndDeckSelector();
        addComponent(modeAndDeckSelector, 50, (height - GuiUtil.BUTTON_HEIGHT_DEFAULT));
        addDeckButtons();
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
        boolean isDeckSelected = (modeAndDeckSelector.getDeck() != null);
        GuiUtil.setButtonEnabled(buttonEdit, isDeckSelected);
        GuiUtil.setButtonEnabled(buttonDelete, isDeckSelected);
    }

    private void addDeckButtons() {
        float margin = 50;
        float buttonWidth = 200;
        float x = margin;
        addDeckButton("Create", x, buttonWidth, b -> createDeck());
        x += buttonWidth;
        buttonEdit = addDeckButton("Edit", x, buttonWidth, b -> editDeck());
        x += buttonWidth;
        buttonDelete = addDeckButton("Delete", x, buttonWidth, b -> deleteDeck());
        x = (width - margin - buttonWidth);
        addDeckButton("Back", x, buttonWidth, b -> switchTo(new MainMenuAppState()));
    }

    private Button addDeckButton(String text, float x, float buttonWidth, Command<Button> command) {
        float margin = 50;
        float buttonHeight = 100;
        float y = (margin + buttonHeight);
        Button button = addButton(text, buttonWidth, buttonHeight, command);
        button.setLocalTranslation(x, y, 0);
        return button;
    }

    private void createDeck() {
        getModule(GameDataClientModule.class).createUserCardList(modeAndDeckSelector.getMode().getId());
        waitForUpdatedDecks();
    }

    private void editDeck() {
        switchTo(new DeckAppState(modeAndDeckSelector.getDeck()));
    }

    private void deleteDeck() {
        getModule(GameDataClientModule.class).deleteUserCardList(modeAndDeckSelector.getDeck().getId());
        waitForUpdatedDecks();
    }

    private void waitForUpdatedDecks() {
        mainApplication.getStateManager().attach(new LoadingAppState() {

            @Override
            protected boolean shouldClose() {
                return (getModule(GameDataClientModule.class).getUserCardLists() != null);
            }

            @Override
            protected void close() {
                super.close();
                modeAndDeckSelector.updateDecks();
            }
        });
    }
}
