package com.destrostudios.cards.frontend.cardgui.events;

import com.destrostudios.cards.frontend.cardgui.*;

/**
 *
 * @author Carl
 */
public class ModelUpdatedEvent<ModelType extends BoardObjectModel> extends GameEvent{

    public ModelUpdatedEvent(BoardObject boardObject) {
        this.boardObject = boardObject;
    }
    private BoardObject<ModelType> boardObject;

    @Override
    public void trigger(Board board) {
        boardObject.checkForVisualisationUpdate();
    }
}
