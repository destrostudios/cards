package com.destrostudios.cards.frontend.cardpainter.model;

import com.destrostudios.cards.frontend.cardgui.BoardObjectModel;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class ManaCost extends BoardObjectModel {

    public ManaCost(BoardObjectModel parentModel) {
        super(parentModel);
    }
    private Map<Color, Integer> manaCost = new LinkedHashMap<>();

    public void set(Color color, int cost) {
        updateIfNotEquals(manaCost.get(color), cost, () -> manaCost.put(color, cost));
    }

    public Integer get(Color color) {
        return manaCost.get(color);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof ManaCost) {
            ManaCost cost = (ManaCost) object;
            return Objects.equals(manaCost, cost.manaCost);
        }
        return false;
    }
}
