package com.destrostudios.cards.frontend.application.appstates;

public class VisualBoardAppState extends MyBaseAppState {

    public VisualBoardAppState(int playerIndex) {
        this.playerIndex = playerIndex;
    }
    protected int playerIndex;
}
