package com.destrostudios.cards.frontend.cardguitest;

import com.destrostudios.cards.frontend.cardgui.BoardObjectModel;
import com.destrostudios.cards.frontend.cardguitest.game.MyCard;

public class MyCardModel extends BoardObjectModel {

    private MyCard.Color color;
    private String name;
    private boolean isDamaged;

    public void setColor(MyCard.Color color) {
        updateIfNotEquals(this.color, color, () -> this.color = color);
    }

    public MyCard.Color getColor() {
        return color;
    }

    public void setName(String name) {
        updateIfNotEquals(this.name, name, () -> this.name = name);
    }

    public String getName() {
        return name;
    }

    public void setDamaged(boolean isDamaged) {
        updateIfNotEquals(this.isDamaged, isDamaged, () -> this.isDamaged = isDamaged);
    }

    public boolean isDamaged() {
        return isDamaged;
    }
}
