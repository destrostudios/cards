package com.destrostudios.cards.frontend.application.appstates.services.cardpainter.deckpower;

import com.destrostudios.cardgui.BoardObjectModel;
import com.destrostudios.cardgui.annotations.IsBoardObjectHovered;
import lombok.Getter;

@Getter
public class DeckPowerModel extends BoardObjectModel {

    private String name;
    private boolean selected;
    @IsBoardObjectHovered
    private boolean hovered;

    public void setName(String name) {
        updateIfNotEquals(this.name, name, () -> this.name = name);
    }

    public void setSelected(boolean selected) {
        updateIfNotEquals(this.selected, selected, () -> this.selected = selected);
    }
}
