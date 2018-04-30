package com.destrostudios.cards.network;

import com.destrostudios.cards.shared.entities.collections.IntArrayList;
import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameContext;
import com.destrostudios.cards.shared.rules.TestGameSetup;
import com.destrostudios.cards.shared.rules.game.StartGameEvent;
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
public class TestGameServer {

    private static final Logger LOG = LoggerFactory.getLogger(TestGameServer.class);

    private final Server server;
    private final TrackedRandom trackedRandom;
    private final GameContext context;
    private final List<ActionNotificationMessage> actionHistory = new ArrayList<>();

    public TestGameServer(int port) throws IOException {
        server = Network.createServer(port);
        trackedRandom = new TrackedRandom(new SecureRandom());
        context = new GameContext(trackedRandom::nextInt);
        new TestGameSetup().testSetup(context.getData());
        applyAction(-1);

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
            applyAction(message.action);
        }, ActionRequestMessage.class);
    }

    private void applyAction(int actionIndex) {
        IntArrayList history = trackedRandom.getHistory();
        history.clear();
        Event action = actionIndex == -1 ? new StartGameEvent() : context.getMoveGenerator().generateAvailableMoves(context.getData().entity(Components.TURN_PHASE)).get(actionIndex);
        context.getEvents().action(action);
        int[] rngs = new int[history.size()];
        for (int i = 0; i < rngs.length; i++) {
            rngs[i] = history.get(i);
        }
        ActionNotificationMessage message = new ActionNotificationMessage(actionIndex, rngs);
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
