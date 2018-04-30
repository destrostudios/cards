package com.destrostudios.cards.shared.network;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/**
 *
 * @author Philipp
 */
@Serializable
public class ActionNotificationMessage extends AbstractMessage {

    int action;
    int[] random;

    public ActionNotificationMessage() {
    }

    public ActionNotificationMessage(int action, int[] random) {
        this.action = action;
        this.random = random;
    }
}
