package com.destrostudios.cards.frontend.application.appstates.menu;

import com.destrostudios.cards.frontend.application.appstates.LoadingAppState;
import com.destrostudios.cards.frontend.application.gui.GuiUtil;
import com.destrostudios.cards.frontend.application.modules.GameDataClientModule;
import com.destrostudios.cards.shared.model.Deck;
import com.destrostudios.cards.shared.model.Mode;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Command;

public class CollectionAppState extends MenuAppState {

    private ModeSelector modeSelector;
    private DeckSelector deckSelector;
    private Button buttonEdit;
    private Button buttonDelete;

    @Override
    public void initialize(AppStateManager stateManager, Application application) {
        super.initialize(stateManager, application);
        addTitle("Collection");
        addButtons();
        modeSelector = new ModeSelector(true) {

            @Override
            public void selectMode(Mode mode) {
                super.selectMode(mode);
                updateDecks();
            }
        };
        deckSelector = new DeckSelector() {

            @Override
            public void selectDeck(Deck deck) {
                super.selectDeck(deck);
                boolean isDeckSelected = (deck != null);
                GuiUtil.setButtonEnabled(buttonEdit, isDeckSelected);
                GuiUtil.setButtonEnabled(buttonDelete, isDeckSelected);
            }
        };
        float x = 50;
        float y = (height - GuiUtil.BUTTON_HEIGHT_DEFAULT);
        // Initialize deck selector before mode selector, since auto selecting the initial mode will load its decks
        addComponent(deckSelector, x, y);
        addComponent(modeSelector, x, y);
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
        getModule(GameDataClientModule.class).createDeck(modeSelector.getMode());
        waitForUpdatedDecks();
    }

    private void editDeck() {
        switchTo(new CollectionDeckAppState(modeSelector.getMode(), deckSelector.getDeck()));
    }

    private void deleteDeck() {
        getModule(GameDataClientModule.class).deleteDeck(modeSelector.getMode(), deckSelector.getDeck());
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
                updateDecks();
            }
        });
    }

    private void updateDecks() {
        deckSelector.setDecks(modeSelector.getMode());
    }
}
