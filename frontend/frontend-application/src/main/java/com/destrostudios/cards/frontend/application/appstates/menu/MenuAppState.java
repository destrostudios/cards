package com.destrostudios.cards.frontend.application.appstates.menu;

import com.destrostudios.cards.frontend.application.appstates.IngameAppState;
import com.destrostudios.cards.frontend.application.appstates.services.GameService;
import com.destrostudios.cards.frontend.application.gui.GuiComponent;
import com.destrostudios.cards.frontend.application.gui.GuiUtil;
import com.destrostudios.cards.frontend.application.appstates.MyBaseAppState;
import com.destrostudios.cards.shared.rules.GameContext;
import com.destrostudios.cards.shared.rules.actions.Action;
import com.destrostudios.gametools.network.client.modules.game.ClientGameData;
import com.destrostudios.gametools.network.client.modules.game.GameClientModule;
import com.destrostudios.gametools.network.client.modules.jwt.JwtClientModule;
import com.jme3.app.Application;
import com.jme3.app.state.AppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Command;
import com.simsilica.lemur.Label;

import java.util.List;
import java.util.UUID;

public class MenuAppState extends MyBaseAppState {

    public MenuAppState() {
        this.guiNode = new Node();
    }
    protected int width;
    protected int height;
    protected Node guiNode;

    @Override
    public void initialize(AppStateManager stateManager, Application application){
        super.initialize(stateManager, application);
        mainApplication.getGuiNode().attachChild(guiNode);
        width = mainApplication.getContext().getSettings().getWidth();
        height = mainApplication.getContext().getSettings().getHeight();
    }

    protected void addTitle(String title) {
        Label label = new Label(title);
        label.setFontSize(48);
        float x = ((width / 2f) - (label.getPreferredSize().getX() / 2));
        float y = (height - 50);
        label.setLocalTranslation(x, y, 0);
        label.setColor(ColorRGBA.White);
        guiNode.attachChild(label);
    }

    protected Button addButton(String text, float width, float height, Command<Button> command) {
        Button button = GuiUtil.createButton(text, width, height, command);
        guiNode.attachChild(button);
        return button;
    }

    protected void addComponent(GuiComponent guiComponent, float x, float y) {
        guiComponent.init(mainApplication);
        guiComponent.getGuiNode().setLocalTranslation(x, y, 0);
        guiNode.attachChild(guiComponent.getGuiNode());
    }

    protected void setTopDownCamera() {
        mainApplication.getCamera().setLocation(new Vector3f(0, 15, 0));
        mainApplication.getCamera().lookAtDirection(new Vector3f(0, -1, 0).normalizeLocal(), Vector3f.UNIT_Z.mult(-1));
    }

    protected void switchTo(AppState appState) {
        mainApplication.getStateManager().detach(this);
        mainApplication.getStateManager().attach(appState);
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
        switchToActiveGameIfExisting();
    }

    private void switchToActiveGameIfExisting() {
        GameClientModule<GameContext, Action> gameClientModule = getModule(GameClientModule.class);
        List<ClientGameData<GameContext, Action>> joinedGames = gameClientModule.getJoinedGames();
        if (joinedGames.size() > 0) {
            UUID gameUUID = joinedGames.get(0).getId();
            System.out.println("Joined game \"" + gameUUID + "\".");

            JwtClientModule jwtClientModule = getModule(JwtClientModule.class);
            GameService gameService = new GameService(jwtClientModule, gameClientModule, gameUUID);

            switchTo(new IngameAppState(gameService));
        }
    }

    @Override
    public void cleanup() {
        super.cleanup();
        mainApplication.getGuiNode().detachChild(guiNode);
    }
}