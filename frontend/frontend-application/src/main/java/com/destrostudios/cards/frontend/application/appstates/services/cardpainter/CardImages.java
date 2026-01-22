package com.destrostudios.cards.frontend.application.appstates.services.cardpainter;

import com.destrostudios.cardgui.samples.visualization.ImageUtil;
import com.destrostudios.cards.frontend.application.appstates.services.cardpainter.model.CardModel;
import com.destrostudios.cards.shared.files.FileAssets;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class CardImages {

    private static HashMap<String, BufferedImage> CACHED_IMAGES = new HashMap<>();

    public static BufferedImage getCachedImage(String filePath, int width, int height) {
        BufferedImage image = getCachedImage(filePath);
        String key = filePath + "_" + width + "_" + height;
        return CACHED_IMAGES.computeIfAbsent(key, _ -> ImageUtil.resizeImage(image, width, height));
    }

    public static BufferedImage getCachedImage(String filePath) {
        return CACHED_IMAGES.computeIfAbsent(filePath, _ -> {
            try {
                return ImageIO.read(new File(FileAssets.ROOT + filePath));
            } catch (IOException ex) {
                throw new RuntimeException(filePath, ex);
            }
        });
    }

    public static String getCardImageFilePath(CardModel cardModel) {
        return getCardImageFilePath(cardModel.getName());
    }

    public static String getCardImageFilePath(String cardName) {
        return "images/cards/" + cardName + ".png";
    }

    public static String getPowerImageFilePath(CardModel cardModel) {
        return getPowerImageFilePath(cardModel.getName());
    }

    public static String getPowerImageFilePath(String cardName) {
        return "images/powers/" + cardName + ".png";
    }
}
