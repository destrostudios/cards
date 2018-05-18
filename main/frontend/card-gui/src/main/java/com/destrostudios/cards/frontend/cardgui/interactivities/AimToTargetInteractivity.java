package com.destrostudios.cards.frontend.cardgui.interactivities;

import com.destrostudios.cards.frontend.cardgui.BoardObjectFilter;
import com.destrostudios.cards.frontend.cardgui.BoardObjectModel;
import com.destrostudios.cards.frontend.cardgui.Interactivity;
import com.destrostudios.cards.frontend.cardgui.targetarrow.TargetSnapMode;

/**
 *
 * @author Carl
 */
public abstract class AimToTargetInteractivity<ModelType extends BoardObjectModel> extends Interactivity<ModelType> implements BoardObjectFilter {
    
    public AimToTargetInteractivity(TargetSnapMode targetSnapMode) {
        super(Type.AIM);
        this.targetSnapMode = targetSnapMode;
    }
    private TargetSnapMode targetSnapMode;

    public TargetSnapMode getTargetSnapMode() {
        return targetSnapMode;
    }
}
