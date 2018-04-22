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
        int index = 0;
        for (TransformedBoardObject transformedBoardObject : transformedBoardObjects) {
            if (positionTransformation != null) {
                PositionTransformationType clonedPositionTransformation = (PositionTransformationType) positionTransformation.clone();
                updatePositionTransform(index, transformedBoardObject, clonedPositionTransformation);
                transformedBoardObject.setPositionTransformation(clonedPositionTransformation);
            }
            if (rotationTransformation != null) {
                RotationTransformationType clonedRotationTransformation = (RotationTransformationType) rotationTransformation.clone();
                updateRotationTransform(index, transformedBoardObject, clonedRotationTransformation);
                transformedBoardObject.setRotationTransformation(rotationTransformation.clone());
            }
            index++;
        }
    }

    @Override
    public void update(float lastTimePerFrame) {
        super.update(lastTimePerFrame);
        if (reevaluateEveryFrame) {
            int index = 0;
            for (TransformedBoardObject transformedBoardObject : transformedBoardObjects) {
                if (!transformedBoardObject.hasReachedTargetPosition()) {
                    updatePositionTransform(index, transformedBoardObject, (PositionTransformationType) transformedBoardObject.getPositionTransformation());
                }
                if (!transformedBoardObject.hasReachedTargetRotation()) {
                    updateRotationTransform(index, transformedBoardObject, (RotationTransformationType) transformedBoardObject.getRotationTransformation());
                }
                index++;
            }
        }
        allTargetTransformationsReached = transformedBoardObjects.stream().allMatch(transformedBoardObject -> transformedBoardObject.hasReachedTargetTransform());
    }

    @Override
    public boolean isFinished() {
        return allTargetTransformationsReached;
    }

    protected abstract PositionTransformationType createPositionTransform();

    protected abstract void updatePositionTransform(int index, TransformedBoardObject transformedBoardObject, PositionTransformationType positionTransformation);

    protected abstract RotationTransformationType createRotationTransform();

    protected abstract void updateRotationTransform(int index, TransformedBoardObject transformedBoardObject, RotationTransformationType rotationTransformation);
}
