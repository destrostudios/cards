package com.destrostudios.cards.shared.network.messages;

import com.destrostudios.cards.shared.network.FullGameState;
import com.jme3.network.AbstractMessage;

/**
 *
 * @author Philipp
 */
public class FullGameStateMessage extends AbstractMessage {

    private FullGameState fullGameState;
    private int playerEntity;

    private FullGameStateMessage() {

    }

    public FullGameStateMessage(FullGameState fullGameState, int playerEntity) {
        this.fullGameState = fullGameState;
        this.playerEntity = playerEntity;
    }

    public FullGameState getFullGameState() {
        return fullGameState;
    }

    public int getPlayerEntity() {
        return playerEntity;
    }
}
