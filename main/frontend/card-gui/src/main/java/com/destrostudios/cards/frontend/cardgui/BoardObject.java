package com.destrostudios.cards.frontend.cardgui;

import java.util.HashMap;

/**
 *
 * @author Carl
 */
public class BoardObject implements GameLoopListener {

    private int id = -1;
    private HashMap<String, String> properties = new HashMap<>();
    private boolean needsVisualisationUpdate;
    private Interactivity interactivity;

    @Override
    public void update(float lastTimePerFrame) {

    }

    public void setProperty(String name, String value) {
        String oldValue = properties.get(name);
        if ((value != oldValue) && (!value.equals(oldValue))) {
            properties.put(name, value);
            needsVisualisationUpdate = true;
        }
    }

    public void onVisualisationUpdate() {
        this.needsVisualisationUpdate = false;
    }

    public boolean needsVisualisationUpdate() {
        return needsVisualisationUpdate;
    }

    public String getProperty(String name) {
        return properties.get(name);
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
