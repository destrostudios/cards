package com.destrostudios.cards.shared.network;

import com.destrostudios.cards.shared.network.messages.*;
import com.esotericsoftware.kryo.Kryo;

public class NetworkUtil {

    public static final int PORT = 33800;

    public static void setupSerializer(Kryo kryo) {
        kryo.setDefaultSerializer(JsonSerializer.class);
        kryo.register(GetOwnUserCardListsMessage.class);
        kryo.register(OwnUserCardListsMessage.class);
        kryo.register(StaticGameDataMessage.class);
        kryo.register(QueueMessage.class);
        kryo.register(UnqueueMessage.class);
    }
}
