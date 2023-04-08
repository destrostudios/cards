package com.destrostudios.cards.frontend.application.appstates.menu;

import com.destrostudios.cards.frontend.application.gui.GuiUtil;
import com.destrostudios.cards.frontend.application.appstates.IngameAppState;
import com.destrostudios.cards.frontend.application.appstates.services.GameService;
import com.destrostudios.cards.frontend.application.modules.GameDataClientModule;
import com.destrostudios.cards.frontend.application.modules.QueueClientModule;
import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.model.Queue;
import com.destrostudios.cards.shared.rules.GameConstants;
import com.destrostudios.cards.shared.rules.GameContext;
import com.destrostudios.gametools.network.client.modules.game.ClientGameData;
import com.destrostudios.gametools.network.client.modules.game.GameClientModule;
import com.destrostudios.gametools.network.client.modules.jwt.JwtClientModule;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.simsilica.lemur.Button;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class PlayAppState extends MenuAppState {

    private ModeAndDeckSelector modeAndDeckSelector;
    private List<Button> buttonsQueue;

    @Override
    public void initialize(AppStateManager stateManager, Application application){
        super.initialize(stateManager, application);
        addTitle("Play");
        modeAndDeckSelector = new ModeAndDeckSelector(false);
        addComponent(modeAndDeckSelector, 50, (height - GuiUtil.BUTTON_HEIGHT_DEFAULT));
        addButtons();
    }

    private void addButtons() {
        float margin = 50;
        float buttonWidth = 200;
        float buttonHeight = 100;
        float x = (width - margin - buttonWidth);
        float y = (margin + buttonHeight);
        buttonsQueue = new LinkedList<>();
        List<Queue> queues = getModule(GameDataClientModule.class).getQueues();
        for (Queue queue : queues) {
            Button buttonQueue = addQueueButton(queue, x, y, buttonWidth, buttonHeight);
            x -= buttonWidth;
            buttonsQueue.add(buttonQueue);
        }
        x = margin;
        Button buttonBack = addButton("Back", buttonWidth, buttonHeight, b -> switchTo(new MainMenuAppState()));
        buttonBack.setLocalTranslation(x, y, 0);
    }

    private Button addQueueButton(Queue queue, float x, float y, float buttonWidth, float buttonHeight) {
        boolean isUserQueue = queue.getName().equals(GameConstants.QUEUE_NAME_USER);
        Button button = addButton(isUserQueue ? "Queue vs Human" : "Start vs Bot", buttonWidth, buttonHeight, b -> {
            QueueClientModule queueModule = getModule(QueueClientModule.class);
            // Whatever, good enough for now
            if ("Unqueue vs Human".equals(b.getText())) {
                queueModule.unqueue();
                if (isUserQueue) {
                    b.setText("Queue vs Human");
                }
            } else {
                queueModule.queue(modeAndDeckSelector.getMode(), modeAndDeckSelector.getDeck(), queue);
                if (isUserQueue) {
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
        buttonsQueue.forEach(buttonQueue -> GuiUtil.setButtonEnabled(buttonQueue, isDeckSelected));
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