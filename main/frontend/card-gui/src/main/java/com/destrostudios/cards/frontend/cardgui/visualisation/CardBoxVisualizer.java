package com.destrostudios.cards.frontend.cardgui.visualisation;

import com.destrostudios.cards.frontend.cardgui.BoardObjectModel;
import com.destrostudios.cards.frontend.cardgui.BoardObjectVisualizer;
import com.destrostudios.cards.frontend.cardgui.Card;
import com.jme3.asset.AssetManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.texture.Texture2D;

/**
 *
 * @author Carl
 */
public abstract class CardBoxVisualizer<CardModelType extends BoardObjectModel> implements BoardObjectVisualizer<Card<CardModelType>> {

    @Override
    public void createVisualisation(Node node, AssetManager assetManager) {
        CardBox cardBox = new CardBox(assetManager, "images/cardbacks/magic.png", "images/card_side.png");
        node.attachChild(cardBox.getNode());
    }

    @Override
    public void updateVisualisation(Node node, Card<CardModelType> card, AssetManager assetManager) {
        Geometry faceFront = (Geometry) node.getChild(CardBox.NAME_GEOMETRY_FRONT);
        PaintableImage paintableImage = paintCard(card.getModel());
        Texture2D texture = new Texture2D();
        texture.setImage(paintableImage.getImage());
        faceFront.getMaterial().setTexture("DiffuseMap", texture);
    }

    public abstract PaintableImage paintCard(CardModelType cardModel);
}
