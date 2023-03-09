package com.destrostudios.cards.frontend.application.modules;

import com.destrostudios.cards.shared.network.messages.QueueMessage;
import com.destrostudios.cards.shared.network.messages.UnqueueMessage;
import com.destrostudios.gametools.network.shared.modules.NetworkModule;
import com.esotericsoftware.kryonet.Connection;

public class QueueClientModule extends NetworkModule {

    public QueueClientModule(Connection connection) {
        this.connection = connection;
    }
    private Connection connection;

    public void queue(boolean againstHumanOrBot, int cardListId) {
        connection.sendTCP(new QueueMessage(againstHumanOrBot, cardListId));
    }

    public void unqueue() {
        connection.sendTCP(new UnqueueMessage());
    }
}
