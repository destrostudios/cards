package com.destrostudios.cards.frontend.application;

import com.destrostudios.cards.frontend.cardgui.CardZone;

/**
 *
 * @author Carl
 */
public class PlayerZones {

    public PlayerZones(CardZone deckZone, CardZone handZone, CardZone landZone, CardZone creatureZone, CardZone enchantmentZone, CardZone graveyardZone) {
        this.deckZone = deckZone;
        this.handZone = handZone;
        this.landZone = landZone;
        this.creatureZone = creatureZone;
        this.enchantmentZone = enchantmentZone;
        this.graveyardZone = graveyardZone;
    }
    private CardZone deckZone;
    private CardZone handZone;
    private CardZone landZone;
    private CardZone creatureZone;
    private CardZone enchantmentZone;
    private CardZone graveyardZone;

    public CardZone getDeckZone() {
        return deckZone;
    }

    public CardZone getHandZone() {
        return handZone;
    }

    public CardZone getLandZone() {
        return landZone;
    }

    public CardZone getCreatureZone() {
        return creatureZone;
    }

    public CardZone getEnchantmentZone() {
        return enchantmentZone;
    }

    public CardZone getGraveyardZone() {
        return graveyardZone;
    }
}
