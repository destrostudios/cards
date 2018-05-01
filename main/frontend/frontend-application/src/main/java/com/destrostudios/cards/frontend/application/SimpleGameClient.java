package com.destrostudios.cards.frontend.application;

import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.network.ActionNotificationMessage;
import com.destrostudios.cards.shared.network.ActionRequestMessage;
import com.destrostudios.cards.shared.network.SerializerSetup;
import com.destrostudios.cards.shared.rules.GameContext;
import com.destrostudios.cards.shared.rules.TestGameSetup;
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
public class SimpleGameClient {

    private static final Logger LOG = LoggerFactory.getLogger(SimpleGameClient.class);

    private final Client client;
    private final Queue<Integer> randomQueue = new ArrayDeque<>();
    private final GameContext context;
    private final List<Consumer<Event>> actionCallbacks = new ArrayList<>();

    public SimpleGameClient(String host, int port) throws IOException {
        SerializerSetup.ensureInitialized();
        client = Network.connectToServer(host, port);
        context = new GameContext(x -> randomQueue.poll());
        new TestGameSetup().testSetup(context.getData());

        client.addMessageListener((Client s, Message message) -> {
            ActionNotificationMessage actionMessage = (ActionNotificationMessage) message;
            for (int randomHistory : actionMessage.getRandomHistory()) {
                randomQueue.offer(randomHistory);
            }
            context.getEvents().action(actionMessage.getAction());
            for (Consumer<Event> actionCallback : actionCallbacks) {
                actionCallback.accept(actionMessage.getAction());
            }
        }, ActionNotificationMessage.class);
    }

    public void start() {
        client.start();
        LOG.info("started client");
    }

    public void requestAction(Event action) {
        client.send(new ActionRequestMessage(action));
        LOG.info("sent action request");
    }

    public GameContext getGame() {
        return context;
    }

    public void addActionCallback(Consumer<Event> callback) {
        actionCallbacks.add(callback);
    }

}
