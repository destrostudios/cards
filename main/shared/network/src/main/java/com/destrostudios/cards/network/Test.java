package com.destrostudios.cards.network;

import com.destrostudios.cards.sandbox.Main;
import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.entities.collections.IntArrayList;
import com.destrostudios.cards.shared.events.ActionEvent;
import com.destrostudios.cards.shared.events.EventDispatcher;
import com.destrostudios.cards.shared.events.EventQueue;
import com.destrostudios.cards.shared.events.EventQueueImpl;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.game.StartGameEvent;
import com.destrostudios.cards.shared.rules.moves.MoveGenerator;
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
public class Test {

    static {
        Serializer.registerClass(ActionNotificationMessage.class);
        Serializer.registerClass(ActionRequestMessage.class);
    }

    public static void main(String[] args) throws IOException {
        int port = 7563;
        Server server = Network.createServer(port);

        TrackedRandom serverRandom = new TrackedRandom(new Random(5));
        EntityData serverData = new EntityData(new Random(5)::nextInt);
        EventDispatcher serverDispatcher = new EventDispatcher();
        EventQueue serverQueue = new EventQueueImpl(serverDispatcher::fire);
        Main.setListener(serverDispatcher, serverData, serverQueue, serverRandom::nextInt);
        Main.populateEntityData(serverData);
        MoveGenerator serverMoveGenerator = new MoveGenerator(serverData);

        server.addMessageListener((HostedConnection connection, Message message) -> {
            ActionRequestMessage msg = (ActionRequestMessage) message;
            IntArrayList history = serverRandom.getHistory();
            history.clear();
            serverQueue.action(serverMoveGenerator.generateAvailableMoves(serverData.entity(Components.TURN_PHASE)).get(msg.action));
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
        EntityData clientData = new EntityData(new Random(5)::nextInt);
        EventDispatcher clientDispatcher = new EventDispatcher();
        EventQueue clientQueue = new EventQueueImpl(clientDispatcher::fire);
        Main.setListener(clientDispatcher, clientData, clientQueue, x -> clientRandom.poll());
        Main.populateEntityData(clientData);
        MoveGenerator clientMoveGenerator = new MoveGenerator(clientData);

        Random rng = new Random(5);
        client.addMessageListener((Client s, Message message) -> {
            ActionNotificationMessage msg = (ActionNotificationMessage) message;
            for (int rngs : msg.random) {
                clientRandom.offer(rngs);
            }
            if (msg.action == -1) {
                clientQueue.action(new StartGameEvent());
            } else {
                clientQueue.action(clientMoveGenerator.generateAvailableMoves(clientData.entity(Components.TURN_PHASE)).get(msg.action));
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
            
            List<ActionEvent> moves = clientMoveGenerator.generateAvailableMoves(clientData.entity(Components.TURN_PHASE));
            client.send(new ActionRequestMessage(rng.nextInt(moves.size())));
        }, ActionNotificationMessage.class);

        client.start();
        System.out.println("client started");

        IntArrayList h = serverRandom.getHistory();
        h.clear();
        serverQueue.action(new StartGameEvent());
        int[] rngs = new int[h.size()];
        for (int i = 0; i < rngs.length; i++) {
            rngs[i] = h.get(i);
        }
        server.getConnection(0).send(new ActionNotificationMessage(-1, rngs));
    }
}
