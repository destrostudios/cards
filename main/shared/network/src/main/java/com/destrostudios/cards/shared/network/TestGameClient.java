package com.destrostudios.cards.shared.network;

import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameContext;
import com.destrostudios.cards.shared.rules.TestGameSetup;
import com.destrostudios.cards.shared.rules.game.StartGameEvent;
import com.jme3.network.Client;
import com.jme3.network.Message;
import com.jme3.network.Network;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class TestGameClient {

    private static final Logger LOG = LoggerFactory.getLogger(TestGameClient.class);

    private final Client client;
    private final Queue<Integer> randomQueue = new ArrayDeque<>();
    private final GameContext context;
    private final List<Consumer<Event>> actionCallbacks = new ArrayList<>();

    public TestGameClient(String host, int port) throws IOException {
        client = Network.connectToServer(host, port);
        context = new GameContext(x -> randomQueue.poll());
        new TestGameSetup().testSetup(context.getData());

        client.addMessageListener((Client s, Message message) -> {
            ActionNotificationMessage msg = (ActionNotificationMessage) message;
            for (int rngs : msg.random) {
                randomQueue.offer(rngs);
            }
            Event action;
            if (msg.action == -1) {
                action = new StartGameEvent();
            } else {
                action = context.getMoveGenerator().generateAvailableMoves(context.getData().entity(Components.TURN_PHASE)).get(msg.action);
            }
            context.getEvents().action(action);
            for (Consumer<Event> actionCallback : actionCallbacks) {
                actionCallback.accept(action);
            }
        }, ActionNotificationMessage.class);
    }

    public void start() {
        client.start();
        LOG.info("started client");
    }

    public void requestAction(int actionIndex) {
        client.send(new ActionRequestMessage(actionIndex));
        LOG.info("sent action request");
    }

    public GameContext getGame() {
        return context;
    }

    public void addActionCallback(Consumer<Event> callback) {
        actionCallbacks.add(callback);
    }

}
