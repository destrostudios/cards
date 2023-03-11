package com.destrostudios.cards.frontend.application.appstates.menu;

import com.destrostudios.cards.frontend.application.gui.GuiUtil;
import com.destrostudios.cards.frontend.application.appstates.IngameAppState;
import com.destrostudios.cards.frontend.application.appstates.services.GameService;
import com.destrostudios.cards.frontend.application.modules.QueueClientModule;
import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.GameContext;
import com.destrostudios.gametools.network.client.modules.game.ClientGameData;
import com.destrostudios.gametools.network.client.modules.game.GameClientModule;
import com.destrostudios.gametools.network.client.modules.jwt.JwtClientModule;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.simsilica.lemur.Button;

import java.util.List;
import java.util.UUID;

public class PlayAppState extends MenuAppState {

    private ModeAndDeckSelector modeAndDeckSelector;
    private Button buttonQueueHuman;
    private Button buttonQueueBot;

    @Override
    public void initialize(AppStateManager stateManager, Application application){
        super.initialize(stateManager, application);
        addTitle("Play");
        modeAndDeckSelector = new ModeAndDeckSelector();
        addComponent(modeAndDeckSelector, 50, (height - GuiUtil.BUTTON_HEIGHT_DEFAULT));
        addButtons();
    }

    private void addButtons() {
        float margin = 50;
        float buttonWidth = 200;
        float buttonHeight = 100;
        float x = (width - margin - buttonWidth);
        float y = (margin + buttonHeight);
        buttonQueueBot = addQueueButton(false, x, y, buttonWidth, buttonHeight);
        x -= buttonWidth;
        buttonQueueHuman = addQueueButton(true, x, y, buttonWidth, buttonHeight);
        x = margin;
        Button buttonBack = addButton("Back", buttonWidth, buttonHeight, b -> switchTo(new MainMenuAppState()));
        buttonBack.setLocalTranslation(x, y, 0);
    }

    private Button addQueueButton(boolean againstHumanOrBot, float x, float y, float buttonWidth, float buttonHeight) {
        Button button = addButton(againstHumanOrBot ? "Queue vs Human" : "Start vs Bot", buttonWidth, buttonHeight, b -> {
            QueueClientModule queueModule = getModule(QueueClientModule.class);
            // Whatever, good enough for now
            if ("Unqueue vs Human".equals(b.getText())) {
                queueModule.unqueue();
                if (againstHumanOrBot) {
                    b.setText("Queue vs Human");
                }
            } else {
                queueModule.queue(againstHumanOrBot, modeAndDeckSelector.getDeck().getId());
                if (againstHumanOrBot) {
                    b.setText("Unqueue vs Human");
                }
            }
        });
        button.setLocalTranslation(x, y, 0);
        return button;
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
        boolean isDeckSelected = (modeAndDeckSelector.getDeck() != null);
        GuiUtil.setButtonEnabled(buttonQueueHuman, isDeckSelected);
        GuiUtil.setButtonEnabled(buttonQueueBot, isDeckSelected);
        switchToActiveGameIfExisting();
    }

    private void switchToActiveGameIfExisting() {
        GameClientModule<GameContext, Event> gameClientModule = getModule(GameClientModule.class);
        List<ClientGameData<GameContext, Event>> joinedGames = gameClientModule.getJoinedGames();
        if (joinedGames.size() > 0) {
            UUID gameUUID = joinedGames.get(0).getId();
            System.out.println("Joined game \"" + gameUUID + "\".");

            JwtClientModule jwtClientModule = getModule(JwtClientModule.class);
            GameService gameService = new GameService(jwtClientModule, gameClientModule, gameUUID);

            switchTo(new IngameAppState(gameService));
        }
    }
}