package com.destrostudios.cards.frontend.cardgui;

import com.destrostudios.cards.frontend.cardgui.transformations.*;
/**
 *
 * @author Carl
 */
public class Card<ModelType extends BoardObjectModel> extends TransformedBoardObject<ModelType>{

    public Card(ModelType model) {
        position().setDefaultTransformationProvider(() -> new CardInZonePositionTransformation(zonePosition));
        rotation().setDefaultTransformationProvider(() -> new CardInZoneRotationTransformation(zonePosition));
        setModel(model);
    }
    private ZonePosition zonePosition = new ZonePosition();

    public ZonePosition getZonePosition() {
        return zonePosition;
    }
}
