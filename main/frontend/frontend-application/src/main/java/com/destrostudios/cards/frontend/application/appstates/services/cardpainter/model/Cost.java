package com.destrostudios.cards.frontend.application.appstates.services.cardpainter.model;

import com.destrostudios.cardgui.BoardObjectModel;

import java.util.Objects;

public class Cost extends BoardObjectModel {

    private Integer manaCost;

    public void setManaCost(Integer manaCost) {
        updateIfNotEquals(this.manaCost, manaCost, () -> this.manaCost = manaCost);
    }

    public Integer getManaCost() {
        return manaCost;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Cost) {
            Cost cost = (Cost) object;
            return Objects.equals(manaCost, cost.manaCost);
        }
        return false;
    }
}
