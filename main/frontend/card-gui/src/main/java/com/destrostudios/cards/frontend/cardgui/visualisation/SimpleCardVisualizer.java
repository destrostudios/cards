package com.destrostudios.cards.frontend.cardgui.visualisation;

import com.destrostudios.cards.frontend.cardgui.Card;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture2D;
import com.destrostudios.cards.frontend.cardgui.BoardObjectVisualizer;
import com.jme3.material.RenderState;
import com.jme3.math.FastMath;

/**
 *
 * @author Carl
 */
public abstract class SimpleCardVisualizer implements BoardObjectVisualizer<Card> {
    
    private static final String GEOMETRY_NAME = "cardGeometry";

    @Override
    public void createVisualisation(Node node, AssetManager assetManager) {
        Geometry geometry = new Geometry(GEOMETRY_NAME, new Box(0.4f, 0.01f, 0.6f));
        Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        material.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        geometry.setMaterial(material);
        geometry.setQueueBucket(RenderQueue.Bucket.Transparent);
        geometry.rotate(0, FastMath.HALF_PI, 0);
        node.attachChild(geometry);
    }

    @Override
    public void updateVisualisation(Node node, Card card, AssetManager assetManager) {
        Geometry geometry = (Geometry) node.getChild(GEOMETRY_NAME);
        PaintableImage paintableImage = paintCard(card);
        Texture2D texture = new Texture2D();
        texture.setImage(paintableImage.getImage());
        geometry.getMaterial().setTexture("ColorMap", texture);
    }
    
    public abstract PaintableImage paintCard(Card card);
}
