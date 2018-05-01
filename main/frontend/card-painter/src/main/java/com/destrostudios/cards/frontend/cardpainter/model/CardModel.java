package com.destrostudios.cards.frontend.cardpainter.model;

import com.destrostudios.cards.frontend.cardgui.BoardObjectModel;

import java.util.List;

public class CardModel extends BoardObjectModel {

    private boolean isFront;
    private List<Color> colors;
    private String title;
    private List<String> tribes;
    private List<String> keywords;
    private String description;
    private String castDescription;
    private List<Spell> spells;
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

    public void setTitle(String title) {
        this.title = title;
    }

    public void setFront(boolean isFront) {
        updateIfNotEquals(this.isFront, isFront, () -> this.isFront = isFront);
    }

    public void setColors(List<Color> colors) {
        updateIfNotEquals(this.colors, colors, () -> this.colors = colors);
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
