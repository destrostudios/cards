package com.destrostudios.cards.frontend.application;

import com.destrostudios.cards.frontend.application.appstates.IngameAppState;
import com.destrostudios.cards.shared.files.FileAssets;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.system.AppSettings;

public class FrontendJmeApplication extends SimpleApplication {

    public FrontendJmeApplication(SimpleGameClient gameClient) {
        this.gameClient = gameClient;
        loadSettings();
        setPauseOnLostFocus(false);
    }
    private SimpleGameClient gameClient;

    private void loadSettings(){
        settings = new AppSettings(true);
        settings.setWidth(1600);
        settings.setHeight(900);
        settings.setTitle("Cards");
        setShowSettings(false);
    }

    @Override
    public void simpleInitApp() {
        assetManager.registerLocator(FileAssets.ROOT, FileLocator.class);
        stateManager.attach(new IngameAppState(gameClient));
    }

    public void enqueue(final Runnable runnable){
        enqueue(() -> {
            runnable.run();
            return null;
        });
    }

    public AppSettings getSettings() {
        return settings;
    }
}