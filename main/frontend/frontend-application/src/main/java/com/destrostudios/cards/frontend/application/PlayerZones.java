package com.destrostudios.cards.frontend.application;

import com.destrostudios.cardgui.CardZone;

import java.util.ArrayList;

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
        zones.add(deckZone);
        zones.add(handZone);
        zones.add(landZone);
        zones.add(creatureZone);
        zones.add(enchantmentZone);
        zones.add(graveyardZone);
    }
    private CardZone deckZone;
    private CardZone handZone;
    private CardZone landZone;
    private CardZone creatureZone;
    private CardZone enchantmentZone;
    private CardZone graveyardZone;
    private ArrayList<CardZone> zones = new ArrayList<>(6);

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

    public ArrayList<CardZone> getZones() {
        return zones;
    }
}
