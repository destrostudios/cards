package com.destrostudios.cards.frontend.cardpainter;

import com.destrostudios.cards.frontend.cardgui.files.FileAssets;
import com.destrostudios.cards.frontend.cardpainter.model.CardModel;

import java.awt.Image;
import java.awt.Toolkit;
import java.util.HashMap;

/**
 *
 * @author Carl
 */
public class CardImages{

    private static HashMap<String, Image> imageCache = new HashMap<String, Image>();

    public static Image getCachedImage(String resourcePath){
        return getCachedImage(resourcePath, -1, -1);
    }

    public static Image getCachedImage(String filePath, int width, int height){
        String key = (filePath + "_" + width + "_" + height);
        Image image = imageCache.get(key);
        if(image == null){
            image = Toolkit.getDefaultToolkit().createImage(FileAssets.ROOT + filePath);
            if((width != -1) && (height != -1)){
                int scaleMode = (filePath.endsWith(".gif")?Image.SCALE_FAST:Image.SCALE_SMOOTH);
                image = image.getScaledInstance(width, height, scaleMode);
            }
            imageCache.put(key, image);
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
            filePath += "cards/Other.png";
        }
        return filePath;
    }
}
