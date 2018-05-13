package com.destrostudios.cards.shared.network.messages;

import com.destrostudios.cards.shared.events.Event;
import com.jme3.network.AbstractMessage;

/**
 *
 * @author Philipp
 */
public class ActionNotificationMessage extends AbstractMessage {

    private Event action;
    private int[] randomHistory;

    ActionNotificationMessage() {
    }

    public ActionNotificationMessage(Event action, int[] randomHistory) {
        this.action = action;
        this.randomHistory = randomHistory;
    }

    public Event getAction() {
        return action;
    }

    public int[] getRandomHistory() {
        return randomHistory;
    }
}
