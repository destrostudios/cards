package com.destrostudios.cards.frontend.application.appstates.services;

import com.destrostudios.cardgui.samples.visualization.CustomAttachmentVisualization;
import com.destrostudios.cardgui.samples.visualization.GlowBox;
import com.destrostudios.cardgui.samples.visualization.PaintableImage;
import com.destrostudios.cardgui.samples.visualization.cards.modelled.FoilModelledCard;
import com.destrostudios.cardgui.samples.visualization.cards.modelled.SimpleModelledCard;
import com.destrostudios.cards.frontend.application.appstates.services.cardpainter.CardPainterJME;
import com.destrostudios.cards.frontend.application.appstates.services.cardpainter.model.CardModel;
import com.destrostudios.cards.shared.rules.cards.Foil;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;

public class IngameCardVisualization extends CustomAttachmentVisualization<Node> {

    public IngameCardVisualization(AssetManager assetManager, boolean fullArt) {
        this.fullArt = fullArt;
        node = new Node();
        foilModelledCard = new FoilModelledCard(assetManager, "models/card/card.j3o", "images/cardbacks/yugioh.png", ColorRGBA.Black);
        node.attachChild(foilModelledCard.getNode());
        glowBox = new GlowBox(assetManager, 0.96f, 1.28f);
    }
    private boolean fullArt;
    private Node node;
    private FoilModelledCard foilModelledCard;
    private GlowBox glowBox;

    public void updateCardFront(CardModel cardModel) {
        int textureWidth = 400;
        int textureHeight = 560;

        PaintableImage imageContent = new PaintableImage(textureWidth, textureHeight);
        PaintableImage imageArtwork = new PaintableImage(textureWidth, textureHeight);
        PaintableImage imageFoilMap = new PaintableImage(textureWidth, textureHeight);
        imageContent.setBackground_Alpha(0);
        imageArtwork.setBackground_Alpha(0);
        imageFoilMap.setBackground_Alpha(0);

        if (fullArt) {
            CardPainterJME.drawCardFront_Minified_Artwork(imageArtwork, cardModel);
            if (cardModel.isFront()) {
                if (cardModel.getFoil() != null) {
                    imageFoilMap.setBackground_Alpha(255);
                }
            }
        } else {
            CardPainterJME.drawCardFront_Full_Content(imageContent, cardModel);
            CardPainterJME.drawCardFront_Full_Artwork(imageArtwork, cardModel);
            if (cardModel.isFront()) {
                if (cardModel.getFoil() == Foil.ARTWORK) {
                    int artworkX = 35;
                    int artworkY = 68;
                    int artworkWidth = 330;
                    int artworkHeight = 241;
                    for (int x = 0; x < artworkWidth; x++) {
                        for (int y = 0; y < artworkHeight; y++) {
                            imageFoilMap.setPixel_Alpha(artworkX + x, artworkY + y, 255);
                        }
                    }
                } else if (cardModel.getFoil() == Foil.FULL) {
                    imageFoilMap.setBackground_Alpha(255);
                }
            }
        }
        if (cardModel.isFront()) {
            CardPainterJME.drawCardFront_ManaCost(imageArtwork, cardModel, fullArt);
            CardPainterJME.drawCardFront_Stats(imageArtwork, cardModel, fullArt);
        }

        Material material = foilModelledCard.getMaterial_Front();
        material.setTexture("DiffuseMap1", SimpleModelledCard.flipAndCreateTexture(imageContent));
        material.setTexture("DiffuseMap2", SimpleModelledCard.flipAndCreateTexture(imageArtwork));
        material.setTexture("FoilMap", SimpleModelledCard.flipAndCreateTexture(imageFoilMap));
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
