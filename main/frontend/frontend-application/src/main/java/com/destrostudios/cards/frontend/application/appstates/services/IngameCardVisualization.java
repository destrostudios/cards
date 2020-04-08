package com.destrostudios.cards.frontend.application.appstates.services;

import com.destrostudios.cardgui.samples.visualization.CustomAttachmentVisualization;
import com.destrostudios.cardgui.samples.visualization.GlowBox;
import com.destrostudios.cardgui.samples.visualization.PaintableImage;
import com.destrostudios.cardgui.samples.visualization.cards.modelled.FoilModelledCard;
import com.destrostudios.cards.frontend.application.appstates.services.cardpainter.CardPainterJME;
import com.destrostudios.cards.frontend.application.appstates.services.cardpainter.model.CardModel;
import com.destrostudios.cards.shared.rules.cards.Foil;
import com.jme3.asset.AssetManager;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;

public class IngameCardVisualization extends CustomAttachmentVisualization<Node> {

    public IngameCardVisualization(AssetManager assetManager, boolean minified) {
        this.minified = minified;
        node = new Node();
        foilModelledCard = new FoilModelledCard(assetManager, "models/card/card.j3o", "images/cardbacks/magic.png", ColorRGBA.Black);
        node.attachChild(foilModelledCard.getNode());
        glowBox = new GlowBox(assetManager, 0.96f, 1.28f);
    }
    private boolean minified;
    private Node node;
    private FoilModelledCard foilModelledCard;
    private GlowBox glowBox;

    public void updateCardFront(CardModel cardModel) {
        int textureWidth = 400;
        int textureHeight = 560;

        PaintableImage imageBack = new PaintableImage(textureWidth, textureHeight);
        PaintableImage imageFoil = new PaintableImage(textureWidth, textureHeight);
        PaintableImage imageFront = new PaintableImage(textureWidth, textureHeight);
        imageBack.setBackground_Alpha(0);
        imageFoil.setBackground_Alpha(0);
        imageFront.setBackground_Alpha(0);

        PaintableImage imageForContent = ((cardModel.getFoil() == Foil.FULL) ? imageFoil : imageBack);
        PaintableImage imageForArtwork = ((cardModel.getFoil() != null) ? imageFoil : imageBack);

        if (minified) {
            CardPainterJME.drawCardFront_Minified_Artwork(imageForArtwork, cardModel);
        } else {
            CardPainterJME.drawCardFront_Full_Content(imageForContent, cardModel);
            CardPainterJME.drawCardFront_Full_Artwork(imageForArtwork, cardModel);
        }
        CardPainterJME.drawCardFront_Front(imageFront, cardModel);

        foilModelledCard.setFront(imageBack, imageFoil, imageFront);
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
