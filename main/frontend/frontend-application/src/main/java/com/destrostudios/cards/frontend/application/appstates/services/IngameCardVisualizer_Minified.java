package com.destrostudios.cards.frontend.application.appstates.services;

import com.destrostudios.cardgui.samples.visualisation.CardBoxVisualizer;
import com.destrostudios.cardgui.samples.visualisation.PaintableImage;
import com.destrostudios.cards.frontend.cardpainter.CardPainterJME;
import com.destrostudios.cards.frontend.cardpainter.model.CardModel;

public class IngameCardVisualizer_Minified extends CardBoxVisualizer<CardModel> {

    @Override
    public PaintableImage paintCard(CardModel cardModel) {
        PaintableImage paintableImage = new PaintableImage(400, 560);
        CardPainterJME.drawCard_Minified(paintableImage, cardModel);
        return paintableImage;
    }
}
