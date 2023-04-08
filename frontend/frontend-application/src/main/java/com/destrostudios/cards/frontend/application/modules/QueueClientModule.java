package com.destrostudios.cards.frontend.application.modules;

import com.destrostudios.cards.shared.model.Deck;
import com.destrostudios.cards.shared.model.Mode;
import com.destrostudios.cards.shared.model.Queue;
import com.destrostudios.cards.shared.network.messages.QueueMessage;
import com.destrostudios.cards.shared.network.messages.UnqueueMessage;
import com.destrostudios.gametools.network.shared.modules.NetworkModule;
import com.esotericsoftware.kryonet.Connection;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class QueueClientModule extends NetworkModule {

    private Connection connection;

    public void queue(Mode mode, Deck deck, Queue queue) {
        connection.sendTCP(new QueueMessage(mode.getId(), deck.getId(), queue.getId()));
    }

    public void unqueue() {
        connection.sendTCP(new UnqueueMessage());
    }
}
