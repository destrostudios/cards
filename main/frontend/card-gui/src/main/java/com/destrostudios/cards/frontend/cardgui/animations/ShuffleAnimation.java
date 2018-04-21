package com.destrostudios.cards.frontend.cardgui.animations;

import com.destrostudios.cards.frontend.cardgui.TransformedBoardObject;
import com.jme3.app.Application;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.system.AppSettings;
import com.jme3.util.TempVars;

import java.util.Collection;

public class ShuffleAnimation extends StagedAnimation{

    public ShuffleAnimation(Collection<? extends TransformedBoardObject> transformedBoardObjects, Application application) {
        super(new FixedTransformAnimation(transformedBoardObjects) {

            @Override
            protected Vector3f getTargetPosition() {
                AppSettings settings = application.getContext().getSettings();
                Vector3f origin = application.getCamera().getWorldCoordinates(new Vector2f((settings.getWidth() / 2), (settings.getHeight() / 2)), 0.0f);
                Vector3f direction = application.getCamera().getWorldCoordinates(new Vector2f((settings.getWidth() / 2), (settings.getHeight() / 2)), 0.3f).subtract(origin).normalizeLocal();
                return origin.addLocal(direction.multLocal(2));
            }

            @Override
            protected Quaternion getTargetRotation() {
                // Set rotation so the node faces the camera (2d-like)
                Quaternion targetRotation = application.getCamera().getRotation().clone();
                TempVars vars = TempVars.get();
                Quaternion faceToCamera = vars.quat1;
                faceToCamera.fromAngles(-FastMath.HALF_PI, 0, FastMath.PI);
                targetRotation.multLocal(faceToCamera);
                vars.release();
                return targetRotation;
            }
        }, new ResetFixedTransformAnimation(transformedBoardObjects));
    }
}
