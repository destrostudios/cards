package com.destrostudios.cards.frontend.application.appstates.services.cardpainter.model;

import com.destrostudios.cardgui.BoardObjectModel;
import com.destrostudios.cards.shared.rules.cards.Foil;
import lombok.Getter;

import java.util.List;

@Getter
public class CardModel extends BoardObjectModel {

    private boolean front;
    private boolean playable;

    private String type;
    private String title;
    private Integer manaCostDetails;
    private Integer manaCostFullArt;
    private StatModification manaCostModification;
    private List<String> tribes;
    private List<String> keywords;
    private String description;
    private List<Spell> spells;
    private Integer attack;
    private Integer health;
    private StatModification attackModification;
    private StatModification healthModification;
    private boolean damaged;
    private boolean divineShield;
    private boolean taunt;
    private String flavourText;
    private boolean legendary;
    private Foil foil;

    public void set(CardModel cardModel) {
        setFront(cardModel.front);
        setPlayable(cardModel.playable);
        setType(cardModel.type);
        setTitle(cardModel.title);
        setManaCostFullArt(cardModel.manaCostFullArt);
        setManaCostDetails(cardModel.manaCostDetails);
        setManaCostModification(cardModel.manaCostModification);
        setDescription(cardModel.description);
        setKeywords(cardModel.keywords);
        setTribes(cardModel.tribes);
        setAttack(cardModel.attack);
        setHealth(cardModel.health);
        setAttackModification(cardModel.attackModification);
        setHealthModification(cardModel.healthModification);
        setDamaged(cardModel.damaged);
        setSpells(cardModel.spells);
        setFlavourText(cardModel.flavourText);
        setLegendary(cardModel.legendary);
        setFoil(cardModel.foil);
    }

    public void setFront(boolean front) {
        updateIfNotEquals(this.front, front, () -> this.front = front);
    }

    public void setPlayable(boolean playable) {
        updateIfNotEquals(this.playable, playable, () -> this.playable = playable);
    }

    public void setType(String type) {
        updateIfNotEquals(this.type, type, () -> this.type = type);
    }

    public void setTitle(String title) {
        updateIfNotEquals(this.title, title, () -> this.title = title);
    }

    public void setManaCostFullArt(Integer manaCostFullArt) {
        updateIfNotEquals(this.manaCostFullArt, manaCostFullArt, () -> this.manaCostFullArt = manaCostFullArt);
    }

    public void setManaCostDetails(Integer manaCostDetails) {
        updateIfNotEquals(this.manaCostDetails, manaCostDetails, () -> this.manaCostDetails = manaCostDetails);
    }

    public void setManaCostModification(StatModification manaCostModification) {
        updateIfNotEquals(this.manaCostModification, manaCostModification, () -> this.manaCostModification = manaCostModification);
    }

    public void setDescription(String description) {
        updateIfNotEquals(this.description, description, () -> this.description = description);
    }

    public void setKeywords(List<String> keywords) {
        updateIfNotEquals(this.keywords, keywords, () -> this.keywords = keywords);
    }

    public void setTribes(List<String> tribes) {
        updateIfNotEquals(this.tribes, tribes, () -> this.tribes = tribes);
    }

    public void setAttack(Integer attack) {
        updateIfNotEquals(this.attack, attack, () -> this.attack = attack);
    }

    public void setHealth(Integer health) {
        updateIfNotEquals(this.health, health, () -> this.health = health);
    }

    public void setAttackModification(StatModification attackModification) {
        updateIfNotEquals(this.attackModification, attackModification, () -> this.attackModification = attackModification);
    }

    public void setHealthModification(StatModification healthModification) {
        updateIfNotEquals(this.healthModification, healthModification, () -> this.healthModification = healthModification);
    }

    public void setDamaged(boolean damaged) {
        updateIfNotEquals(this.damaged, damaged, () -> this.damaged = damaged);
    }

    public void setDivineShield(boolean divineShield) {
        updateIfNotEquals(this.divineShield, divineShield, () -> this.divineShield = divineShield);
    }

    public void setTaunt(boolean taunt) {
        updateIfNotEquals(this.taunt, taunt, () -> this.taunt = taunt);
    }

    public void setSpells(List<Spell> spells) {
        updateIfNotEquals(this.spells, spells, () -> this.spells = spells);
    }

    public void setFlavourText(String flavourText) {
        updateIfNotEquals(this.flavourText, flavourText, () -> this.flavourText = flavourText);
    }

    public void setLegendary(boolean legendary) {
        updateIfNotEquals(this.legendary, legendary, () -> this.legendary = legendary);
    }

    public void setFoil(Foil foil) {
        updateIfNotEquals(this.foil, foil, () -> this.foil = foil);
    }
}
