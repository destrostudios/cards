package com.destrostudios.cards.frontend.application.appstates.services;

import com.destrostudios.cards.shared.entities.templates.Util;
import com.destrostudios.cards.shared.rules.actions.Action;
import com.destrostudios.cards.shared.rules.actions.MulliganAction;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameContext;
import com.destrostudios.gametools.network.client.modules.game.GameClientModule;
import com.destrostudios.gametools.network.client.modules.jwt.JwtClientModule;
import lombok.Getter;

import java.util.HashSet;
import java.util.UUID;

public class GameService {

    public GameService(JwtClientModule jwtClientModule, GameClientModule<GameContext, Action> gameClientModule, UUID gameUUID) {
        this.gameClientModule = gameClientModule;
        this.gameUUID = gameUUID;
        gameContext = gameClientModule.getJoinedGame(gameUUID).getState();
        playerEntity = gameContext.getData().list(Components.NEXT_PLAYER, player -> gameContext.getData().getComponent(player, Components.NAME).equals(jwtClientModule.getOwnAuthentication().user.login)).get(0);
        mulliganCards = new HashSet<>();
    }
    private GameClientModule<GameContext, Action> gameClientModule;
    private UUID gameUUID;
    @Getter
    private GameContext gameContext;
    @Getter
    private int playerEntity;
    private HashSet<Integer> mulliganCards;

    public void applyNextActionIfExisting() {
        gameClientModule.applyNextAction(gameUUID);
    }

    public boolean toggleMulliganCard(int card) {
        if (mulliganCards.contains(card)) {
            mulliganCards.remove(card);
            return false;
        } else {
            mulliganCards.add(card);
            return true;
        }
    }

    public void sendMulliganAction() {
        sendAction(new MulliganAction(Util.convertToArray_Integer(mulliganCards)));
    }

    public void sendAction(Action action) {
        gameClientModule.sendAction(gameUUID, action);
    }

    public void onGameOver() {
        gameClientModule.removeJoinedGame(gameUUID);
    }
}
