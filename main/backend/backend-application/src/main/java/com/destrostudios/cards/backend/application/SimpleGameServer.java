package com.destrostudios.cards.backend.application;

import com.destrostudios.cards.shared.network.SerializerSetup;
import com.destrostudios.cards.shared.network.messages.ActionNotificationMessage;
import com.destrostudios.cards.shared.network.messages.ActionRequestMessage;
import com.destrostudios.cards.shared.network.messages.ClientReadyMessage;
import com.jme3.network.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 *
 * @author Philipp
 */
public class SimpleGameServer {

    private static final Logger LOG = LoggerFactory.getLogger(SimpleGameServer.class);

    private final Server server;
    private RunningGame runningGame;

    public SimpleGameServer(int port) throws IOException {
        SerializerSetup.ensureInitialized();
        server = Network.createServer(port);
        server.addConnectionListener(new ConnectionListener() {

            @Override
            public void connectionAdded(Server server, HostedConnection hc) {
                LOG.info("Added connection {}", hc);
                runningGame.sendInitialGameStateMessage(hc);
            }

            @Override
            public void connectionRemoved(Server server, HostedConnection hc) {
                LOG.info("Removed connection {}", hc);
            }
        });

        server.addMessageListener((HostedConnection hc, Message msg) -> {
            runningGame.sendActionHistory(hc);
        }, ClientReadyMessage.class);

        server.addMessageListener((HostedConnection s, Message msg) -> {
            ActionRequestMessage message = (ActionRequestMessage) msg;
            ActionNotificationMessage actionNotificationMessage = runningGame.applyAction(message.getAction());
            server.broadcast(actionNotificationMessage);
        }, ActionRequestMessage.class);

        startNewGame();
    }

    public void startServer() {
        server.start();
        LOG.info("Server started");
    }

    public void startNewGame() {
        runningGame = new RunningGame();
        runningGame.start();
    }
}
