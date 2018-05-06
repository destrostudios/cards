package com.destrostudios.cards.frontend.cardgui;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Objects;

public class BoardObjectModel {

    public BoardObjectModel(){
        this(null);
    }

    public BoardObjectModel(BoardObjectModel parentModel){
        this.parentModel = parentModel;
        if (parentModel != null) {
            parentModel.childModels.add(this);
        }
    }
    private BoardObjectModel parentModel;
    private LinkedList<BoardObjectModel> childModels = new LinkedList<>();
    private boolean wasChanged;

    public <T> void updateIfNotEquals(T oldValue, T newValue, PropertyUpdater propertyUpdater) {
        if (!Objects.equals(oldValue, newValue)) {
            propertyUpdater.updateProperty();
            onChanged();
        }
    }

    public <T> void addElement(Collection<T> collection, T value) {
        collection.add(value);
        onChanged();
    }

    public <T> void removeElement(Collection<T> collection, T value) {
        if (collection.remove(value)) {
            onChanged();
        }
    }

    private void onChanged() {
        wasChanged = true;
        if (parentModel != null) {
            parentModel.onChanged();
        }
    }

    public void onUpdate() {
        wasChanged = false;
        for (BoardObjectModel childModel : childModels) {
            childModel.onUpdate();
        }
    }

    public boolean wasChanged() {
        return wasChanged;
    }

    public interface PropertyUpdater {
        void updateProperty();
    }
}
