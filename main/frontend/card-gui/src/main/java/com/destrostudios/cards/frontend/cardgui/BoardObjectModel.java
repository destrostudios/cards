package com.destrostudios.cards.frontend.cardgui;

import java.util.Objects;

public class BoardObjectModel {

    private boolean wasChanged;

    public <T> void updateIfNotEquals(T oldValue, T newValue, PropertyUpdater propertyUpdater) {
        if (!Objects.equals(oldValue, newValue)) {
            propertyUpdater.updateProperty();
            this.wasChanged = true;
        }
    }

    public void onUpdate() {
        this.wasChanged = false;
    }

    public boolean wasChanged() {
        return wasChanged;
    }

    public interface PropertyUpdater {
        void updateProperty();
    }
}
