package com.destrostudios.cards.tools;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageUtil {

    public static BufferedImage resize(BufferedImage source, int width, int height) {
        Image sourceScaled = source.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage target = new BufferedImage(width, height, source.getType());
        Graphics2D graphics = target.createGraphics();
        graphics.drawImage(sourceScaled, 0, 0, null);
        graphics.dispose();
        return target;
    }

    public static BufferedImage overlay(BufferedImage... sources) {
        BufferedImage target = new BufferedImage(sources[0].getWidth(), sources[0].getHeight(), sources[0].getType());
        Graphics2D graphics = target.createGraphics();
        for (BufferedImage source : sources) {
            graphics.drawImage(source, 0, 0, null);
        }
        graphics.dispose();
        return target;
    }

    public static BufferedImage extractHorizontalCenter(BufferedImage source, int width) {
        BufferedImage target = new BufferedImage(width, source.getHeight(), source.getType());
        Graphics2D graphics = target.createGraphics();
        graphics.drawImage(source, ((source.getWidth() - width) / -2), 0, null);
        graphics.dispose();
        return target;
    }
}
