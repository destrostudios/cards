package com.destrostudios.cards.frontend.application.appstates.services;

import com.destrostudios.cardgui.samples.visualization.CustomAttachmentVisualization;
import com.destrostudios.cardgui.samples.visualization.background.*;
import com.destrostudios.cardgui.samples.visualization.cards.modelled.FoilModelledCard;
import com.destrostudios.cards.frontend.application.appstates.services.cardpainter.CardPainter;
import com.destrostudios.cards.frontend.application.appstates.services.cardpainter.model.CardModel;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import com.jme3.texture.Texture2D;

public class IngameCardVisualization extends CustomAttachmentVisualization<Node> {

    public IngameCardVisualization(AssetManager assetManager, boolean fullArt, float scale, boolean shadows, float foilDistortion) {
        this.fullArt = fullArt;
        node = new Node();
        node.setLocalScale(scale);
        foilModelledCard = new FoilModelledCard(assetManager, "images/cardbacks/yugioh.png", ColorRGBA.Black);
        foilModelledCard.getMaterial_Front().setFloat("Distortion", foilDistortion);
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
        Material material = foilModelledCard.getMaterial_Front();
        Texture2D[] textures = CardPainter.getAll(cardModel, fullArt);
        for (int i = 0; i < textures.length; i++) {
            String parameterName = ((i < (textures.length - 1)) ? "DiffuseMap" + (i + 1) : "FoilMap");
            if (textures[i] != null) {
                material.setTexture(parameterName, textures[i]);
            } else {
                material.clearParam(parameterName);
            }
        }
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
