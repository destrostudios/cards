package com.destrostudios.cards.shared.network;

import com.destrostudios.cards.shared.network.messages.OwnUserCardListsMessage;
import com.destrostudios.cards.shared.network.messages.GameDataMessage;
import com.destrostudios.cards.shared.network.messages.QueueMessage;
import com.destrostudios.cards.shared.network.messages.UnqueueMessage;
import com.esotericsoftware.kryo.Kryo;

public class NetworkUtil {

    public static final int PORT = 33800;

    public static void setupSerializer(Kryo kryo) {
        kryo.setDefaultSerializer(JsonSerializer.class);
        kryo.register(OwnUserCardListsMessage.class);
        kryo.register(GameDataMessage.class);
        kryo.register(QueueMessage.class);
        kryo.register(UnqueueMessage.class);
    }
}
