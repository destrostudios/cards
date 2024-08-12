package com.destrostudios.cards.tools;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageUtil {

    public static BufferedImage extractHorizontalCenter(BufferedImage source, int width) {
        BufferedImage target = new BufferedImage(width, source.getHeight(), source.getType());
        Graphics2D graphics = target.createGraphics();
        graphics.drawImage(source, ((source.getWidth() - width) / -2), 0, null);
        graphics.dispose();
        return target;
    }

    public static BufferedImage resize(BufferedImage source, int width, int height) {
        Image target = source.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage targetBuffered = new BufferedImage(width, height, source.getType());
        Graphics2D graphics = targetBuffered.createGraphics();
        graphics.drawImage(target, 0, 0, null);
        graphics.dispose();
        return targetBuffered;
    }
}
