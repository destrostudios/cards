package com.destrostudios.cards.frontend.cardgui;

import java.util.LinkedList;

public class AnimationQueue implements GameLoopListener {

    private LinkedList<Animation> animationsQueue = new LinkedList<>();
    private LinkedList<Animation> playingAnimations = new LinkedList<>();
    private LinkedList<Animation> finishedAnimations = new LinkedList<>();
    private Animation blockingAnimation;

    public void addAnimation(Animation animation) {
        animationsQueue.add(animation);
    }

    @Override
    public void update(float lastTimePerFrame) {
        updatePlayingAnimations(lastTimePerFrame);
        checkBlockingAnimation();
        while((blockingAnimation == null) && (animationsQueue.size() > 0)){
            startNextAnimation();
        }
    }

    private void updatePlayingAnimations(float lastTimePerFrame) {
        finishedAnimations.clear();
        for (Animation playingAnimation : playingAnimations) {
            playingAnimation.update(lastTimePerFrame);
            if (playingAnimation.isFinished()) {
                finishedAnimations.add(playingAnimation);
                if (playingAnimation == blockingAnimation) {
                    blockingAnimation = null;
                }
            }
        }
        playingAnimations.removeAll(finishedAnimations);
    }

    private void checkBlockingAnimation() {
        if ((blockingAnimation != null) && (!blockingAnimation.isBlocking())) {
            blockingAnimation = null;
        }
    }

    private void startNextAnimation() {
        Animation animation = animationsQueue.removeFirst();
        playingAnimations.add(animation);
        animation.start();
        if (animation.isBlocking()) {
            blockingAnimation = animation;
        }
    }
}
