package com.destrostudios.cards.tools;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Converter_CardImages {

    public static void main(String[] args) throws IOException {
        File sourceDirectory = new File("./cards");
        int targetWidth = 500;
        int targetHeight = 365;
        float targetRatio = ((float) targetWidth) / targetHeight;
        for (File file : sourceDirectory.listFiles()) {
            System.out.println("Converting " + file.getPath());
            BufferedImage sourceImage = ImageIO.read(file);
            int sourceWidthForTargetRatio = Math.round(sourceImage.getHeight() * targetRatio);
            BufferedImage targetImage = ImageUtil.extractHorizontalCenter(sourceImage, sourceWidthForTargetRatio);
            BufferedImage targetImageScaledDown = ImageUtil.resize(targetImage, targetWidth, targetHeight);
            ImageIO.write(targetImageScaledDown, "png", new File("../../assets/images/cards/" + file.getName().replace(".jpg", ".png")));
        }
    }
}
