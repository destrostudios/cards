package com.destrostudios.cards.shared.network;

import com.destrostudios.cards.shared.network.messages.*;
import com.esotericsoftware.kryo.Kryo;

public class NetworkUtil {

    public static final int PORT = 33800;

    public static void setupSerializer(Kryo kryo) {
        kryo.setDefaultSerializer(JsonSerializer.class);
        kryo.register(CreateDeckMessage.class);
        kryo.register(DeleteDeckMessage.class);
        kryo.register(GetUserMessage.class);
        kryo.register(InitialGameDataMessage.class);
        kryo.register(OpenPackMessage.class);
        kryo.register(PackResultMessage.class);
        kryo.register(QueueMessage.class);
        kryo.register(UnqueueMessage.class);
        kryo.register(UpdateDeckMessage.class);
        kryo.register(UserMessage.class);
    }
}
