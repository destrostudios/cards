package com.destrostudios.cards.shared.network.modules;

import com.destrostudios.gametools.network.shared.modules.NetworkModule;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.util.LinkedList;

public class QueueModule extends NetworkModule {

    @Override
    public void initialize(Kryo kryo) {
        super.initialize(kryo);
        kryo.register(QueueMessage.class, new Serializer<QueueMessage>() {

            @Override
            public void write(Kryo kryo, Output output, QueueMessage queueMessage) {
                output.writeBoolean(queueMessage.isAgainstHumanOrBot());
                output.writeInt(queueMessage.getLibraryTemplates().size());
                for (String template : queueMessage.getLibraryTemplates()) {
                    output.writeString(template);
                }
            }

            @Override
            public QueueMessage read(Kryo kryo, Input input, Class<QueueMessage> type) {
                LinkedList<String> libraryTemplates = new LinkedList<>();
                boolean againstHumanOrBot = input.readBoolean();
                int librarySize = input.readInt();
                for (int i = 0; i < librarySize; i++) {
                    libraryTemplates.add(input.readString());
                }
                return new QueueMessage(againstHumanOrBot, libraryTemplates);
            }
        });
        kryo.register(UnqueueMessage.class);
    }
}
