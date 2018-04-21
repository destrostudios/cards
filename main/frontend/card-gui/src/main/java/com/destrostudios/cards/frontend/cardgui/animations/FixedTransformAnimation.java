package com.destrostudios.cards.frontend.cardgui.animations;

import com.destrostudios.cards.frontend.cardgui.Animation;
import com.destrostudios.cards.frontend.cardgui.TransformedBoardObject;
import com.destrostudios.cards.frontend.cardgui.transformations.*;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

import java.util.Collection;

public abstract class FixedTransformAnimation extends Animation {

    public FixedTransformAnimation(Collection<? extends TransformedBoardObject> transformedBoardObjects) {
        this.transformedBoardObjects = transformedBoardObjects;
    }
    private Collection<? extends TransformedBoardObject> transformedBoardObjects;
    private boolean isFirstFrame;
    private boolean allTargetTransformationsReached;

    @Override
    public void start() {
        isFirstFrame = true;
    }

    @Override
    public void update(float lastTimePerFrame) {
        super.update(lastTimePerFrame);
        Vector3f targetPosition = getTargetPosition();
        Quaternion targetRotation = getTargetRotation();
        for (TransformedBoardObject transformedBoardObject : transformedBoardObjects) {
            if (isFirstFrame ||(!transformedBoardObject.hasReachedTargetPosition())) {
                transformedBoardObject.setPositionTransformation(new SimpleTargetPositionTransformation(targetPosition));
            }
            if (isFirstFrame ||(!transformedBoardObject.hasReachedTargetRotation())) {
                transformedBoardObject.setRotationTransformation(new SimpleTargetRotationTransformation(targetRotation));
            }
        }
        allTargetTransformationsReached = transformedBoardObjects.stream().allMatch(transformedBoardObject -> transformedBoardObject.hasReachedTargetTransform());
        isFirstFrame = false;
    }

    @Override
    public boolean isFinished() {
        return allTargetTransformationsReached;
    }

    protected abstract Vector3f getTargetPosition();

    protected abstract Quaternion getTargetRotation();
}
