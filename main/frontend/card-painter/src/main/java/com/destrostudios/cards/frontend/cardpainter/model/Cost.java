package com.destrostudios.cards.frontend.cardpainter.model;

import com.destrostudios.cards.frontend.cardgui.BoardObjectModel;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class Cost extends BoardObjectModel {

    public Cost(BoardObjectModel parentModel) {
        super(parentModel);
    }
    private boolean isTap;
    private Map<Color, Integer> manaCost = new LinkedHashMap<>();

    public void setTap(boolean isTap) {
        updateIfNotEquals(this.isTap, isTap, () -> this.isTap = isTap);
    }

    public boolean isTap() {
        return isTap;
    }

    public void setManaCost(Color color, int cost) {
        updateIfNotEquals(manaCost.get(color), cost, () -> manaCost.put(color, cost));
    }

    public Map<Color, Integer> getManaCost() {
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
