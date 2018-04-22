package com.destrostudios.cards.frontend.cardgui.animations;

import com.destrostudios.cards.frontend.cardgui.Animation;
import com.destrostudios.cards.frontend.cardgui.TransformedBoardObject;
import com.destrostudios.cards.frontend.cardgui.transformations.*;

import java.util.Collection;

public abstract class FixedTransformAnimation<PositionTransformationType extends PositionTransformation, RotationTransformationType extends RotationTransformation> extends Animation {

    public FixedTransformAnimation(Collection<? extends TransformedBoardObject> transformedBoardObjects) {
        this(transformedBoardObjects, false);
    }

    public FixedTransformAnimation(Collection<? extends TransformedBoardObject> transformedBoardObjects, boolean reevaluateEveryFrame) {
        this.transformedBoardObjects = transformedBoardObjects;
        this.reevaluateEveryFrame = reevaluateEveryFrame;
    }
    private Collection<? extends TransformedBoardObject> transformedBoardObjects;
    private boolean reevaluateEveryFrame;
    private boolean allTargetTransformationsReached;

    @Override
    public void start() {
        PositionTransformationType positionTransformation = createPositionTransform();
        RotationTransformationType rotationTransformation = createRotationTransform();
        updatePositionTransform(positionTransformation);
        updateRotationTransform(rotationTransformation);
        for (TransformedBoardObject transformedBoardObject : transformedBoardObjects) {
            transformedBoardObject.setPositionTransformation(positionTransformation.clone());
            transformedBoardObject.setRotationTransformation(rotationTransformation.clone());
        }
    }

    @Override
    public void update(float lastTimePerFrame) {
        super.update(lastTimePerFrame);
        if (reevaluateEveryFrame) {
            for (TransformedBoardObject transformedBoardObject : transformedBoardObjects) {
                if (!transformedBoardObject.hasReachedTargetPosition()) {
                    updatePositionTransform((PositionTransformationType) transformedBoardObject.getPositionTransformation());
                }
                if (!transformedBoardObject.hasReachedTargetRotation()) {
                    updateRotationTransform((RotationTransformationType) transformedBoardObject.getRotationTransformation());
                }
            }
        }
        allTargetTransformationsReached = transformedBoardObjects.stream().allMatch(transformedBoardObject -> transformedBoardObject.hasReachedTargetTransform());
    }

    @Override
    public boolean isFinished() {
        return allTargetTransformationsReached;
    }

    protected abstract PositionTransformationType createPositionTransform();

    protected abstract void updatePositionTransform(PositionTransformationType positionTransformation);

    protected abstract RotationTransformationType createRotationTransform();

    protected abstract void updateRotationTransform(RotationTransformationType rotationTransformation);
}
