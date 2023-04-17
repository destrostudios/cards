package com.destrostudios.cards.shared.rules;

import com.destrostudios.cards.shared.entities.SimpleEntityData;
import com.destrostudios.cards.shared.events.*;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import lombok.Getter;

public class GameContext {

    public GameContext(GameContext gameContext) {
        this(gameContext.startGameInfo, new SimpleEntityData(gameContext.data), gameContext.eventHandling);
    }

    public GameContext(StartGameInfo startGameInfo, SimpleEntityData data, GameEventHandling eventHandling) {
        this.startGameInfo = startGameInfo;
        this.data = data;
        this.eventHandling = eventHandling;
        events = new EventQueue<>(eventHandling);
    }
    @Getter
    private StartGameInfo startGameInfo;
    @Getter
    private SimpleEntityData data;
    @Getter
    private GameEventHandling eventHandling;
    @Getter
    private EventQueue<GameContext> events;
    @Getter
    private NetworkRandom random;
    @Getter
    private Integer winner;

    public void fireAndResolveEvent(Event event, NetworkRandom random) {
        fireEvent(event, random);
        resolveEvents();
    }

    public void fireEvent(Event event, NetworkRandom random) {
        this.random = random;
        events.fire(event);
    }

    public void resolveEvents() {
        while (events.hasPendingEventHandler()) {
            events.triggerNextEventHandler(this);
        }
    }

    public void onGameOver(int winner) {
        this.winner = winner;
    }

    public boolean isGameOver() {
        return (winner != null);
    }

    public int getUserId(int player) {
        String login = data.getComponent(player, Components.NAME);
        for (PlayerInfo playerInfo : startGameInfo.getPlayers()) {
            if (playerInfo.getLogin().equals(login)) {
                return playerInfo.getId();
            }
        }
        throw new RuntimeException("User with login '" + login + "' not found.");
    }
}
