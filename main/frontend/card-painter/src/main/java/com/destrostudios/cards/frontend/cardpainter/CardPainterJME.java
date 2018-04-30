package com.destrostudios.cards.frontend.cardpainter;

import com.destrostudios.cards.frontend.cardgui.visualisation.PaintableImage;
import com.destrostudios.cards.frontend.cardpainter.model.CardModel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class CardPainterJME {

    public static void drawCard(PaintableImage paintableImage, CardModel cardModel){
        BufferedImage bufferedImage = new BufferedImage(300, 400, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = (Graphics2D) bufferedImage.getGraphics();
        CardPainterAWT.drawCard(graphics, null, cardModel);
        paintableImage.loadImage(bufferedImage, true);
    }
}
