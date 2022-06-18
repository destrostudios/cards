package com.destrostudios.cards.frontend.application.appstates.services.players;

import com.destrostudios.cardgui.BoardObjectModel;
import lombok.Getter;

public class PlayerModel extends BoardObjectModel {

    @Getter
    private String name;
    @Getter
    private int health;

    public void setName(String name) {
        updateIfNotEquals(this.name, name, () -> this.name = name);
    }

    public void setHealth(int health) {
        updateIfNotEquals(this.health, health, () -> this.health = health);
    }
}
