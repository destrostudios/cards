package com.destrostudios.cards.frontend.cardpainter.model;

import com.destrostudios.cardgui.BoardObjectModel;

import java.util.Objects;

public class Spell extends BoardObjectModel {

    private Cost cost;
    private String description;

    public void setCost(Cost cost) {
        updateIfNotEquals(this.cost, cost, () -> this.cost = cost);
    }

    public Cost getCost() {
        return cost;
    }

    public void setDescription(String description) {
        updateIfNotEquals(this.description, description, () -> this.description = description);
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Spell) {
            Spell spell = (Spell) object;
            return (Objects.equals(cost, spell.cost)
                && (Objects.equals(description, spell.description)));
        }
        return false;
    }
}
