package com.destrostudios.cards.frontend.application.appstates.services.cardpainter.deckpower;

import com.destrostudios.cardgui.samples.visualization.CustomAttachmentVisualization;
import com.destrostudios.cardgui.samples.visualization.PaintableImage;
import com.destrostudios.cardgui.samples.visualization.circles.UnshadedSimpleModelledCircle;
import com.destrostudios.cards.frontend.application.appstates.services.IngameCardVisualization;
import com.destrostudios.cards.frontend.application.appstates.services.cardpainter.CardImages;
import com.jme3.asset.AssetManager;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import com.jme3.texture.Texture2D;

import java.awt.image.BufferedImage;

public class DeckPowerVisualization extends CustomAttachmentVisualization<Node> {

    public DeckPowerVisualization(AssetManager assetManager) {
        node = new Node();
        modelledCircle = new UnshadedSimpleModelledCircle(assetManager, IngameCardVisualization.CARDBACK_PATH, ColorRGBA.Black);
        modelledCircle.getNode().setLocalScale(0.48f);
        node.attachChild(modelledCircle.getNode());
    }
    private Node node;
    private UnshadedSimpleModelledCircle modelledCircle;
    private BufferedImage editIcon;

    public void updateCardFront(DeckPowerModel deckPowerModel) {
        String imageFilePath = CardImages.getPowerImageFilePath(deckPowerModel.getName());
        PaintableImage image = new PaintableImage(CardImages.getCachedImage(imageFilePath));

        // Already load it here, to reduce any lag when hovering to a minimum
        if (editIcon == null) {
            editIcon = CardImages.getCachedImage("textures/interface/companion_power_edit.png", image.getWidth(), image.getHeight());
        }
        if (deckPowerModel.isSelected() || deckPowerModel.isHovered()) {
            image.paintImage(editIcon, 0, 0);
        }

        Texture2D texture = new Texture2D();
        texture.setImage(image.getImage());
        modelledCircle.setFront(texture);
    }

    @Override
    public Node getSpatial() {
        return node;
    }
}
