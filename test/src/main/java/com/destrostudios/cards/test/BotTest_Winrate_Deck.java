package com.destrostudios.cards.test;

import com.destrostudios.cards.shared.rules.PlayerInfo;
import com.destrostudios.cards.shared.rules.StartGameInfo;

import java.util.Random;

public class BotTest_Winrate_Deck extends BotTest_Winrate {

    public static void main(String[] args) {
        new BotTest_Winrate_Deck().run();
    }

    public BotTest_Winrate_Deck() {
        super(false);
    }

    @Override
    protected StartGameInfo getStartGameInfo(Random random) {
        return getStartGameInfo_Brawl(random);
    }

    @Override
    protected String getPlayerKey(PlayerInfo playerInfo) {
        return playerInfo.getDeck().getName();
    }
}
