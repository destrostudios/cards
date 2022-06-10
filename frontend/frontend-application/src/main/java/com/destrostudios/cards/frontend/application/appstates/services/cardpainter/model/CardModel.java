package com.destrostudios.cards.frontend.application.appstates.services.cardpainter.model;

import com.destrostudios.cardgui.BoardObjectModel;
import com.destrostudios.cards.shared.rules.cards.Foil;

import java.util.List;

public class CardModel extends BoardObjectModel {

    private boolean isFront;
    private boolean isPlayable;

    private String type;
    private String title;
    private Integer manaCostDetails;
    private Integer manaCostFullArt;
    private List<String> tribes;
    private List<String> keywords;
    private String description;
    private List<Spell> spells;
    private Integer attackDamage;
    private Integer lifepoints;
    private boolean isAttackBuffed;
    private boolean isHealthBuffed;
    private boolean isDamaged;
    private boolean divineShield;
    private boolean taunt;
    private String flavourText;
    private Foil foil;

    public boolean isFront() {
        return isFront;
    }

    public boolean isPlayable() {
        return isPlayable;
    }

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public Integer getManaCostDetails() {
        return manaCostDetails;
    }

    public Integer getManaCostFullArt() {
        return manaCostFullArt;
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

    public boolean isAttackBuffed() {
        return isAttackBuffed;
    }

    public boolean isHealthBuffed() {
        return isHealthBuffed;
    }

    public boolean isDamaged() {
        return isDamaged;
    }

    public boolean isDivineShield() {
        return divineShield;
    }

    public boolean isTaunt() {
        return taunt;
    }

    public List<Spell> getSpells() {
        return spells;
    }

    public String getFlavourText() {
        return flavourText;
    }

    public Foil getFoil() {
        return foil;
    }

    public void set(CardModel cardModel) {
        setFront(cardModel.isFront);
        setPlayable(cardModel.isPlayable);
        setType(cardModel.type);
        setTitle(cardModel.title);
        setManaCostFullArt(cardModel.manaCostFullArt);
        setManaCostDetails(cardModel.manaCostDetails);
        setDescription(cardModel.description);
        setKeywords(cardModel.keywords);
        setTribes(cardModel.tribes);
        setAttackDamage(cardModel.attackDamage);
        setLifepoints(cardModel.lifepoints);
        setAttackBuffed(cardModel.isAttackBuffed);
        setHealthBuffed(cardModel.isHealthBuffed);
        setDamaged(cardModel.isDamaged);
        setSpells(cardModel.spells);
        setFlavourText(cardModel.flavourText);
        setFoil(cardModel.foil);
    }

    public void setFront(boolean isFront) {
        updateIfNotEquals(this.isFront, isFront, () -> this.isFront = isFront);
    }

    public void setPlayable(boolean isPlayable) {
        updateIfNotEquals(this.isPlayable, isPlayable, () -> this.isPlayable = isPlayable);
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

    public void setAttackBuffed(boolean isAttackBuffed) {
        updateIfNotEquals(this.isAttackBuffed, isAttackBuffed, () -> this.isAttackBuffed = isAttackBuffed);
    }

    public void setHealthBuffed(boolean isHealthBuffed) {
        updateIfNotEquals(this.isHealthBuffed, isHealthBuffed, () -> this.isHealthBuffed = isHealthBuffed);
    }

    public void setDamaged(boolean isDamaged) {
        updateIfNotEquals(this.isDamaged, isDamaged, () -> this.isDamaged = isDamaged);
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

    public void setFoil(Foil foil) {
        this.foil = foil;
    }
}
