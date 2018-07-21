package com.destrostudios.cards.frontend.application.appstates.services;

import com.destrostudios.cards.frontend.cardgui.Card;
import com.destrostudios.cards.frontend.cardgui.visualisation.PaintableImage;
import com.destrostudios.cards.frontend.cardgui.visualisation.SimpleCardVisualizer;
import com.destrostudios.cards.frontend.cardpainter.CardPainterJME;
import com.destrostudios.cards.frontend.cardpainter.model.CardModel;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;

import java.util.HashMap;

public class IngameCardVisualizer extends SimpleCardVisualizer<CardModel> {

    private static final String NAME_GLOW_BOX = "glowBox";

    private HashMap<Node, Geometry> cachedGlowBoxes = new HashMap<>();

    @Override
    public void createVisualisation(Node node, AssetManager assetManager) {
        super.createVisualisation(node, assetManager);
        Geometry glowBox = createGlowBox(assetManager);
        cachedGlowBoxes.put(node, glowBox);
    }

    private Geometry createGlowBox(AssetManager assetManager) {
        // TODO: Have these as setting/constants somewhere in the cardgui
        float cardWidth = 0.4f;
        float cardHeight = 0.6f;
        float cardDepth = 0.01f;
        float glowExtension = 0.08f;
        Geometry geometry = new Geometry(NAME_GLOW_BOX, new Quad(2 * (cardWidth + glowExtension), 2 * (cardHeight + glowExtension)));
        Material material = new Material(assetManager, "materials/glow_box/glow_box.j3md");
        material.setTexture("GlowMap", assetManager.loadTexture("textures/effects/card_glow.png"));
        material.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        geometry.setMaterial(material);
        geometry.setQueueBucket(RenderQueue.Bucket.Translucent);
        geometry.setLocalTranslation(-1 * (cardWidth + glowExtension), -0.5f * cardDepth, cardHeight + glowExtension);
        geometry.rotate(new Quaternion().fromAngleAxis(-1 * FastMath.HALF_PI, Vector3f.UNIT_X));
        geometry.addControl(new PulsatingMaterialParamControl("Alpha", 0.4f, 1, 2.5f));
        return geometry;
    }

    @Override
    public void updateVisualisation(Node node, Card<CardModel> card, AssetManager assetManager) {
        super.updateVisualisation(node, card, assetManager);
        Geometry glowBox = cachedGlowBoxes.get(node);
        if (card.getModel().isPlayable()) {
            // Here we can have different colors for different effects
            glowBox.getMaterial().setColor("Color", ColorRGBA.White);
            node.attachChild(glowBox);
        }
        else {
            node.detachChild(glowBox);
        }
    }

    @Override
    public PaintableImage paintCard(CardModel cardModel) {
        PaintableImage paintableImage = new PaintableImage(400, 560);
        CardPainterJME.drawCard(paintableImage, cardModel);
        return paintableImage;
    }
}