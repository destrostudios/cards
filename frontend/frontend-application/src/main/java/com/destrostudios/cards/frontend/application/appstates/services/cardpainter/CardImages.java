package com.destrostudios.cards.frontend.application.appstates.services.cardpainter;

import com.destrostudios.cards.frontend.application.appstates.services.cardpainter.model.CardModel;
import com.destrostudios.cards.shared.files.FileAssets;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class CardImages {

    private static HashMap<String, Image> CACHED_IMAGES = new HashMap<>();

    public static Image getCachedImage(String filePath, int width, int height) {
        Image image = getCachedImage(filePath);
        String key = (filePath + "_" + width + "_" + height);
        return CACHED_IMAGES.computeIfAbsent(key, k -> {
            int scaleMode = (filePath.endsWith(".gif") ? Image.SCALE_FAST : Image.SCALE_SMOOTH);
            return image.getScaledInstance(width, height, scaleMode);
        });
    }

    public static Image getCachedImage(String filePath) {
        return CACHED_IMAGES.computeIfAbsent(filePath, k -> {
            try {
                return ImageIO.read(new File(FileAssets.ROOT + filePath));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    public static String getCardImageFilePath(CardModel cardModel) {
        String filePath = "images/";
        String titleImageSuffix = ("cards/" + cardModel.getTitle());
        if (FileAssets.exists(filePath + titleImageSuffix + ".gif")) {
            filePath += titleImageSuffix + ".gif";
        } else if (FileAssets.exists(filePath + titleImageSuffix + ".png")) {
            filePath += titleImageSuffix + ".png";
        } else if (FileAssets.exists(filePath + titleImageSuffix + ".jpg")) {
            filePath += titleImageSuffix + ".jpg";
        } else {
            filePath += "cards/unknown.png";
        }
        return filePath;
    }
}
