package com.destrostudios.cards.frontend.cardgui;

/**
 *
 * @author Carl
 */
public abstract class Interactivity {

    protected Interactivity(Type type) {
        this.type = type;
    }
    public enum Type {
        CLICK,
        DRAG,
        AIM
    }
    private Type type;

    public Type getType() {
        return type;
    }
}
