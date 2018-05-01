package com.destrostudios.cards.shared.rules;

import com.destrostudios.cards.shared.entities.EntityData;

/**
 *
 * @author Philipp
 */
public class TestGameSetup {

    public void testSetup(EntityData data) {
        int player1 = data.createEntity();
        int player2 = data.createEntity();
        int hero1 = data.createEntity();
        int hero2 = data.createEntity();
        int handCards1 = data.createEntity();
        int handCards2 = data.createEntity();

        initPlayerAndHeroEntities(data, player1, player2, hero1, hero2);
        initLibraryAndHandCardsEntities(data, player1, player2, handCards1, handCards2);
        initBoardCardsEntities(data, player1, player2);
    }

    private void initBoardCardsEntities(EntityData data, int player1, int player2) {
        int card1 = data.createEntity();
        int card2 = data.createEntity();

        data.set(card1, Components.DISPLAY_NAME, "card100");
        data.set(card1, Components.OWNED_BY, player1);
        data.set(card1, Components.BOARD, null);
        data.set(card1, Components.CREATURE_CARD, null);
        data.set(card1, Components.CREATURE_ZONE, 0);
        data.set(card1, Components.ATTACK, 2);
        data.set(card1, Components.HEALTH, 2);
        data.set(card2, Components.OWNED_BY, player1);
        data.set(card2, Components.DISPLAY_NAME, "card101");
        data.set(card2, Components.BOARD, null);
        data.set(card2, Components.ENCHANTMENT_CARD, null);
        data.set(card2, Components.ENCHANTMENT_ZONE, 0);

        int card3 = data.createEntity();
        int card4 = data.createEntity();

        data.set(card3, Components.DISPLAY_NAME, "card102");
        data.set(card3, Components.OWNED_BY, player2);
        data.set(card3, Components.BOARD, null);
        data.set(card3, Components.CREATURE_CARD, null);
        data.set(card3, Components.CREATURE_ZONE, 0);
        data.set(card3, Components.ATTACK, 1);
        data.set(card3, Components.HEALTH, 1);

        data.set(card4, Components.OWNED_BY, player2);
        data.set(card4, Components.DISPLAY_NAME, "card103");
        data.set(card4, Components.BOARD, null);
        data.set(card4, Components.CREATURE_CARD, null);
        data.set(card4, Components.CREATURE_ZONE, 1);
        data.set(card4, Components.ATTACK, 1);
        data.set(card4, Components.HEALTH, 1);
    }

    private void initLibraryAndHandCardsEntities(EntityData data, int player1, int player2, int handCards1, int handCards2) {
        int librarySize = 50;
        int handSize = 5;

        for (int i = 0; i < 2 * (librarySize - handSize); i++) {
            int card = data.createEntity();
            data.set(card, Components.DISPLAY_NAME, "card" + i);
            data.set(card, Components.OWNED_BY, i < (librarySize - handSize) ? player1 : player2);
            data.set(card, Components.LIBRARY, i % (librarySize - handSize));
            data.set(card, i % 2 == 0 ? Components.CREATURE_CARD : Components.SPELL_CARD, null);
        }

        data.set(handCards1, Components.OWNED_BY, player1);
        data.set(handCards2, Components.OWNED_BY, player2);

        for (int i = 0; i < 2 * handSize; i++) {
            int card = data.createEntity();
            data.set(card, Components.DISPLAY_NAME, "card" + i);
            data.set(card, Components.OWNED_BY, i < handSize ? player1 : player2);
            data.set(card, Components.HAND_CARDS, i % handSize);
            data.set(card, i % 2 == 0 ? Components.CREATURE_CARD : Components.SPELL_CARD, null);
        }

    }

    private void initPlayerAndHeroEntities(EntityData data, int player1, int player2, int hero1, int hero2) {
        data.set(player1, Components.DISPLAY_NAME, "player1");
        data.set(player2, Components.DISPLAY_NAME, "player2");
        data.set(player1, Components.NEXT_PLAYER, player2);
        data.set(player2, Components.NEXT_PLAYER, player1);

        data.set(hero1, Components.HEALTH, 20);
        data.set(hero1, Components.DISPLAY_NAME, "hero1");
        data.set(hero1, Components.OWNED_BY, player1);

        data.set(hero1, Components.HEALTH, 20);
        data.set(hero1, Components.DISPLAY_NAME, "hero2");
        data.set(hero1, Components.OWNED_BY, player2);
    }
}
