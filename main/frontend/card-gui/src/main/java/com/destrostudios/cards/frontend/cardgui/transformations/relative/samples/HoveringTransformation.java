package com.destrostudios.cards.frontend.cardgui.transformations.relative.samples;

import com.destrostudios.cards.frontend.cardgui.transformations.ConstantPositionTransformation;
import com.jme3.math.FastMath;

public class HoveringTransformation extends ConstantPositionTransformation {

    public HoveringTransformation(float height, float timePerCycle) {
        this.height = height;
        this.timePerCycle = timePerCycle;
    }
    private float height;
    private float timePerCycle;
    private float passedTime;

    @Override
    public void update(float lastTimePerFrame) {
        super.update(lastTimePerFrame);
        passedTime += lastTimePerFrame;
        currentValue.setY(((FastMath.sin((passedTime / timePerCycle) * FastMath.TWO_PI) + 1) / 2) * height);
    }
}
