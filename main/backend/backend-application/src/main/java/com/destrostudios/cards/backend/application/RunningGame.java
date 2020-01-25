package com.destrostudios.cards.backend.application;

import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.network.FullGameState;
import com.destrostudios.cards.shared.network.GameStateMessageConverter;
import com.destrostudios.cards.shared.network.TrackedRandom;
import com.destrostudios.cards.shared.network.messages.ActionNotificationMessage;
import com.destrostudios.cards.shared.network.messages.FullGameStateMessage;
import com.destrostudios.cards.shared.rules.GameContext;
import com.destrostudios.cards.shared.rules.game.GameStartEvent;
import com.jme3.network.HostedConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class RunningGame {

    private static final Logger LOG = LoggerFactory.getLogger(RunningGame.class);

    private TrackedRandom trackedRandom;
    private GameContext context;
    private FullGameState initialGameState;
    private List<ActionNotificationMessage> actionHistory = new ArrayList<>();
    private TestGameSetup testGameSetup;

    public void start() {
        trackedRandom = new TrackedRandom(new SecureRandom());
        context = new GameContext(trackedRandom::nextInt);

        testGameSetup = new TestGameSetup(context.getData());
        testGameSetup.apply();

        GameStateMessageConverter gameStateMessageConverter = new GameStateMessageConverter(context.getData());
        initialGameState = gameStateMessageConverter.exportState();

        applyAction(new GameStartEvent());
    }

    public void sendInitialGameStateMessage(HostedConnection hostedConnection) {
        int playerEntity = testGameSetup.getPlayers()[hostedConnection.getId()];
        hostedConnection.send(new FullGameStateMessage(initialGameState, playerEntity));
    }

    public void sendActionHistory(HostedConnection hostedConnection) {
        for (ActionNotificationMessage message : actionHistory) {
            hostedConnection.send(message);
        }
        LOG.info("Sent action history to connection {}", hostedConnection);
    }

    public ActionNotificationMessage applyAction(Event action) {
        List<Integer> history = trackedRandom.getHistory();
        history.clear();
        context.getEvents().fire(action);
        while (context.getEvents().hasNextTriggeredHandler()) {
            context.getEvents().triggerNextHandler();
        }
        int[] randomHistory = new int[history.size()];
        for (int i = 0; i < randomHistory.length; i++) {
            randomHistory[i] = history.get(i);
        }
        ActionNotificationMessage message = new ActionNotificationMessage(action, randomHistory);
        actionHistory.add(message);
        LOG.debug("Applied {}, broadcasting...", action);
        return message;
    }
}
