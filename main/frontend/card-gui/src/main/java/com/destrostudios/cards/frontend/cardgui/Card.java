package com.destrostudios.cards.frontend.cardgui;

import com.destrostudios.cards.frontend.cardgui.transformations.*;

/**
 *
 * @author Carl
 */
public class Card extends TransformedBoardObject{

    private ZonePosition zonePosition = new ZonePosition();

    public ZonePosition getZonePosition() {
        return zonePosition;
    }

    @Override
    protected PositionTransformation getDefaultPositionTransformation() {
        return new CardInZonePositionTransformation(zonePosition);
    }

    @Override
    protected RotationTransformation getDefaultRotationTransformation() {
        return new CardInZoneRotationTransformation(zonePosition);
    }
}
