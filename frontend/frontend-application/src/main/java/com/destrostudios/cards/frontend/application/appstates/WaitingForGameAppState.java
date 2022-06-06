package com.destrostudios.cards.frontend.application.appstates;

import com.destrostudios.cards.frontend.application.GuiUtil;
import com.destrostudios.cards.frontend.application.appstates.services.GameService;
import com.destrostudios.cards.frontend.application.modules.QueueClientModule;
import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.GameContext;
import com.destrostudios.gametools.network.client.modules.game.ClientGameData;
import com.destrostudios.gametools.network.client.modules.game.GameClientModule;
import com.destrostudios.gametools.network.client.modules.jwt.JwtClientModule;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.scene.Node;
import com.simsilica.lemur.Button;

import java.util.List;
import java.util.UUID;

public class WaitingForGameAppState extends MyBaseAppState {

    private Node guiNode;

    @Override
    public void initialize(AppStateManager stateManager, Application application){
        super.initialize(stateManager, application);
        initGui();
        stateManager.attach(new DeckAppState());
    }

    private void initGui() {
        guiNode = new Node();

        addQueueButton(true, 140);
        addQueueButton(false, 370);

        mainApplication.getGuiNode().attachChild(guiNode);
    }

    private void addQueueButton(boolean againstHumanOrBot, float additionalX) {
        Button buttonQueue = GuiUtil.addButton(guiNode, againstHumanOrBot ? "Queue vs Human" : "Start vs Bot", 200, GuiUtil.BUTTON_HEIGHT_DEFAULT, button -> {
            QueueClientModule queueModule = mainApplication.getToolsClient().getModule(QueueClientModule.class);
            // Whatever, good enough for now
            if ("Unqueue vs Human".equals(button.getText())) {
                queueModule.unqueue();
                if (againstHumanOrBot) {
                    button.setText("Queue vs Human");
                }
            } else {
                DeckAppState deckAppState = getAppState(DeckAppState.class);
                queueModule.queue(againstHumanOrBot, deckAppState.getLibraryTemplates());
                if (againstHumanOrBot) {
                    button.setText("Unqueue vs Human");
                }
            }
        });
        float margin = 20;
        float x = margin + additionalX;
        float y = (mainApplication.getContext().getSettings().getHeight() - margin);
        buttonQueue.setLocalTranslation(x, y, 0);
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
        GameClientModule<GameContext, Event> gameClientModule = mainApplication.getToolsClient().getModule(GameClientModule.class);
        List<ClientGameData<GameContext, Event>> joinedGames = gameClientModule.getJoinedGames();
        if (joinedGames.size() > 0) {
            UUID gameUUID = joinedGames.get(0).getId();
            System.out.println("Joined game \"" + gameUUID + "\".");

            JwtClientModule jwtClientModule = mainApplication.getToolsClient().getModule(JwtClientModule.class);
            GameService gameService = new GameService(jwtClientModule, gameClientModule, gameUUID);

            mainApplication.getStateManager().detach(this);
            mainApplication.getStateManager().attach(new IngameAppState(gameService));
        }
    }

    @Override
    public void cleanup() {
        super.cleanup();
        mainApplication.getStateManager().detach(getAppState(DeckAppState.class));
        mainApplication.getGuiNode().detachChild(guiNode);
    }
}