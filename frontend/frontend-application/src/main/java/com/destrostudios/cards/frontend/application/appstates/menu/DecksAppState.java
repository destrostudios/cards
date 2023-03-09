package com.destrostudios.cards.frontend.application.appstates.menu;

import com.destrostudios.cards.frontend.application.appstates.LoadingAppState;
import com.destrostudios.cards.frontend.application.modules.GameDataClientModule;
import com.destrostudios.cards.shared.model.UserCardList;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.math.ColorRGBA;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Command;
import com.simsilica.lemur.Label;

public class DecksAppState extends MenuAppState {

    @Override
    public void initialize(AppStateManager stateManager, Application application) {
        super.initialize(stateManager, application);
        addTitle();
        addButton(0, "Create", b -> createDeck());
        addButton(1, "Edit", b -> editDeck());
        addButton(2, "Delete", b -> deleteDeck());
        addButton(3, "Back", b -> switchTo(new MainMenuAppState()));
    }

    private void addTitle() {
        Label label = new Label("Decks");
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

    private void createDeck() {
        getModule(GameDataClientModule.class).createUserCardList(1);
        mainApplication.getStateManager().attach(new LoadingAppState() {

            @Override
            protected boolean shouldClose() {
                return (getModule(GameDataClientModule.class).getUserCardLists() != null);
            }
        });
    }

    private void editDeck() {
        UserCardList deck = getModule(GameDataClientModule.class).getUserCardLists().stream().filter(ucl -> !ucl.isLibrary()).findFirst().orElseThrow();
        switchTo(new DeckAppState(deck));
    }

    private void deleteDeck() {
        UserCardList deck = getModule(GameDataClientModule.class).getUserCardLists().stream().filter(ucl -> !ucl.isLibrary()).findFirst().orElseThrow();
        getModule(GameDataClientModule.class).deleteUserCardList(deck.getId());
        mainApplication.getStateManager().attach(new LoadingAppState() {

            @Override
            protected boolean shouldClose() {
                return (getModule(GameDataClientModule.class).getUserCardLists() != null);
            }
        });
    }
}
