package com.destrostudios.cards.frontend.application;

import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.events.IterableEventQueue;
import com.destrostudios.cards.shared.events.IterableEventQueueImpl;
import com.destrostudios.cards.shared.events.QueuedIterableEventQueue;
import com.destrostudios.cards.shared.network.ActionNotificationMessage;
import com.destrostudios.cards.shared.network.ActionRequestMessage;
import com.destrostudios.cards.shared.network.SerializerSetup;
import com.destrostudios.cards.shared.rules.GameContext;
import com.destrostudios.cards.shared.network.FullGameStateMessage;
import com.destrostudios.cards.shared.network.GameStateMessageConverter;
import com.jme3.network.Client;
import com.jme3.network.Message;
import com.jme3.network.Network;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Queue;
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
    private final GameContext<QueuedIterableEventQueue> context;

    public SimpleGameClient(String host, int port) throws IOException {
        SerializerSetup.ensureInitialized();
        client = Network.connectToServer(host, port);
        GameContext.EventQueueProvider<QueuedIterableEventQueue> eventQueueProvider = (preDispatcher, dispatcher, postDispatcher) -> {
            IterableEventQueue iterableEventQueue = new IterableEventQueueImpl(preDispatcher, dispatcher, postDispatcher);
            return new QueuedIterableEventQueue(iterableEventQueue);
        };
        context = new GameContext<>(eventQueueProvider, x -> randomQueue.poll());
        GameStateMessageConverter gameStateMessageConverter = new GameStateMessageConverter(context.getData());

        client.addMessageListener((Client s, Message message) -> {
            FullGameStateMessage stateMessage = (FullGameStateMessage) message;
            gameStateMessageConverter.importStateMessage(stateMessage);
        }, FullGameStateMessage.class);

        client.addMessageListener((Client s, Message message) -> {
            ActionNotificationMessage actionMessage = (ActionNotificationMessage) message;
            for (int randomHistory : actionMessage.getRandomHistory()) {
                randomQueue.offer(randomHistory);
            }
            context.getEvents().fireActionEvent(actionMessage.getAction());
        }, ActionNotificationMessage.class);
        client.addErrorListener((Client source, Throwable t) -> {
            t.printStackTrace(System.err);
            LOG.error("client error", t);
        });
    }

    public void start() {
        client.start();
        LOG.info("started client");
    }

    public void requestAction(Event action) {
        client.send(new ActionRequestMessage(action));
        LOG.info("sent action request");
    }

    public GameContext<QueuedIterableEventQueue> getGame() {
        return context;
    }

}
