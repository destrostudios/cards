package com.destrostudios.cards.frontend.application.appstates.services.players;

import com.destrostudios.cardgui.TransformedBoardObject;

public class PlayerBoardObject extends TransformedBoardObject<PlayerModel> {

    public PlayerBoardObject() {
        super(new PlayerModel());
    }
}
