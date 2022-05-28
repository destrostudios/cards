package com.destrostudios.cards.frontend.application.appstates.services;

import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameContext;
import com.destrostudios.gametools.network.client.modules.game.GameClientModule;
import com.destrostudios.gametools.network.client.modules.jwt.JwtClientModule;
import lombok.Getter;

import java.util.UUID;

public class GameService {

    public GameService(JwtClientModule jwtClientModule, GameClientModule<GameContext, Event> gameClientModule, UUID gameUUID) {
        this.gameClientModule = gameClientModule;
        this.gameUUID = gameUUID;
        gameContext = gameClientModule.getJoinedGame(gameUUID).getState();
        playerEntity = gameContext.getData().query(Components.NEXT_PLAYER).list(entity -> gameContext.getData().getComponent(entity, Components.NAME).equals(jwtClientModule.getOwnAuthentication().user.login)).get(0);
    }
    private GameClientModule<GameContext, Event> gameClientModule;
    private UUID gameUUID;
    @Getter
    private GameContext gameContext;
    @Getter
    private int playerEntity;

    public void applyNextActionIfExisting() {
        gameClientModule.applyNextAction(gameUUID);
    }

    public void sendAction(Event action) {
        gameClientModule.sendAction(gameUUID, action);
    }

    public void onGameOver() {
        gameClientModule.removeJoinedGame(gameUUID);
    }
}
