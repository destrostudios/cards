package com.destrostudios.cards.frontend.application.appstates;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Quad;

public class BackgroundAppState extends MyBaseAppState {

    public BackgroundAppState(String texturePath) {
        this.texturePath = texturePath;
    }
    private String texturePath;
    private ViewPort preViewPort;

    @Override
    public void initialize(AppStateManager stateManager, Application application){
        super.initialize(stateManager, application);
        int width = mainApplication.getContext().getSettings().getWidth();
        int height = mainApplication.getContext().getSettings().getHeight();

        Geometry geometry = new Geometry("background", new Quad(width, height));
        Material material = new Material(mainApplication.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        material.setTexture("ColorMap", mainApplication.getAssetManager().loadTexture(texturePath));
        geometry.setMaterial(material);
        geometry.setQueueBucket(RenderQueue.Bucket.Gui);

        preViewPort = mainApplication.getRenderManager().createPreView("background", mainApplication.getCamera());
        preViewPort.setClearColor(true);
        preViewPort.attachScene(geometry);
        geometry.updateGeometricState();
        preViewPort.setBackgroundColor(ColorRGBA.Red);
        mainApplication.getViewPort().setClearColor(false);
    }

    @Override
    public void cleanup() {
        super.cleanup();
        mainApplication.getRenderManager().removePreView(preViewPort);
        mainApplication.getViewPort().setClearColor(true);
    }
}