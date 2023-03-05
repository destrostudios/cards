package com.destrostudios.cards.frontend.application.modules;

import com.destrostudios.cards.shared.model.Card;
import com.destrostudios.cards.shared.model.Mode;
import com.destrostudios.cards.shared.model.User;
import com.destrostudios.cards.shared.network.messages.OwnUserCardListsMessage;
import com.destrostudios.cards.shared.network.messages.GameDataMessage;
import com.destrostudios.gametools.network.shared.modules.NetworkModule;
import com.esotericsoftware.kryonet.Connection;
import lombok.Getter;

import java.util.List;

@Getter
public class GameDataClientModule extends NetworkModule {

    private List<Mode> modes;
    private List<Card> cards;
    private User ownUser;

    @Override
    public void received(Connection connection, Object object) {
        if (object instanceof GameDataMessage gameDataMessage) {
            modes = gameDataMessage.getModes();
            cards = gameDataMessage.getCards();
            ownUser = gameDataMessage.getOwnUser();
        } else if (object instanceof OwnUserCardListsMessage ownUserCardListsMessage) {
            System.out.println(ownUserCardListsMessage);
        }
    }
}
