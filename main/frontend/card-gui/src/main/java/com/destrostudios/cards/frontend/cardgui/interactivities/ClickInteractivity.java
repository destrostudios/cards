package com.destrostudios.cards.frontend.cardgui.interactivities;

import com.destrostudios.cards.frontend.cardgui.BoardObjectModel;
import com.destrostudios.cards.frontend.cardgui.Interactivity;

/**
 *
 * @author Carl
 */
public abstract class ClickInteractivity<ModelType extends BoardObjectModel> extends Interactivity<ModelType> {

    public ClickInteractivity() {
        super(Type.CLICK);
    }
}
