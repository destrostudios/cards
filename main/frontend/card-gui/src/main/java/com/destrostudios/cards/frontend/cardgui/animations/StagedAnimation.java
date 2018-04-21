package com.destrostudios.cards.frontend.cardgui.animations;

import com.destrostudios.cards.frontend.cardgui.Animation;

public class StagedAnimation extends Animation {

    public StagedAnimation(Animation... stages) {
        this.stages = stages;
    }
    private Animation[] stages;
    private int currentStageIndex;

    @Override
    public void start() {
        super.start();
        startNextStage();
    }

    @Override
    public void update(float lastTimePerFrame) {
        super.update(lastTimePerFrame);
        Animation currentStage = stages[currentStageIndex];
        currentStage.update(lastTimePerFrame);
        if (currentStage.isFinished()) {
            currentStageIndex++;
            startNextStage();
        }
    }

    private void startNextStage() {
        if (currentStageIndex < stages.length) {
            stages[currentStageIndex].start();
        }
    }

    @Override
    public boolean isFinished() {
        return (currentStageIndex >= stages.length);
    }
}
