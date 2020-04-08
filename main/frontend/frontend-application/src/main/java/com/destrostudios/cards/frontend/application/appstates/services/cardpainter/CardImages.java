package com.destrostudios.cards.frontend.application.appstates.services.cardpainter;

import com.destrostudios.cards.frontend.application.appstates.services.cardpainter.model.CardModel;
import com.destrostudios.cards.shared.files.FileAssets;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 *
 * @author Carl
 */
public class CardImages{

    private static HashMap<String, Image> imageCache = new HashMap<>();

    public static Image getCachedImage(String resourcePath){
        return getCachedImage(resourcePath, -1, -1);
    }

    public static Image getCachedImage(String filePath, int width, int height){
        String key = (filePath + "_" + width + "_" + height);
        Image image = imageCache.get(key);
        if(image == null){
            try {
                image = ImageIO.read(new File(FileAssets.ROOT + filePath));
                if((width != -1) && (height != -1)){
                    int scaleMode = (filePath.endsWith(".gif")?Image.SCALE_FAST:Image.SCALE_SMOOTH);
                    image = image.getScaledInstance(width, height, scaleMode);
                }
                imageCache.put(key, image);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return image;
    }

    public static String getCardImageFilePath(CardModel cardModel){
        String filePath = "images/";
        String titleImageSuffix = ("cards/" + cardModel.getTitle());
        if(FileAssets.exists(filePath + titleImageSuffix + ".gif")){
            filePath += titleImageSuffix + ".gif";
        }
        else if(FileAssets.exists(filePath + titleImageSuffix + ".png")){
            filePath += titleImageSuffix + ".png";
        }
        else if(FileAssets.exists(filePath + titleImageSuffix + ".jpg")){
            filePath += titleImageSuffix + ".jpg";
        }
        else{
            filePath += "cards/other.png";
        }
        return filePath;
    }
}
