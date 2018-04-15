package com.destrostudios.cards.frontend.cardgui.events;

import com.destrostudios.cards.frontend.cardgui.*;

/**
 *
 * @author Carl
 */
public class SetPropertyEvent extends GameEvent{

    public SetPropertyEvent(BoardObject boardObject, String propertyName, String propertyValue) {
        this.boardObject = boardObject;
        this.propertyName = propertyName;
        this.propertyValue = propertyValue;
    }
    private BoardObject boardObject;
    private String propertyName;
    private String propertyValue;

    @Override
    public void trigger(Board board) {
        boardObject.setProperty(propertyName, propertyValue);
    }
}
