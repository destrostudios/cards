package com.destrostudios.cards.shared.network;

import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.Components;
import com.jme3.network.serializing.Serializer;
import java.io.IOException;
import java.util.List;
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
        int port = 7564;

        TestGameServer server = new TestGameServer(port);
        server.start();

        TestGameClient client = new TestGameClient("localhost", port);
        Random random = new Random(17);
        client.addActionCallback(lastAction -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }

            List<Event> moves = client.getGame().getMoveGenerator().generateAvailableMoves(client.getGame().getData().entity(Components.TURN_PHASE));
            client.requestAction(random.nextInt(moves.size()));
        });
        client.start();
    }
}
