package com.destrostudios.cards.frontend.application.modules;

import com.destrostudios.cards.shared.network.modules.QueueMessage;
import com.destrostudios.cards.shared.network.modules.QueueModule;
import com.destrostudios.cards.shared.network.modules.UnqueueMessage;
import com.esotericsoftware.kryonet.Connection;

import java.util.List;

public class QueueClientModule extends QueueModule {

    public QueueClientModule(Connection connection) {
        this.connection = connection;
    }
    private Connection connection;

    public void queue(boolean againstHumanOrBot, List<String> libraryTemplates) {
        connection.sendTCP(new QueueMessage(againstHumanOrBot, libraryTemplates));
    }

    public void unqueue() {
        connection.sendTCP(new UnqueueMessage());
    }
}
