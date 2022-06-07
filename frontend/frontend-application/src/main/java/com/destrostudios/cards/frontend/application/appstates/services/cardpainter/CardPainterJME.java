package com.destrostudios.cards.frontend.application.appstates.services.cardpainter;

import com.destrostudios.cardgui.samples.visualization.PaintableImage;
import com.destrostudios.cards.frontend.application.appstates.services.cardpainter.model.CardModel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.function.Consumer;

public class CardPainterJME {

    public static void drawCardBack(PaintableImage paintableImage){
        drawCard(paintableImage, graphics -> CardPainterAWT.drawCardBack(graphics, paintableImage.getWidth(), paintableImage.getHeight()));
    }

    public static void drawCardFront_Full_Content(PaintableImage paintableImage, CardModel cardModel){
        drawCard(paintableImage, graphics -> CardPainterAWT.drawCardFront_Full_Content(graphics, cardModel, paintableImage.getWidth(), paintableImage.getHeight()));
    }

    public static void drawCardFront_Full_Artwork(PaintableImage paintableImage, CardModel cardModel){
        drawCard(paintableImage, graphics -> CardPainterAWT.drawCardFront_Full_Artwork(graphics, cardModel));
    }

    public static void drawCardFront_Minified_Artwork(PaintableImage paintableImage, CardModel cardModel){
        drawCard(paintableImage, graphics -> CardPainterAWT.drawCardFront_Minified_Artwork(graphics, cardModel));
    }

    public static void drawCardFront_ManaCost(PaintableImage paintableImage, CardModel cardModel, boolean fullArt){
        drawCard(paintableImage, graphics -> CardPainterAWT.drawCardFront_CardCost(graphics, cardModel, fullArt));
    }

    public static void drawCardFront_Stats(PaintableImage paintableImage, CardModel cardModel, boolean fullArt){
        drawCard(paintableImage, graphics -> CardPainterAWT.drawCardFront_Stats(graphics, cardModel, fullArt));
    }

    private static void drawCard(PaintableImage paintableImage, Consumer<Graphics2D> painter){
        BufferedImage bufferedImage = new BufferedImage(paintableImage.getWidth(), paintableImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = (Graphics2D) bufferedImage.getGraphics();
        painter.accept(graphics);
        paintableImage.paintImage(bufferedImage, 0, 0);
    }
}
