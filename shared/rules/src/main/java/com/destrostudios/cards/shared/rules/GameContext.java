package com.destrostudios.cards.shared.rules;

import com.destrostudios.cards.shared.entities.SimpleEntityData;
import com.destrostudios.cards.shared.events.*;
import com.destrostudios.cards.shared.rules.actions.*;
import com.destrostudios.cards.shared.rules.cards.CastSpellEvent;
import com.destrostudios.cards.shared.rules.cards.MulliganEvent;
import com.destrostudios.cards.shared.rules.game.GameStartEvent;
import com.destrostudios.cards.shared.rules.game.turn.EndTurnEvent;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import lombok.Getter;

public class GameContext {

    // Used in backend, to create a new bot worker thread needing an own instance, will be set up immediately via copyFrom
    public GameContext() {
        data = new SimpleEntityData(Components.ALL);
        events = new EventQueue<>(GameEventHandling.GLOBAL_INSTANCE);
    }

    // Used in backend, to create the actual first instance (a real game or the according "source" instance passed to bot)
    public GameContext(StartGameInfo startGameInfo, GameEventHandling eventHandling) {
        this.startGameInfo = startGameInfo;
        this.eventHandling = eventHandling;
        data = new SimpleEntityData(Components.ALL);
        events = new EventQueue<>(eventHandling);
    }

    // Used in frontend, to create a received context from network
    public GameContext(StartGameInfo startGameInfo, GameEventHandling eventHandling, SimpleEntityData data) {
        this.startGameInfo = startGameInfo;
        this.eventHandling = eventHandling;
        this.data = data;
        events = new EventQueue<>(eventHandling);
    }
    @Getter
    private StartGameInfo startGameInfo;
    @Getter
    private GameEventHandling eventHandling;
    @Getter
    private SimpleEntityData data;
    @Getter
    private EventQueue<GameContext> events;
    @Getter
    private NetworkRandom random;
    @Getter
    private Integer winner;

    public void copyFrom(GameContext source) {
        startGameInfo = source.startGameInfo;
        eventHandling = source.eventHandling;
        data.copyFrom(source.data);
        events.clear();
        winner = null;
    }

    public void fireAndResolveAction(Action action, NetworkRandom random) {
        fireAndResolveEvent(getActionEvent(action), random);
    }

    public void fireAction(Action action, NetworkRandom random) {
        fireEvent(getActionEvent(action), random);
    }

    private Event getActionEvent(Action action) {
        if (action instanceof CastSpellAction castSpellAction) {
            return new CastSpellEvent(castSpellAction.getSource(), castSpellAction.getSpell(), castSpellAction.getTargets());
        } else if (action instanceof EndTurnAction endTurnAction) {
            return new EndTurnEvent(endTurnAction.getPlayer());
        } else if (action instanceof MulliganAction mulliganAction) {
            return new MulliganEvent(mulliganAction.getCards());
        } else if (action instanceof GameStartAction) {
            return new GameStartEvent();
        }
        throw new IllegalArgumentException(action.toString());
    }

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
