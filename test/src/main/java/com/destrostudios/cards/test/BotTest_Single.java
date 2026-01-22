package com.destrostudios.cards.test;

import com.destrostudios.cards.backend.application.botgame.BotGame;

import java.util.Random;

public class BotTest_Single extends BotTest {

    public static void main(String[] args) {
        new BotTest_Single().run();
    }

    @Override
    public void run() {
        super.run();
        new BotGame(allCards, getStartGameInfo_CompletelyRandom(new Random()), 123, true, false, (botSettings, player) -> {}).play();
    }
}
