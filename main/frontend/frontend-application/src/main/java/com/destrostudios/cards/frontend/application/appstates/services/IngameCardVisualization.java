package com.destrostudios.cards.frontend.application.appstates.services;

import com.destrostudios.cardgui.samples.visualization.CustomAttachmentVisualization;
import com.destrostudios.cardgui.samples.visualization.GlowBox;
import com.destrostudios.cardgui.samples.visualization.PaintableImage;
import com.destrostudios.cardgui.samples.visualization.cards.modelled.FoilModelledCard;
import com.destrostudios.cards.frontend.cardpainter.CardPainterJME;
import com.destrostudios.cards.frontend.cardpainter.model.CardModel;
import com.jme3.asset.AssetManager;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;

public class IngameCardVisualization extends CustomAttachmentVisualization<Node> {

    public IngameCardVisualization(AssetManager assetManager, boolean minified) {
        this.minified = minified;
        node = new Node();
        modelledCard = new FoilModelledCard(assetManager, "models/card/card.j3o", "images/cardbacks/magic.png", ColorRGBA.Black);
        node.attachChild(modelledCard.getNode());
        glowBox = new GlowBox(assetManager, 0.96f, 1.28f);
    }
    private boolean minified;
    private Node node;
    private FoilModelledCard modelledCard;
    private GlowBox glowBox;

    public void updateCardFront(CardModel cardModel) {
        PaintableImage paintableImage = new PaintableImage(400, 560);
        if (minified) {
            CardPainterJME.drawCard_Minified(paintableImage, cardModel);
        } else {
            CardPainterJME.drawCard_Full(paintableImage, cardModel);
        }
        modelledCard.setFront(paintableImage);
    }

    public void setGlow(ColorRGBA colorRGBA) {
        glowBox.setColor(colorRGBA);
        node.attachChild(glowBox.getGeometry());
    }

    public void removeGlow() {
        node.detachChild(glowBox.getGeometry());
    }

    @Override
    public Node getSpatial() {
        return node;
    }
}
