package com.destrostudios.cards.frontend.application.appstates.services.cardpainter.model;

import com.destrostudios.cardgui.BoardObjectModel;
import com.destrostudios.cardgui.annotations.IsBoardObjectInspected;
import com.destrostudios.cards.shared.rules.cards.Foil;

import java.util.List;

public class CardModel extends BoardObjectModel {

    @IsBoardObjectInspected
    private boolean isInspected;

    private boolean isFront;
    private boolean isPlayable;

    private List<Color> colors;
    private String title;
    private Integer manaCost;
    private List<String> tribes;
    private List<String> keywords;
    private String castDescription;
    private String description;
    private List<Spell> spells;
    private Integer attackDamage;
    private Integer lifepoints;
    private boolean isDamaged;
    private String flavourText;
    private Foil foil;

    public boolean isInspected() {
        return isInspected;
    }

    public boolean isFront() {
        return isFront;
    }

    public boolean isPlayable() {
        return isPlayable;
    }

    public List<Color> getColors() {
        return colors;
    }

    public String getTitle() {
        return title;
    }

    public Integer getManaCost() {
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

    public Foil getFoil() {
        return foil;
    }

    public void setFront(boolean isFront) {
        updateIfNotEquals(this.isFront, isFront, () -> this.isFront = isFront);
    }

    public void setPlayable(boolean isPlayable) {
        updateIfNotEquals(this.isPlayable, isPlayable, () -> this.isPlayable = isPlayable);
    }

    public void setTitle(String title) {
        updateIfNotEquals(this.title, title, () -> this.title = title);
    }

    public void setManaCost(Integer manaCost) {
        updateIfNotEquals(this.manaCost, manaCost, () -> this.manaCost = manaCost);
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

    public void setFoil(Foil foil) {
        this.foil = foil;
    }
}
