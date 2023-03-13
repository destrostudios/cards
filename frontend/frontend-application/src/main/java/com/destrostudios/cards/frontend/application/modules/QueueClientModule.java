package com.destrostudios.cards.frontend.application.modules;

import com.destrostudios.cards.shared.network.messages.QueueMessage;
import com.destrostudios.cards.shared.network.messages.UnqueueMessage;
import com.destrostudios.gametools.network.shared.modules.NetworkModule;
import com.esotericsoftware.kryonet.Connection;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class QueueClientModule extends NetworkModule {

    private Connection connection;

    public void queue(int modeId, int userModeDeckId, boolean againstHumanOrBot) {
        connection.sendTCP(new QueueMessage(modeId, userModeDeckId, againstHumanOrBot));
    }

    public void unqueue() {
        connection.sendTCP(new UnqueueMessage());
    }
}
