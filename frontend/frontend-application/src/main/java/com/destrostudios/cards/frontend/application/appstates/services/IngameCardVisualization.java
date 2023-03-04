package com.destrostudios.cards.frontend.application.appstates.services;

import com.destrostudios.cardgui.samples.visualization.CustomAttachmentVisualization;
import com.destrostudios.cardgui.samples.visualization.background.*;
import com.destrostudios.cardgui.samples.visualization.PaintableImage;
import com.destrostudios.cardgui.samples.visualization.cards.modelled.FoilModelledCard;
import com.destrostudios.cardgui.samples.visualization.cards.modelled.SimpleModelledCard;
import com.destrostudios.cards.frontend.application.appstates.services.cardpainter.CardPainterJME;
import com.destrostudios.cards.frontend.application.appstates.services.cardpainter.model.CardModel;
import com.destrostudios.cards.shared.rules.cards.Foil;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;

public class IngameCardVisualization extends CustomAttachmentVisualization<Node> {

    public IngameCardVisualization(AssetManager assetManager, boolean fullArt, float scale, boolean shadows) {
        this.fullArt = fullArt;
        node = new Node();
        node.setLocalScale(scale);
        foilModelledCard = new FoilModelledCard(assetManager, "models/card/card.j3o", "images/cardbacks/yugioh.png", ColorRGBA.Black);
        Node foilModelledCardNode = foilModelledCard.getNode();
        foilModelledCardNode.setShadowMode(shadows ? RenderQueue.ShadowMode.CastAndReceive : RenderQueue.ShadowMode.Off);
        this.node.attachChild(foilModelledCardNode);
        glowQuad = new GlowQuad(assetManager, 0.96f, 1.28f);
        tauntBox = new TextureQuad(assetManager, 0.96f, 1.5f);
        tauntBox.setTexture(assetManager.loadTexture("images/taunt.png"));
        divineShieldBox = new ColorBox(assetManager, 0.96f / 2.1f, 0.1f, 1.28f / 2.1f);
        divineShieldBox.setColor(new ColorRGBA(1, 0.9f, 0, 0.2f));
    }
    private boolean fullArt;
    private Node node;
    private FoilModelledCard foilModelledCard;
    private GlowQuad glowQuad;
    private TextureQuad tauntBox;
    private ColorBox divineShieldBox;

    public void updateCardFront(CardModel cardModel) {
        int textureWidth = 400;
        int textureHeight = 560;

        PaintableImage imageContent = new PaintableImage(textureWidth, textureHeight);
        PaintableImage imageArtwork = new PaintableImage(textureWidth, textureHeight);
        PaintableImage imageFoilMap = new PaintableImage(textureWidth, textureHeight);
        imageContent.setBackground_Alpha(0);
        imageArtwork.setBackground_Alpha(0);
        imageFoilMap.setBackground_Alpha(0);

        if (cardModel.isFront()) {
            if (fullArt) {
                CardPainterJME.drawCardFront_Minified_Artwork(imageArtwork, cardModel);
                if (cardModel.getFoil() != null) {
                    imageFoilMap.setBackground_Alpha(255);
                }
            } else {
                CardPainterJME.drawCardFront_Full_Content(imageContent, cardModel);
                CardPainterJME.drawCardFront_Full_Artwork(imageArtwork, cardModel);
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
            CardPainterJME.drawCardFront_ManaCost(imageArtwork, cardModel, fullArt);
            CardPainterJME.drawCardFront_Stats(imageArtwork, cardModel, fullArt);
        } else {
            CardPainterJME.drawCardBack(imageContent);
        }

        Material material = foilModelledCard.getMaterial_Front();
        material.setTexture("DiffuseMap1", SimpleModelledCard.flipAndCreateTexture(imageContent));
        material.setTexture("DiffuseMap2", SimpleModelledCard.flipAndCreateTexture(imageArtwork));
        material.setTexture("FoilMap", SimpleModelledCard.flipAndCreateTexture(imageFoilMap));
    }

    public void setGlow(ColorRGBA colorRGBA) {
        glowQuad.setColor(colorRGBA);
        node.attachChild(glowQuad.getGeometry());
    }

    public void removeGlow() {
        node.detachChild(glowQuad.getGeometry());
    }

    public void setTauntVisible(boolean visible) {
        setBackgroundVisible(tauntBox, visible);
    }

    public void setDivineShieldVisible(boolean visible) {
        setBackgroundVisible(divineShieldBox, visible);
    }

    private void setBackgroundVisible(BackgroundQuad backgroundQuad, boolean visible) {
        if (visible) {
            node.attachChild(backgroundQuad.getGeometry());
        } else {
            node.detachChild(backgroundQuad.getGeometry());
        }
    }

    private void setBackgroundVisible(BackgroundBox backgroundBox, boolean visible) {
        if (visible) {
            node.attachChild(backgroundBox.getGeometry());
        } else {
            node.detachChild(backgroundBox.getGeometry());
        }
    }

    @Override
    public Node getSpatial() {
        return node;
    }
}
