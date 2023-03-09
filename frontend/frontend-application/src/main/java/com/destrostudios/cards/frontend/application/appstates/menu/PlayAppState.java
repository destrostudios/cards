package com.destrostudios.cards.frontend.application.appstates.menu;

import com.destrostudios.cards.frontend.application.appstates.IngameAppState;
import com.destrostudios.cards.frontend.application.appstates.services.GameService;
import com.destrostudios.cards.frontend.application.modules.GameDataClientModule;
import com.destrostudios.cards.frontend.application.modules.QueueClientModule;
import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.model.UserCardList;
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

    @Override
    public void initialize(AppStateManager stateManager, Application application){
        super.initialize(stateManager, application);
        addQueueButton(true, 265);
        addQueueButton(false, 495);
    }

    private void addQueueButton(boolean againstHumanOrBot, float additionalX) {
        Button buttonQueue = addButton(againstHumanOrBot ? "Queue vs Human" : "Start vs Bot", 200, BUTTON_HEIGHT_DEFAULT, button -> {
            QueueClientModule queueModule = getModule(QueueClientModule.class);
            // Whatever, good enough for now
            if ("Unqueue vs Human".equals(button.getText())) {
                queueModule.unqueue();
                if (againstHumanOrBot) {
                    button.setText("Queue vs Human");
                }
            } else {
                // TODO
                UserCardList deck = getModule(GameDataClientModule.class).getUserCardLists().stream().filter(ucl -> !ucl.isLibrary()).findFirst().orElseThrow();
                queueModule.queue(againstHumanOrBot, deck.getId());
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