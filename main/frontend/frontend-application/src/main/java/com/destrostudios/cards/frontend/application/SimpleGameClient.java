package com.destrostudios.cards.frontend.application;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.events.IterableEventQueue;
import com.destrostudios.cards.shared.events.IterableEventQueueImpl;
import com.destrostudios.cards.shared.events.QueuedIterableEventQueue;
import com.destrostudios.cards.shared.network.SerializerSetup;
import com.destrostudios.cards.shared.network.GameStateMessageConverter;
import com.destrostudios.cards.shared.network.messages.*;
import com.destrostudios.cards.shared.rules.GameContext;
import com.jme3.network.Client;
import com.jme3.network.Message;
import com.jme3.network.Network;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.List;
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
    private final List<FullGameStateListener> fullGameStateListeners = new LinkedList<>();

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
            fullGameStateListeners.forEach(fullGameStateListener -> fullGameStateListener.onFullGameStateUpdated(context.getData()));
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

    public void connect() {
        client.start();
        LOG.info("started client");
    }

    public void markAsReady() {
        client.send(new ClientReadyMessage());
        LOG.info("sent client ready");
    }

    public void requestAction(Event action) {
        client.send(new ActionRequestMessage(action));
        LOG.info("sent action request");
    }

    public GameContext<QueuedIterableEventQueue> getGame() {
        return context;
    }

    public void addFullGameStateListener(FullGameStateListener fullGameStateListener) {
        fullGameStateListeners.add(fullGameStateListener);
    }

    public interface FullGameStateListener {
        void onFullGameStateUpdated(EntityData entityData);
    }

}
