package com.destrostudios.cards.frontend.application;

import com.destrostudios.cardgui.CardZone;

import java.util.ArrayList;

/**
 *
 * @author Carl
 */
public class PlayerZones {

    public PlayerZones(CardZone deckZone, CardZone handZone, CardZone spellZone, CardZone creatureZone, CardZone enchantmentZone, CardZone graveyardZone) {
        this.deckZone = deckZone;
        this.handZone = handZone;
        this.spellZone = spellZone;
        this.creatureZone = creatureZone;
        this.enchantmentZone = enchantmentZone;
        this.graveyardZone = graveyardZone;
        zones.add(deckZone);
        zones.add(handZone);
        zones.add(spellZone);
        zones.add(creatureZone);
        zones.add(enchantmentZone);
        zones.add(graveyardZone);
    }
    private CardZone deckZone;
    private CardZone handZone;
    private CardZone spellZone;
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

    public CardZone getSpellZone() {
        return spellZone;
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
