package com.destrostudios.cards.frontend.application.appstates.services.cardpainter.deckpower;

import com.destrostudios.cardgui.samples.visualization.CustomAttachmentVisualizer;
import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;

public class DeckPowerVisualizer extends CustomAttachmentVisualizer<DeckPower, Node, DeckPowerVisualization> {

    @Override
    protected DeckPowerVisualization createVisualizationObject(AssetManager assetManager) {
        return new DeckPowerVisualization(assetManager);
    }

    @Override
    protected void updateVisualizationObject(DeckPowerVisualization visualization, DeckPower deckPower, AssetManager assetManager) {
        visualization.updateCardFront(deckPower.getModel());
    }
}
