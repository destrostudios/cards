package com.destrostudios.cards.shared.network.messages;

import com.destrostudios.cards.shared.events.Event;
import com.jme3.network.AbstractMessage;

/**
 *
 * @author Philipp
 */
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
