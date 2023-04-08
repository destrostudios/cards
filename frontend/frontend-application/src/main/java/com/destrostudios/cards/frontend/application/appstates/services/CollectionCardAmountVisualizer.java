package com.destrostudios.cards.frontend.application.appstates.services;

import com.destrostudios.cardgui.StatefulBoardObjectVisualizer;
import com.destrostudios.cardgui.samples.tools.deckbuilder.DeckBuilderCollectionCardAmount;
import com.destrostudios.cardgui.samples.tools.deckbuilder.DeckBuilderCollectionCardAmountModel;
import com.jme3.asset.AssetManager;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.simsilica.lemur.HAlignment;
import com.simsilica.lemur.Label;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CollectionCardAmountVisualizer extends StatefulBoardObjectVisualizer<DeckBuilderCollectionCardAmount, Label> {

    private Node guiNode;

    @Override
    protected Label createVisualizationObject(AssetManager assetManager) {
        Label label = new Label(null);
        label.setPreferredSize(new Vector3f(247, 20, 1));
        label.setTextHAlignment(HAlignment.Center);
        label.setColor(ColorRGBA.White);
        label.setFontSize(12);
        return label;
    }

    @Override
    protected void addVisualization(Node node, Label label) {
        guiNode.attachChild(label);
    }

    @Override
    protected void updateVisualizationObject(Label label, DeckBuilderCollectionCardAmount amount, AssetManager assetManager) {
        DeckBuilderCollectionCardAmountModel model = amount.getModel();

        float x = 135 + (model.getX() * 265);
        float y = 475 - (model.getY() * 363);
        label.setLocalTranslation(x, y, 0);

        int amountCollection = model.getAmountCollection();
        int amountDeck = model.getAmountDeck();
        int amountLeft = (amountCollection - amountDeck);
        label.setText(amountLeft + "/" + amountCollection);

        // TODO: This should maybe be a call to the deck builder, as it has all the rules
        boolean canBeAdded = ((amountDeck < model.getMaximumAmountDeck()) && (amountLeft > 0));
        label.setColor(canBeAdded ? ColorRGBA.White : ColorRGBA.Red);
    }

    @Override
    protected void removeVisualizationObject(Node node, Label label) {
        guiNode.detachChild(label);
    }
}
