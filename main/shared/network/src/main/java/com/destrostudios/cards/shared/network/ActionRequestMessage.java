package com.destrostudios.cards.shared.network;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/**
 *
 * @author Philipp
 */
@Serializable
public class ActionRequestMessage extends AbstractMessage {

    int action;

    public ActionRequestMessage() {
    }

    public ActionRequestMessage(int action) {
        this.action = action;
    }
}
