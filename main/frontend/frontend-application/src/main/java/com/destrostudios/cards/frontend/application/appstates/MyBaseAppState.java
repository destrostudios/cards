package com.destrostudios.cards.frontend.application.appstates;

import com.destrostudios.cards.frontend.application.FrontendJmeApplication;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppState;
import com.jme3.app.state.AppStateManager;

/**
 *
 * @author Carl
 */
public class MyBaseAppState extends AbstractAppState{

    public MyBaseAppState(){

    }
    protected FrontendJmeApplication mainApplication;

    @Override
    public void initialize(AppStateManager stateManager, Application application){
        super.initialize(stateManager, application);
        mainApplication = (FrontendJmeApplication) application;
    }

    protected <T extends AppState> T getAppState(Class<T> appStateClass){
        return mainApplication.getStateManager().getState(appStateClass);
    }
}