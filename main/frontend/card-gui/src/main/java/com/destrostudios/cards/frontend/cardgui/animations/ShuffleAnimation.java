package com.destrostudios.cards.frontend.cardgui.animations;

import com.destrostudios.cards.frontend.cardgui.TransformedBoardObject;
import com.destrostudios.cards.frontend.cardgui.transformations.*;
import com.destrostudios.cards.frontend.cardgui.transformations.speeds.*;
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
        super(new FixedTransformAnimation<SimpleTargetPositionTransformation, SimpleTargetRotationTransformation>(transformedBoardObjects, true) {

            @Override
            protected SimpleTargetPositionTransformation createPositionTransform() {
                return new SimpleTargetPositionTransformation(new Vector3f(), new TimeBasedPositionTransformationSpeed(1));
            }

            @Override
            protected void updatePositionTransform(SimpleTargetPositionTransformation positionTransformation) {
                AppSettings settings = application.getContext().getSettings();
                Vector3f origin = application.getCamera().getWorldCoordinates(new Vector2f((settings.getWidth() / 2), (settings.getHeight() / 2)), 0.0f);
                Vector3f direction = application.getCamera().getWorldCoordinates(new Vector2f((settings.getWidth() / 2), (settings.getHeight() / 2)), 0.3f).subtract(origin).normalizeLocal();
                Vector3f targetPosition = origin.addLocal(direction.multLocal(2));
                positionTransformation.setTargetPosition(targetPosition, false);
            }

            @Override
            protected SimpleTargetRotationTransformation createRotationTransform() {
                return new SimpleTargetRotationTransformation(new Quaternion(), new TimeBasedRotationTransformationSpeed(1));
            }

            @Override
            protected void updateRotationTransform(SimpleTargetRotationTransformation rotationTransformation) {
                // Set rotation relative to camera
                Quaternion targetRotation = application.getCamera().getRotation().clone();
                TempVars vars = TempVars.get();
                Quaternion faceToCamera = vars.quat1;
                faceToCamera.fromAngles(FastMath.HALF_PI, -FastMath.HALF_PI, 0);
                targetRotation.multLocal(faceToCamera);
                vars.release();

                rotationTransformation.setTargetRotation(targetRotation, false);
            }
        }, new ResetFixedTransformAnimation(transformedBoardObjects));
    }
}
