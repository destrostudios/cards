package com.destrostudios.cards.frontend.application.appstates.services;

import com.destrostudios.cardgui.samples.visualization.CustomAttachmentVisualization;
import com.destrostudios.cardgui.samples.visualization.PaintableImage;
import com.destrostudios.cardgui.samples.visualization.background.*;
import com.destrostudios.cardgui.samples.visualization.circles.UnshadedSimpleModelledCircle;
import com.destrostudios.cards.frontend.application.appstates.services.cardpainter.CardImages;
import com.destrostudios.cards.frontend.application.appstates.services.cardpainter.model.CardModel;
import com.jme3.asset.AssetManager;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import com.jme3.texture.Texture2D;

public class IngamePowerVisualization extends CustomAttachmentVisualization<Node> {

    public IngamePowerVisualization(AssetManager assetManager) {
        node = new Node();
        node.setLocalScale(0.3f);
        modelledCircle = new UnshadedSimpleModelledCircle(assetManager, IngameCardVisualization.CARDBACK_PATH, ColorRGBA.Black);
        Node modelledCircleNode = modelledCircle.getNode();
        modelledCircleNode.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        node.attachChild(modelledCircleNode);
        glowQuad = new SimpleGlowQuad(assetManager, 1.32f, 1.32f, SimpleGlowQuad.Shape.CIRCLE);
    }
    private Node node;
    private UnshadedSimpleModelledCircle modelledCircle;
    private SimpleGlowQuad glowQuad;

    public void updateCardFront(CardModel cardModel) {
        String imageFilePath = CardImages.getPowerImageFilePath(cardModel);
        PaintableImage image = new PaintableImage(CardImages.getCachedImage(imageFilePath));
        Texture2D texture = new Texture2D();
        texture.setImage(image.getImage());
        modelledCircle.setFront(texture);
    }

    public void setGlow(ColorRGBA colorRGBA) {
        glowQuad.setColor(colorRGBA);
        glowQuad.getGeometry().setLocalScale(2);
        node.attachChild(glowQuad.getGeometry());
    }

    public void removeGlow() {
        node.detachChild(glowQuad.getGeometry());
    }

    @Override
    public Node getSpatial() {
        return node;
    }
}
