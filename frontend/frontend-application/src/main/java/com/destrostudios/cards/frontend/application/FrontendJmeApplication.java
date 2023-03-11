package com.destrostudios.cards.frontend.application;

import com.destrostudios.cards.frontend.application.appstates.BackgroundAppState;
import com.destrostudios.cards.frontend.application.appstates.LightAndShadowAppState;
import com.destrostudios.cards.frontend.application.appstates.LoadingAppState;
import com.destrostudios.cards.frontend.application.appstates.PostFilterAppState;
import com.destrostudios.cards.frontend.application.appstates.menu.MainMenuAppState;
import com.destrostudios.cards.frontend.application.modules.GameDataClientModule;
import com.destrostudios.cards.shared.files.FileAssets;
import com.destrostudios.gametools.network.client.ToolsClient;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.system.AppSettings;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.style.BaseStyles;
import lombok.Getter;

import java.util.concurrent.atomic.AtomicBoolean;

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
        stateManager.attach(new BackgroundAppState());
        stateManager.attach(new PostFilterAppState());
        stateManager.attach(new LightAndShadowAppState());
        GuiGlobals.initialize(this);

        AtomicBoolean asyncInitDone = new AtomicBoolean(false);
        initAsync(asyncInitDone);
        stateManager.attach(new LoadingAppState() {

            @Override
            protected boolean shouldClose() {
                return (getModule(GameDataClientModule.class).getUser() != null) && asyncInitDone.get();
            }

            @Override
            protected void close() {
                super.close();
                stateManager.attach(new MainMenuAppState());
            }
        });
    }

    private void initAsync(AtomicBoolean asyncInitDone) {
        new Thread(() -> {
            BaseStyles.loadGlassStyle();
            GuiGlobals.getInstance().getStyles().setDefaultStyle("glass");
            asyncInitDone.set(true);
        }).start();
    }

    public void enqueue(final Runnable runnable){
        enqueue(() -> {
            runnable.run();
            return null;
        });
    }
}