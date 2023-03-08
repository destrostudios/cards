package com.destrostudios.cards.frontend.application;

import com.destrostudios.cards.frontend.application.appstates.PostFilterAppState;
import com.destrostudios.cards.frontend.application.appstates.menu.PlayAppState;
import com.destrostudios.cards.shared.files.FileAssets;
import com.destrostudios.gametools.network.client.ToolsClient;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.system.AppSettings;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.style.BaseStyles;
import lombok.Getter;

public class FrontendJmeApplication extends SimpleApplication {

    public FrontendJmeApplication(ToolsClient toolsClient) {
        this.toolsClient = toolsClient;
        loadSettings();
        setPauseOnLostFocus(false);
        setDisplayStatView(false);
    }
    @Getter
    private ToolsClient toolsClient;

    private void loadSettings(){
        settings = new AppSettings(true);
        settings.setTitle("Cards");
        settings.setWidth(1600);
        settings.setHeight(900);
        settings.setVSync(true);
        setShowSettings(false);
    }

    @Override
    public void simpleInitApp() {
        assetManager.registerLocator(FileAssets.ROOT, FileLocator.class);
        flyCam.setEnabled(false);
        stateManager.attach(new PostFilterAppState());
        stateManager.attach(new PlayAppState());
        // Lemur
        GuiGlobals.initialize(this);
        BaseStyles.loadGlassStyle();
        GuiGlobals.getInstance().getStyles().setDefaultStyle("glass");
    }

    public void enqueue(final Runnable runnable){
        enqueue(() -> {
            runnable.run();
            return null;
        });
    }
}