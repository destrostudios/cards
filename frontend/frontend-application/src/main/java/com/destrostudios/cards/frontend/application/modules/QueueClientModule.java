package com.destrostudios.cards.frontend.application.modules;

import com.destrostudios.cards.shared.network.messages.QueueMessage;
import com.destrostudios.cards.shared.network.messages.UnqueueMessage;
import com.destrostudios.gametools.network.shared.modules.NetworkModule;
import com.esotericsoftware.kryonet.Connection;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class QueueClientModule extends NetworkModule {

    private Connection connection;

    public void queue(boolean againstHumanOrBot, int userCardListId) {
        connection.sendTCP(new QueueMessage(againstHumanOrBot, userCardListId));
    }

    public void unqueue() {
        connection.sendTCP(new UnqueueMessage());
    }
}
