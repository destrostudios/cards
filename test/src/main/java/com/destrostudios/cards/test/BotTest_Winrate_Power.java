package com.destrostudios.cards.test;

import com.destrostudios.cards.shared.rules.PlayerInfo;

public class BotTest_Winrate_Power extends BotTest_Winrate {

    public static void main(String[] args) {
        new BotTest_Winrate_Power().run();
    }

    public BotTest_Winrate_Power() {
        super(false);
    }

    @Override
    protected String getPlayerKey(PlayerInfo playerInfo) {
        return playerInfo.getPowers()[0].getPath();
    }
}
