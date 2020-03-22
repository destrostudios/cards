package com.destrostudios.cards.frontend.cardpainter;

import com.destrostudios.cardgui.samples.visualisation.PaintableImage;
import com.destrostudios.cards.frontend.cardpainter.model.CardModel;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.function.Consumer;

public class CardPainterJME {

    public static void drawCard_Full(PaintableImage paintableImage, CardModel cardModel){
        drawCard(paintableImage, graphics -> CardPainterAWT.drawCard_Full(graphics, cardModel, paintableImage.getWidth(), paintableImage.getHeight()));
    }

    public static void drawCard_Minified(PaintableImage paintableImage, CardModel cardModel){
        drawCard(paintableImage, graphics -> CardPainterAWT.drawCard_Minified(graphics, cardModel, paintableImage.getWidth(), paintableImage.getHeight()));
    }

    private static void drawCard(PaintableImage paintableImage, Consumer<Graphics2D> painter){
        BufferedImage bufferedImage = new BufferedImage(paintableImage.getWidth(), paintableImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = (Graphics2D) bufferedImage.getGraphics();
        painter.accept(graphics);
        paintableImage.loadImage(bufferedImage, true);
    }
}
