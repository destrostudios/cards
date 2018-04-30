package com.destrostudios.cards.network;

import com.destrostudios.cards.shared.entities.collections.IntArrayList;
import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameContext;
import com.destrostudios.cards.shared.rules.TestGameSetup;
import com.destrostudios.cards.shared.rules.game.StartGameEvent;
import com.jme3.network.Client;
import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.Network;
import com.jme3.network.Server;
import com.jme3.network.serializing.Serializer;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;
import java.util.Random;

/**
 *
 * @author Philipp
 */
public class NetworkTest {

    static {
        System.setProperty("org.slf4j.simpleLogger.logFile", "System.out");
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "debug");

        Serializer.registerClass(ActionNotificationMessage.class);
        Serializer.registerClass(ActionRequestMessage.class);
    }

    public static void main(String[] args) throws IOException {
        int port = 7563;
        Server server = Network.createServer(port);

        TrackedRandom serverRandom = new TrackedRandom(new Random(5));
        GameContext serverContext = new GameContext(serverRandom::nextInt);
        new TestGameSetup().testSetup(serverContext.getData());

        server.addMessageListener((HostedConnection connection, Message message) -> {
            ActionRequestMessage msg = (ActionRequestMessage) message;
            IntArrayList history = serverRandom.getHistory();
            history.clear();
            serverContext.getEvents().action(serverContext.getMoveGenerator().generateAvailableMoves(serverContext.getData().entity(Components.TURN_PHASE)).get(msg.action));
            int[] rngs = new int[history.size()];
            for (int i = 0; i < rngs.length; i++) {
                rngs[i] = history.get(i);
            }
            connection.send(new ActionNotificationMessage(msg.action, rngs));
        }, ActionRequestMessage.class);

        server.start();
        System.out.println("server started");

        Client client = Network.connectToServer("localhost", port);

        Queue<Integer> clientRandom = new ArrayDeque<>();
        GameContext clientContext = new GameContext(x -> clientRandom.poll());
        new TestGameSetup().testSetup(clientContext.getData());

        Random rng = new Random(5);
        client.addMessageListener((Client s, Message message) -> {
            ActionNotificationMessage msg = (ActionNotificationMessage) message;
            for (int rngs : msg.random) {
                clientRandom.offer(rngs);
            }
            if (msg.action == -1) {
                clientContext.getEvents().action(new StartGameEvent());
            } else {
                clientContext.getEvents().action(clientContext.getMoveGenerator().generateAvailableMoves(clientContext.getData().entity(Components.TURN_PHASE)).get(msg.action));
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }

            List<Event> moves = clientContext.getMoveGenerator().generateAvailableMoves(clientContext.getData().entity(Components.TURN_PHASE));
            client.send(new ActionRequestMessage(rng.nextInt(moves.size())));
        }, ActionNotificationMessage.class);

        client.start();
        System.out.println("client started");

        IntArrayList h = serverRandom.getHistory();
        h.clear();
        serverContext.getEvents().action(new StartGameEvent());
        int[] rngs = new int[h.size()];
        for (int i = 0; i < rngs.length; i++) {
            rngs[i] = h.get(i);
        }
        server.getConnection(0).send(new ActionNotificationMessage(-1, rngs));
    }
}
