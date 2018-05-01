package com.destrostudios.cards.backend.application;

import com.destrostudios.cards.shared.entities.collections.IntArrayList;
import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.network.ActionNotificationMessage;
import com.destrostudios.cards.shared.network.ActionRequestMessage;
import com.destrostudios.cards.shared.network.SerializerSetup;
import com.destrostudios.cards.shared.network.TrackedRandom;
import com.destrostudios.cards.shared.rules.GameContext;
import com.destrostudios.cards.shared.rules.TestGameSetup;
import com.destrostudios.cards.shared.rules.game.GameStartEvent;
import com.jme3.network.ConnectionListener;
import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.Network;
import com.jme3.network.Server;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class SimpleGameServer {

    private static final Logger LOG = LoggerFactory.getLogger(SimpleGameServer.class);

    private final Server server;
    private final TrackedRandom trackedRandom;
    private final GameContext context;
    private final List<ActionNotificationMessage> actionHistory = new ArrayList<>();

    public SimpleGameServer(int port) throws IOException {
        SerializerSetup.ensureInitialized();
        server = Network.createServer(port);
        trackedRandom = new TrackedRandom(new SecureRandom());
        context = new GameContext(trackedRandom::nextInt);
        new TestGameSetup().testSetup(context.getData());
        applyAction(new GameStartEvent());

        server.addConnectionListener(new ConnectionListener() {
            @Override
            public void connectionAdded(Server server, HostedConnection hc) {
                for (ActionNotificationMessage message : actionHistory) {
                    hc.send(message);
                }
                LOG.info("added connection {}", hc);
            }

            @Override
            public void connectionRemoved(Server server, HostedConnection hc) {
                LOG.info("removed connection {}", hc);
            }
        });
        server.addMessageListener((HostedConnection s, Message msg) -> {
            ActionRequestMessage message = (ActionRequestMessage) msg;
            applyAction(message.getAction());
        }, ActionRequestMessage.class);
    }

    private void applyAction(Event action) {
        IntArrayList history = trackedRandom.getHistory();
        history.clear();
        context.getEvents().action(action);
        int[] randomHistory = new int[history.size()];
        for (int i = 0; i < randomHistory.length; i++) {
            randomHistory[i] = history.get(i);
        }
        ActionNotificationMessage message = new ActionNotificationMessage(action, randomHistory);
        actionHistory.add(message);
        LOG.debug("applied {}, broadcasting...", action);
        server.broadcast(message);
    }

    public void start() {
        server.start();
        LOG.info("server started");
    }

    public GameContext getGame() {
        return context;
    }
}
