package com.destrostudios.cards.frontend.cardpainter.model;

import com.destrostudios.cards.frontend.cardgui.BoardObjectModel;

import java.util.Objects;

public class Cost extends BoardObjectModel {

    private boolean isTap;
    private ManaCost manaCost;

    public void setTap(boolean isTap) {
        updateIfNotEquals(this.isTap, isTap, () -> this.isTap = isTap);
    }

    public boolean isTap() {
        return isTap;
    }

    public void setManaCost(ManaCost manaCost) {
        updateIfNotEquals(this.manaCost, manaCost, () -> this.manaCost = manaCost);
    }

    public ManaCost getManaCost() {
        return manaCost;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Cost) {
            Cost cost = (Cost) object;
            return (Objects.equals(isTap, cost.isTap)
                && (Objects.equals(manaCost, cost.manaCost)));
        }
        return false;
    }
}
