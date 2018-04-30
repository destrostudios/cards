package com.destrostudios.cards.frontend.cardgui;

/**
 *
 * @author Carl
 */
public class BoardObject<ModelType extends BoardObjectModel> implements GameLoopListener {

    private int id = -1;
    private ModelType model;
    private boolean checkForVisualisationUpdate;
    private Interactivity interactivity;

    @Override
    public void update(float lastTimePerFrame) {

    }

    protected void setModel(ModelType model) {
        this.model = model;
    }

    public ModelType getModel() {
        return model;
    }

    public void checkForVisualisationUpdate() {
        checkForVisualisationUpdate = true;
    }

    public void onVisualisationUpdate() {
        model.onUpdate();
        checkForVisualisationUpdate = false;
    }

    public boolean needsVisualisationUpdate() {
        return checkForVisualisationUpdate && model.wasChanged();
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public int getId() {
        return id;
    }

    public void clearInteractivity() {
        setInteractivity(null);
    }

    public void setInteractivity(Interactivity interactivity) {
        this.interactivity = interactivity;
    }

    public Interactivity getInteractivity() {
        return interactivity;
    }
}
