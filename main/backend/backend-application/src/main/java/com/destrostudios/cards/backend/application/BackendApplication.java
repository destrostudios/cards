package com.destrostudios.cards.backend.application;

import com.destrostudios.cards.shared.network.TrackedRandom;
import com.destrostudios.cards.shared.rules.GameContext;
import com.destrostudios.cards.shared.rules.TestGameSetup;
import com.jme3.network.*;

import java.io.IOException;
import java.util.Random;

public class BackendApplication {

    public static void main(String[] args) {
        new BackendApplication(7563);
    }

    public BackendApplication(int port) {
        try {
            System.out.println("Starting server...");
            Server server = Network.createServer(port);

            TrackedRandom serverRandom = new TrackedRandom(new Random(5));
            GameContext serverContext = new GameContext(serverRandom::nextInt);
            new TestGameSetup().testSetup(serverContext.getData());

            server.addConnectionListener(new ConnectionListener() {

                @Override
                public void connectionAdded(Server server, HostedConnection hostedConnection) {
                    System.out.println("#" + hostedConnection.getId() + " connected.");
                }

                @Override
                public void connectionRemoved(Server server, HostedConnection hostedConnection) {
                    System.out.println("#" + hostedConnection.getId() + " disconnected.");
                }
            });
            server.addMessageListener((HostedConnection connection, Message message) -> {

            }, Message.class);

            server.start();
            System.out.println("Server started.");

            new Thread(() -> {
                while (true) {
                    try {
                        Thread.sleep(60000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }).start();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
