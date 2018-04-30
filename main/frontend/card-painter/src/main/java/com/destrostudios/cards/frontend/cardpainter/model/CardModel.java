package com.destrostudios.cards.frontend.cardpainter.model;

import com.destrostudios.cards.frontend.cardgui.BoardObjectModel;

import java.util.List;

public class CardModel extends BoardObjectModel {

    private boolean isFront;
    private List<Integer> manaTypes;
    private String title;
    private String description;
    private List<String> keywords;
    private List<String> mechanics;
    private List<String> tribes;
    private Integer attackDamage;
    private Integer lifepoints;
    private boolean isDamaged;
    private List<Spell> spells;
    private Spell castSpell;
    private String flavourText;

    public boolean isFront() {
        return isFront;
    }

    public List<Integer> getManaTypes() {
        return manaTypes;
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

    public List<String> getMechanics() {
        return mechanics;
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

    public Spell getCastSpell() {
        return castSpell;
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

    public void setManaTypes(List<Integer> manaTypes) {
        updateIfNotEquals(this.manaTypes, manaTypes, () -> this.manaTypes = manaTypes);
    }

    public void setDescription(String description) {
        updateIfNotEquals(this.description, description, () -> this.description = description);
    }

    public void setKeywords(List<String> keywords) {
        updateIfNotEquals(this.keywords, keywords, () -> this.keywords = keywords);
    }

    public void setMechanics(List<String> mechanics) {
        updateIfNotEquals(this.mechanics, mechanics, () -> this.mechanics = mechanics);
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

    public void setCastSpell(Spell castSpell) {
        updateIfNotEquals(this.castSpell, castSpell, () -> this.castSpell = castSpell);
    }

    public void setFlavourText(String flavourText) {
        updateIfNotEquals(this.flavourText, flavourText, () -> this.flavourText = flavourText);
    }
}
