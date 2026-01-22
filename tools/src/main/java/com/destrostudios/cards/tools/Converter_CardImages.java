package com.destrostudios.cards.tools;

import com.destrostudios.cards.shared.application.ApplicationSetup;
import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.entities.SimpleEntityData;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.Powers;
import com.destrostudios.cards.shared.rules.util.CardUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Converter_CardImages {

    private static final String SOURCE_DIRECTORY_ROOT = "./cards/";
    private static final String TARGET_DIRECTORY_ROOT = "../../assets/images/";

    private static HashMap<String, Integer> POWER_X_OFFSETS = new HashMap<>();
    static {
        POWER_X_OFFSETS.put("powers/dark_bargain", -25);
        POWER_X_OFFSETS.put("powers/fair_trade", -45);
        POWER_X_OFFSETS.put("powers/hearty_stew", 55);
        POWER_X_OFFSETS.put("powers/rubble", -50);
        POWER_X_OFFSETS.put("powers/spark", -70);
    };

    public static void main(String[] args) throws IOException {
        ApplicationSetup.setup();
        convertCardImages();
        convertPowerImages();
    }

    private static void convertCardImages() throws IOException {
        int targetWidth = 500;
        int targetHeight = 365;
        float targetRatio = ((float) targetWidth) / targetHeight;
        for (File sourceImageFile : new File(SOURCE_DIRECTORY_ROOT).listFiles()) {
            convertImage(sourceImageFile, "cards/", targetRatio, targetWidth, targetHeight, 0);
        }
    }

    private static void convertPowerImages() throws IOException {
        int targetSize = 365;
        EntityData data = new SimpleEntityData(Components.ALL);
        for (String powerPath : Powers.ALL) {
            int power = CardUtil.createCard(data, powerPath);
            String powerName = data.getComponent(power, Components.NAME);
            File sourceImageFile = new File(SOURCE_DIRECTORY_ROOT + powerName + ".jpg");
            convertImage(sourceImageFile, "powers/", 1, targetSize, targetSize, POWER_X_OFFSETS.getOrDefault(powerPath, 0));
        }
    }

    private static void convertImage(File sourceImageFile, String targetDirectory, float targetRatio, int targetWidth, int targetHeight, int offsetX) throws IOException {
        System.out.println("Converting " + sourceImageFile.getPath());
        BufferedImage sourceImage = ImageIO.read(sourceImageFile);
        int sourceWidthForTargetRatio = Math.round(sourceImage.getHeight() * targetRatio);
        BufferedImage targetImage = ImageUtil.extractHorizontalCenter(sourceImage, sourceWidthForTargetRatio, offsetX);
        BufferedImage targetImageScaledDown = ImageUtil.resize(targetImage, targetWidth, targetHeight);
        ImageIO.write(targetImageScaledDown, "png", new File(TARGET_DIRECTORY_ROOT + targetDirectory + sourceImageFile.getName().replace(".jpg", ".png")));
    }
}
