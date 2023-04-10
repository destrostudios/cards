package com.destrostudios.cards.frontend.application.appstates.services;

import com.destrostudios.cards.shared.entities.templates.Util;
import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameContext;
import com.destrostudios.cards.shared.rules.cards.MulliganEvent;
import com.destrostudios.gametools.network.client.modules.game.GameClientModule;
import com.destrostudios.gametools.network.client.modules.jwt.JwtClientModule;
import lombok.Getter;

import java.util.HashSet;
import java.util.UUID;

public class GameService {

    public GameService(JwtClientModule jwtClientModule, GameClientModule<GameContext, Event> gameClientModule, UUID gameUUID) {
        this.gameClientModule = gameClientModule;
        this.gameUUID = gameUUID;
        gameContext = gameClientModule.getJoinedGame(gameUUID).getState();
        playerEntity = gameContext.getData().query(Components.NEXT_PLAYER).list(entity -> gameContext.getData().getComponent(entity, Components.NAME).equals(jwtClientModule.getOwnAuthentication().user.login)).get(0);
        mulliganCards = new HashSet<>();
    }
    private GameClientModule<GameContext, Event> gameClientModule;
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
        sendAction(new MulliganEvent(Util.convertToArray_Integer(mulliganCards)));
    }

    public void sendAction(Event action) {
        gameClientModule.sendAction(gameUUID, action);
    }

    public void onGameOver() {
        gameClientModule.removeJoinedGame(gameUUID);
    }
}
