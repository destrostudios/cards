package com.destrostudios.cards.frontend.application.appstates.services;

import com.destrostudios.cardgui.Animation;
import com.jme.effekseer.EffekseerEmitterControl;
import com.jme3.scene.Node;

public class EffekseerAnimation extends Animation {

    public EffekseerAnimation(Node node, EffekseerEmitterControl effect) {
        this.node = node;
        this.effect = effect;
    }
    private Node node;
    private EffekseerEmitterControl effect;
    private boolean initialized;

    @Override
    public void start() {
        super.start();
        node.addControl(effect);
    }

    @Override
    public void update(float lastTimePerFrame) {
        super.update(lastTimePerFrame);
        if (!initialized && hasHandles()) {
            initialized = true;
        }
    }

    @Override
    public boolean isFinished() {
        return (initialized && !hasHandles());
    }

    private boolean hasHandles() {
        return (effect.getHandles().size() > 0);
    }
}
