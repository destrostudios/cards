package com.destrostudios.cards.frontend.cardpainter.model;

import com.destrostudios.cards.frontend.cardgui.BoardObjectModel;

import java.util.LinkedList;
import java.util.List;

public class CardModel extends BoardObjectModel {

    private boolean isFront;
    private List<Color> colors = new LinkedList<>();
    private String title;
    private ManaCost manaCost;
    private List<String> tribes = new LinkedList<>();
    private List<String> keywords = new LinkedList<>();
    private String description;
    private String castDescription;
    private List<Spell> spells = new LinkedList<>();
    private Integer attackDamage;
    private Integer lifepoints;
    private boolean isDamaged;
    private String flavourText;

    public boolean isFront() {
        return isFront;
    }

    public List<Color> getColors() {
        return colors;
    }

    public String getTitle() {
        return title;
    }

    public ManaCost getManaCost() {
        return manaCost;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public List<String> getTribes() {
        return tribes;
    }

    public Integer getAttackDamage() {
        return attackDamage;
    }

    public Integer getLifepoints() {
        return lifepoints;
    }

    public boolean isDamaged() {
        return isDamaged;
    }

    public List<Spell> getSpells() {
        return spells;
    }

    public String getCastDescription() {
        return castDescription;
    }

    public String getFlavourText() {
        return flavourText;
    }

    public void setFront(boolean isFront) {
        updateIfNotEquals(this.isFront, isFront, () -> this.isFront = isFront);
    }

    public void addColor(Color color) {
        addElement(colors, color);
    }

    public void removeColor(Color color) {
        removeElement(colors, color);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setManaCost(ManaCost manaCost) {
        this.manaCost = manaCost;
    }

    public void setDescription(String description) {
        updateIfNotEquals(this.description, description, () -> this.description = description);
    }

    public void addKeyword(String keyword) {
        addElement(keywords, keyword);
    }

    public void removeKeyword(String keyword) {
        removeElement(keywords, keyword);
    }

    public void addTribe(String tribe) {
        addElement(tribes, tribe);
    }

    public void removeTribe(String tribe) {
        removeElement(tribes, tribe);
    }

    public void setAttackDamage(Integer attackDamage) {
        updateIfNotEquals(this.attackDamage, attackDamage, () -> this.attackDamage = attackDamage);
    }

    public void setLifepoints(Integer lifepoints) {
        updateIfNotEquals(this.lifepoints, lifepoints, () -> this.lifepoints = lifepoints);
    }

    public void setDamaged(boolean isDamaged) {
        updateIfNotEquals(this.isDamaged, isDamaged, () -> this.isDamaged = isDamaged);
    }

    public void setSpells(List<Spell> spells) {
        updateIfNotEquals(this.spells, spells, () -> this.spells = spells);
    }

    public void setCastDescription(String castDescription) {
        updateIfNotEquals(this.castDescription, castDescription, () -> this.castDescription = castDescription);
    }

    public void setFlavourText(String flavourText) {
        updateIfNotEquals(this.flavourText, flavourText, () -> this.flavourText = flavourText);
    }
}
