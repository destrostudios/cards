package com.destrostudios.cards.frontend.application.appstates.services.cardpainter;

import com.destrostudios.cardgui.samples.visualization.PaintableImage;
import com.jme3.texture.Texture2D;
import lombok.Getter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.function.Consumer;

public class AWTPaintableTexture {

    public AWTPaintableTexture(int width, int height) {
        paintableImage = new PaintableImage(width, height);
        bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        texture = new Texture2D();
    }
    @Getter
    private PaintableImage paintableImage;
    private BufferedImage bufferedImage;
    @Getter
    private Texture2D texture;

    public void draw(Consumer<Graphics2D> painter) {
        Graphics2D graphics = (Graphics2D) bufferedImage.getGraphics();
        painter.accept(graphics);
        paintableImage.paintImage(bufferedImage, 0, 0);
    }

    public void flipAndUpdateTexture() {
        paintableImage.flipY();
        texture.setImage(paintableImage.getImage());
    }
}
