package com.destrostudios.cards.backend.application.templates;

/**
 *
 * @author Philipp
 */
public class TestLibraries {

    public static CardPool red() {
        CardPool cards = new CardPool();
        cards.put(StandardCards.Red::goblin, 5);
        cards.put(StandardCards.Red::orc, 4);
        cards.put(StandardCards.Red::ogre, 3);
        cards.put(StandardCards.Red::giant, 2);
        cards.put(StandardCards.Red::dragon, 1);
        cards.put(StandardCards.Red::camel, 3);
        cards.put(StandardCards.Red::lightningBolt, 5);
        return cards;
    }
}
