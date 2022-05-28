package com.destrostudios.cards.frontend.application.appstates;

import com.destrostudios.cards.frontend.application.appstates.services.GameService;
import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.GameContext;
import com.destrostudios.cards.shared.rules.PlayerInfo;
import com.destrostudios.cards.shared.rules.StartGameInfo;
import com.destrostudios.gametools.network.client.modules.game.ClientGameData;
import com.destrostudios.gametools.network.client.modules.game.GameClientModule;
import com.destrostudios.gametools.network.client.modules.game.GameStartClientModule;
import com.destrostudios.gametools.network.client.modules.game.LobbyClientModule;
import com.destrostudios.gametools.network.client.modules.jwt.JwtClientModule;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.InputManager;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.scene.Node;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class WaitingForGameAppState extends MyBaseAppState implements ActionListener {

    private Node guiNode;
    private boolean isJoining;

    @Override
    public void initialize(AppStateManager stateManager, Application application){
        super.initialize(stateManager, application);
        System.out.println("Waiting for gamse...");

        guiNode = new Node();
        BitmapFont guiFont = mainApplication.getAssetManager().loadFont("Interface/Fonts/Default.fnt");
        BitmapText textTitle = new BitmapText(guiFont);
        textTitle.setSize(20);
        textTitle.setText("Waiting for games...\n\nClick to start a new game against a bot.");
        float margin = 30;
        float x = margin;
        float y = (mainApplication.getSettings().getHeight() - margin);
        textTitle.setLocalTranslation(x, y, 0);
        guiNode.attachChild(textTitle);

        mainApplication.getGuiNode().attachChild(guiNode);

        initInputListeners();
    }

    private void initInputListeners() {
        InputManager inputManager = mainApplication.getInputManager();
        inputManager.addMapping("mouseLeft", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addListener(this, "mouseLeft");
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
        } else if (!isJoining) {
            LobbyClientModule<StartGameInfo> lobbyClientModule = mainApplication.getToolsClient().getModule(LobbyClientModule.class);
            Map<UUID, StartGameInfo> games = lobbyClientModule.getListedGames();
            if (games.size() > 0) {
                UUID gameUUID = games.keySet().iterator().next();
                System.out.println("Joining game \"" + gameUUID + "\"...");
                gameClientModule.join(gameUUID);
                isJoining = true;
            }
        }
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (name.equals("mouseLeft") && isPressed) {
            GameStartClientModule<StartGameInfo> gameStartModule = mainApplication.getToolsClient().getModule(GameStartClientModule.class);
            JwtClientModule jwtClientModule = mainApplication.getToolsClient().getModule(JwtClientModule.class);
            gameStartModule.startNewGame(new StartGameInfo(
                "forest",
                new PlayerInfo(1, jwtClientModule.getOwnAuthentication().user.login, "custom"),
                new PlayerInfo(2, "Bot", "custom")
            ));
            isJoining = true;
        }
    }

    @Override
    public void cleanup() {
        super.cleanup();
        mainApplication.getGuiNode().detachChild(guiNode);
        mainApplication.getInputManager().deleteMapping("mouseLeft");
        mainApplication.getInputManager().removeListener(this);
    }
}