package com.destrostudios.cards.test;

public class BotTest_Single extends BotTest {

    public static void main(String[] args) {
        new BotTest_Single().run();
    }

    @Override
    public void run() {
        super.run();
        new BotGame(allCards, mode, queue, 123, true, (botSettings, player) -> {
            botSettings.maxThreads = 1;
        }).play();
    }
}
