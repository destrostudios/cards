package com.destrostudios.cards.frontend.application.appstates.services.players;

import com.destrostudios.cardgui.samples.visualization.SimpleAttachmentVisualizer;
import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

public class PlayerVisualizer extends SimpleAttachmentVisualizer<PlayerBoardObject, BitmapText> {

    private static final float SCALE = 0.01f;

    @Override
    protected BitmapText createVisualizationObject(AssetManager assetManager) {
        BitmapFont font = assetManager.loadFont("Interface/Fonts/Default.fnt");
        BitmapText bitmapText = new BitmapText(font);
        bitmapText.setLocalRotation(new Quaternion().fromAngleAxis(-1 * FastMath.HALF_PI, Vector3f.UNIT_X));
        bitmapText.setLocalScale(SCALE);
        return bitmapText;
    }

    @Override
    protected void updateVisualizationObject(BitmapText bitmapText, PlayerBoardObject playerBoardObject, AssetManager assetManager) {
        bitmapText.setText(playerBoardObject.getModel().getName() + " (" + playerBoardObject.getModel().getHealth() + ")");
        float lineWidth = (bitmapText.getLineWidth() * SCALE);
        bitmapText.setLocalTranslation((lineWidth / -2), 0, 0);
    }
}
