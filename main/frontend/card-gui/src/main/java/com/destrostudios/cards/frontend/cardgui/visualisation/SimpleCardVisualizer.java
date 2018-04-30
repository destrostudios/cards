package com.destrostudios.cards.frontend.cardgui.visualisation;

import com.destrostudios.cards.frontend.cardgui.BoardObjectModel;
import com.destrostudios.cards.frontend.cardgui.Card;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture2D;
import com.destrostudios.cards.frontend.cardgui.BoardObjectVisualizer;
import com.jme3.material.RenderState;

/**
 *
 * @author Carl
 */
public abstract class SimpleCardVisualizer<CardModelType extends BoardObjectModel> implements BoardObjectVisualizer<Card<CardModelType>> {
    
    private static final String GEOMETRY_NAME = "cardGeometry";

    @Override
    public void createVisualisation(Node node, AssetManager assetManager) {
        Box box = new Box(0.4f, 0.01f, 0.6f);
        box.setBuffer(VertexBuffer.Type.TexCoord, 2, new float[]{
            1, 1, 1, 0, 0, 0, 0, 1, // back
            1, 1, 1, 0, 0, 0, 0, 1, // right
            1, 1, 1, 0, 0, 0, 0, 1, // front
            1, 1, 1, 0, 0, 0, 0, 1, // left
            1, 1, 1, 0, 0, 0, 0, 1, // top
            1, 1, 1, 0, 0, 0, 0, 1  // bottom
        });
        Geometry geometry = new Geometry(GEOMETRY_NAME, box);
        Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        material.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        geometry.setMaterial(material);
        geometry.setQueueBucket(RenderQueue.Bucket.Transparent);
        node.attachChild(geometry);
    }

    @Override
    public void updateVisualisation(Node node, Card<CardModelType> card, AssetManager assetManager) {
        Geometry geometry = (Geometry) node.getChild(GEOMETRY_NAME);
        PaintableImage paintableImage = paintCard(card.getModel());
        Texture2D texture = new Texture2D();
        texture.setImage(paintableImage.getImage());
        geometry.getMaterial().setTexture("ColorMap", texture);
    }

    public abstract PaintableImage paintCard(CardModelType cardModel);
}
