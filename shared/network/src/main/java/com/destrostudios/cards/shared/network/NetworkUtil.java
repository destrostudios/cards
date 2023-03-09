package com.destrostudios.cards.shared.network;

import com.destrostudios.cards.shared.network.messages.*;
import com.esotericsoftware.kryo.Kryo;

public class NetworkUtil {

    public static final int PORT = 33800;

    public static void setupSerializer(Kryo kryo) {
        kryo.setDefaultSerializer(JsonSerializer.class);
        kryo.register(CardPackResultMessage.class);
        kryo.register(CreateUserCardListMessage.class);
        kryo.register(DeleteUserCardListMessage.class);
        kryo.register(InitialGameDataMessage.class);
        kryo.register(OpenCardPackMessage.class);
        kryo.register(QueueMessage.class);
        kryo.register(UnqueueMessage.class);
        kryo.register(UpdateUserCardListMessage.class);
        kryo.register(UserCardListsMessage.class);
    }
}
