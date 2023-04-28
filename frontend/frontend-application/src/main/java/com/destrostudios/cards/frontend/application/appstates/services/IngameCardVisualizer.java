package com.destrostudios.cards.frontend.application.appstates.services;

import com.destrostudios.cardgui.Card;
import com.destrostudios.cardgui.samples.visualization.CustomAttachmentVisualizer;
import com.destrostudios.cards.frontend.application.appstates.services.cardpainter.model.CardModel;
import com.jme3.asset.AssetManager;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class IngameCardVisualizer extends CustomAttachmentVisualizer<Card<CardModel>, Node, IngameCardVisualization> {

    public static IngameCardVisualizer forCollection() {
        return new IngameCardVisualizer(false, false, 4.25f, false, 0.0003f);
    }

    public static IngameCardVisualizer forPack() {
        return new IngameCardVisualizer(false, false, 3.8f, false, 0.002f);
    }

    public static IngameCardVisualizer forIngame_General(boolean fullArt, boolean boardAttachments) {
        return new IngameCardVisualizer(fullArt, boardAttachments, 1, true, 0.002f);
    }

    public static IngameCardVisualizer forIngame_InspectOrSelect() {
        return new IngameCardVisualizer(false, false, 1, false, 0.001f);
    }

    private boolean fullArt;
    private boolean boardAttachments;
    private float scale;
    private boolean shadows;
    private float foilDistortion;

    @Override
    protected IngameCardVisualization createVisualizationObject(AssetManager assetManager) {
        return new IngameCardVisualization(assetManager, fullArt, scale, shadows, foilDistortion);
    }

    @Override
    protected void updateVisualizationObject(IngameCardVisualization visualization, Card<CardModel> card, AssetManager assetManager) {
        visualization.updateCardFront(card.getModel());

        if (card.getModel().isPlayable()) {
            visualization.setGlow(ColorRGBA.White);
        } else {
            visualization.removeGlow();
        }

        if (boardAttachments) {
            visualization.setDivineShieldVisible(card.getModel().isDivineShield());
            visualization.setTauntVisible(card.getModel().isTaunt());
        }
    }
}
