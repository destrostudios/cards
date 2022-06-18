package com.destrostudios.cards.frontend.application.appstates.services.players;

import com.destrostudios.cardgui.BoardObjectModel;
import lombok.Getter;

public class PlayerModel extends BoardObjectModel {

    @Getter
    private boolean isActivePlayer;
    @Getter
    private String name;
    @Getter
    private int currentHealth;
    @Getter
    private int maxHealth;
    @Getter
    private int currentMana;
    @Getter
    private int maxMana;

    public void setActivePlayer(boolean isActivePlayer) {
        updateIfNotEquals(this.isActivePlayer, isActivePlayer, () -> this.isActivePlayer = isActivePlayer);
    }

    public void setName(String name) {
        updateIfNotEquals(this.name, name, () -> this.name = name);
    }

    public void setCurrentHealth(int currentHealth) {
        updateIfNotEquals(this.currentHealth, currentHealth, () -> this.currentHealth = currentHealth);
    }

    public void setMaxHealth(int maxHealth) {
        updateIfNotEquals(this.maxHealth, maxHealth, () -> this.maxHealth = maxHealth);
    }

    public void setCurrentMana(int currentMana) {
        updateIfNotEquals(this.currentMana, currentMana, () -> this.currentMana = currentMana);
    }

    public void setMaxMana(int maxMana) {
        updateIfNotEquals(this.maxMana, maxMana, () -> this.maxMana = maxMana);
    }
}
