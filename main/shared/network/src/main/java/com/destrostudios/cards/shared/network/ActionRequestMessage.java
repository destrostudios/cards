package com.destrostudios.cards.shared.network;

import com.destrostudios.cards.shared.events.Event;
import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/**
 *
 * @author Philipp
 */
@Serializable
public class ActionRequestMessage extends AbstractMessage {

    private Event action;

    public ActionRequestMessage() {
    }

    public ActionRequestMessage(Event action) {
        this.action = action;
    }

    public Event getAction() {
        return action;
    }
}
