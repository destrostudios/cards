package com.destrostudios.cards.frontend.application.appstates.services;

import com.destrostudios.cardgui.samples.tools.deckbuilder.SimpleDeckBuilderDeckCardVisualizer;
import com.destrostudios.cardgui.samples.visualization.PaintableImage;
import com.destrostudios.cards.frontend.application.appstates.services.cardpainter.CardImages;
import com.destrostudios.cards.frontend.application.appstates.services.cardpainter.model.CardModel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class DeckBuilderCardVisualizer extends SimpleDeckBuilderDeckCardVisualizer<CardModel> {

    public DeckBuilderCardVisualizer() {
        super(3.98f, 0.57f, 57);
    }
    private static final Font FONT = new Font("Tahoma", Font.BOLD, 22);

    @Override
    protected PaintableImage paintActualCard(CardModel cardModel) {
        PaintableImage image = new PaintableImage(400, 57);
        image.setBackground(Color.BLACK);
        String imageFilePath = CardImages.getCardImageFilePath(cardModel);
        PaintableImage artwork = new PaintableImage((BufferedImage) CardImages.getCachedImage(imageFilePath));
        image.paintImage(artwork, 0, 0, artwork.getWidth(), artwork.getHeight(), amountPixelWidth, 0, (image.getWidth() - amountPixelWidth), image.getHeight());
        paintText(image, amountPixelWidth + 20, (image.getHeight() / 2), false, true, cardModel.getTitle());
        return image;
    }

    @Override
    protected void paintCenteredAmount(PaintableImage image, int x, int y, int amount) {
        paintText(image, x, y, true, true, "" + amount);
    }

    private void paintText(PaintableImage image, int x, int y, boolean centeredX, boolean centeredY, String text) {
        // Overkill implementation via an own AWT image
        BufferedImage bufferedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = (Graphics2D) bufferedImage.getGraphics();
        graphics.setFont(FONT);
        graphics.setColor(Color.WHITE);
        FontMetrics fontMetrics = graphics.getFontMetrics();
        int resultingX = x;
        if (centeredX) {
            resultingX -= (fontMetrics.stringWidth(text) / 2);
        }
        int resultingY = y;
        if (centeredY) {
            resultingY -= (fontMetrics.getHeight() / 2);
            resultingY += fontMetrics.getAscent();
        }
        graphics.drawString(text, resultingX, resultingY);
        image.paintImage(bufferedImage, 0, 0);
    }
}
