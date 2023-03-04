package com.destrostudios.cards.frontend.application.appstates.services;

import com.destrostudios.cardgui.Card;
import com.destrostudios.cardgui.samples.visualization.CustomAttachmentVisualizer;
import com.destrostudios.cards.frontend.application.appstates.services.cardpainter.model.CardModel;
import com.jme3.asset.AssetManager;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;

public class IngameCardVisualizer extends CustomAttachmentVisualizer<Card<CardModel>, Node, IngameCardVisualization> {

    public IngameCardVisualizer(boolean fullArt, boolean boardAttachments) {
        this(fullArt, boardAttachments, 1);
    }

    public IngameCardVisualizer(boolean fullArt, boolean boardAttachments, float scale) {
        this(fullArt, boardAttachments, scale, true);
    }

    public IngameCardVisualizer(boolean fullArt, boolean boardAttachments, float scale, boolean shadows) {
        this.fullArt = fullArt;
        this.boardAttachments = boardAttachments;
        this.scale = scale;
        this.shadows = shadows;
    }
    private boolean fullArt;
    private boolean boardAttachments;
    private float scale;
    private boolean shadows;

    @Override
    protected IngameCardVisualization createVisualizationObject(AssetManager assetManager) {
        return new IngameCardVisualization(assetManager, fullArt, scale, shadows);
    }

    @Override
    protected void updateVisualizationObject(IngameCardVisualization visualization, Card<CardModel> card, AssetManager assetManager) {
        visualization.updateCardFront(card.getModel());

        if (card.getModel().isPlayable()) {
            visualization.setGlow(ColorRGBA.White);
        } else {
            visualization.removeGlow();
        }

        if (boardAttachments) {
            visualization.setDivineShieldVisible(card.getModel().isDivineShield());
            visualization.setTauntVisible(card.getModel().isTaunt());
        }
    }
}
