package com.destrostudios.cards.frontend.application.appstates;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.shadow.DirectionalLightShadowFilter;
import lombok.Getter;

public class LightAndShadowAppState extends MyBaseAppState {

    private AmbientLight ambientLight;
    @Getter
    private Vector3f lightDirection;
    private DirectionalLight directionalLight;
    private DirectionalLightShadowFilter shadowFilter;

    @Override
    public void initialize(AppStateManager stateManager, Application application) {
        super.initialize(stateManager, application);
        ambientLight = new AmbientLight(ColorRGBA.White.mult(0.4f));
        mainApplication.getRootNode().addLight(ambientLight);

        lightDirection = new Vector3f(1, -5, -1).normalizeLocal();
        directionalLight = new DirectionalLight(lightDirection, ColorRGBA.White.mult(1.1f));
        mainApplication.getRootNode().addLight(directionalLight);

        shadowFilter = new DirectionalLightShadowFilter(mainApplication.getAssetManager(), 4096, 3);
        shadowFilter.setLight(directionalLight);
        shadowFilter.setShadowIntensity(0.4f);
    }

    @Override
    public void cleanup() {
        super.cleanup();
        setShadowsEnabled(false);
        mainApplication.getRootNode().removeLight(directionalLight);
        mainApplication.getRootNode().removeLight(ambientLight);
    }

    public void setShadowsEnabled(boolean enabled) {
        if (enabled) {
            getAppState(PostFilterAppState.class).addFilter(shadowFilter);
        } else {
            getAppState(PostFilterAppState.class).removeFilter(shadowFilter);
        }
    }
}
