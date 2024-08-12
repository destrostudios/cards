package com.destrostudios.cards.tools;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Converter_CardBacks {

    public static void main(String[] args) throws IOException {
        File sourceDirectory = new File("./cardbacks");
        String croppedFileSuffix = "_cropped.png";
        File borderFile = new File(sourceDirectory + "/border.png");
        BufferedImage borderImage = ImageIO.read(borderFile);
        int targetWidth = borderImage.getWidth();
        int targetHeight = borderImage.getHeight();
        for (File file : sourceDirectory.listFiles()) {
            if (file.getName().endsWith(croppedFileSuffix)) {
                String cardbackName = file.getName().substring(0, file.getName().length() - croppedFileSuffix.length());
                System.out.println("Converting " + cardbackName);
                BufferedImage sourceImage = ImageIO.read(file);
                BufferedImage sourceImageScaledDown = ImageUtil.resize(sourceImage, targetWidth, targetHeight);
                BufferedImage targetImage = ImageUtil.overlay(sourceImageScaledDown, borderImage);
                ImageIO.write(targetImage, "png", new File("../../assets/images/cardbacks/" + cardbackName + ".png"));
            }
        }
    }
}
