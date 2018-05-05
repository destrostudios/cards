package com.destrostudios.cards.backend.application;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.rules.Components;

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
        data.setComponent(card1, Components.Color.NEUTRAL);
        data.setComponent(card1, Components.CREATURE_CARD);
        data.setComponent(card1, Components.DISPLAY_NAME, "card100");
        int cost1 = data.createEntity();
        data.setComponent(cost1, Components.ManaAmount.WHITE, 3);
        data.setComponent(cost1, Components.ManaAmount.GREEN, 1);
        data.setComponent(card1, Components.COST_ENTITY, cost1);
        data.setComponent(card1, Components.ATTACK, 2);
        data.setComponent(card1, Components.HEALTH, 2);
        data.setComponent(card1, Components.OWNED_BY, player1);
        data.setComponent(card1, Components.BOARD);
        data.setComponent(card1, Components.CREATURE_ZONE, 0);

        int card2 = data.createEntity();
        data.setComponent(card2, Components.ENCHANTMENT_CARD);
        data.setComponent(card2, Components.DISPLAY_NAME, "card101");
        int cost2 = data.createEntity();
        data.setComponent(cost2, Components.ManaAmount.WHITE, 2);
        data.setComponent(card2, Components.COST_ENTITY, cost2);
        data.setComponent(card2, Components.OWNED_BY, player1);
        data.setComponent(card2, Components.BOARD);
        data.setComponent(card2, Components.ENCHANTMENT_ZONE, 0);

        int card3 = data.createEntity();
        data.setComponent(card3, Components.Color.BLUE);
        data.setComponent(card3, Components.Color.RED);
        data.setComponent(card3, Components.CREATURE_CARD);
        data.setComponent(card3, Components.DISPLAY_NAME, "Shyvana");
        int cost3 = data.createEntity();
        data.setComponent(cost3, Components.ManaAmount.RED, 1);
        data.setComponent(cost3, Components.ManaAmount.BLUE, 1);
        data.setComponent(card3, Components.COST_ENTITY, cost3);
        data.setComponent(card3, Components.ATTACK, 1);
        data.setComponent(card3, Components.HEALTH, 1);
        data.setComponent(card3, Components.DAMAGED);
        data.setComponent(card3, Components.Tribe.HUMAN);
        data.setComponent(card3, Components.Tribe.DRAGON);
        data.setComponent(card3, Components.Ability.TAUNT);
        int spell3 = data.createEntity();
        int spell3Cost = data.createEntity();
        data.setComponent(spell3Cost, Components.Cost.TAP);
        data.setComponent(spell3Cost, Components.ManaAmount.RED, 2);
        data.setComponent(spell3, Components.COST_ENTITY, spell3Cost);
        data.setComponent(card3, Components.SPELL_ENTITIES, new int[]{spell3});
        data.setComponent(card3, Components.FLAVOUR_TEXT, "\"I am op.\"");
        data.setComponent(card3, Components.OWNED_BY, player2);
        data.setComponent(card3, Components.BOARD);
        data.setComponent(card3, Components.CREATURE_ZONE, 0);

        int card4 = data.createEntity();
        data.setComponent(card4, Components.Color.NEUTRAL);
        data.setComponent(card4, Components.Color.WHITE);
        data.setComponent(card4, Components.Color.RED);
        data.setComponent(card4, Components.Color.GREEN);
        data.setComponent(card4, Components.Color.BLUE);
        data.setComponent(card4, Components.Color.BLACK);
        data.setComponent(card4, Components.CREATURE_CARD);
        data.setComponent(card4, Components.DISPLAY_NAME, "Aether Adept");
        int cost4 = data.createEntity();
        data.setComponent(cost4, Components.ManaAmount.NEUTRAL, 1);
        data.setComponent(card4, Components.COST_ENTITY, cost4);
        data.setComponent(card4, Components.ATTACK, 1);
        data.setComponent(card4, Components.HEALTH, 1);
        data.setComponent(card4, Components.Tribe.GOD);
        data.setComponent(card4, Components.Ability.CHARGE);
        data.setComponent(card4, Components.Ability.DIVINE_SHIELD);
        data.setComponent(card4, Components.Ability.HEXPROOF);
        data.setComponent(card4, Components.Ability.IMMUNE);
        data.setComponent(card4, Components.Ability.TAUNT);
        int spell4 = data.createEntity();
        int spell4Cost = data.createEntity();
        data.setComponent(spell4Cost, Components.ManaAmount.BLUE, 1);
        data.setComponent(spell4, Components.COST_ENTITY, spell4Cost);
        data.setComponent(card4, Components.SPELL_ENTITIES, new int[]{spell4});
        data.setComponent(card4, Components.OWNED_BY, player2);
        data.setComponent(card4, Components.BOARD);
        data.setComponent(card4, Components.CREATURE_ZONE, 1);
    }

    private void initLibraryAndHandCardsEntities(EntityData data, int player1, int player2, int handCards1, int handCards2) {
        int librarySize = 45;
        int handSize = 5;

        for (int i = 0; i < 2 * librarySize; i++) {
            int card = data.createEntity();
            data.setComponent(card, Components.Color.NEUTRAL);
            data.setComponent(card, i % 2 == 0 ? Components.CREATURE_CARD : Components.SPELL_CARD);
            data.setComponent(card, Components.DISPLAY_NAME, "card" + i);
            data.setComponent(card, Components.OWNED_BY, i < librarySize ? player1 : player2);
            data.setComponent(card, Components.LIBRARY, i % librarySize);
        }

        data.setComponent(handCards1, Components.OWNED_BY, player1);
        data.setComponent(handCards2, Components.OWNED_BY, player2);

        for (int i = 0; i < 2 * handSize; i++) {
            int card = data.createEntity();
            data.setComponent(card, Components.Color.NEUTRAL);
            data.setComponent(card, i % 2 == 0 ? Components.CREATURE_CARD : Components.SPELL_CARD);
            data.setComponent(card, Components.DISPLAY_NAME, "card" + i);
            data.setComponent(card, Components.OWNED_BY, i < handSize ? player1 : player2);
            data.setComponent(card, Components.HAND_CARDS, i % handSize);
        }
    }

    private void initPlayerAndHeroEntities(EntityData data, int player1, int player2, int hero1, int hero2) {
        data.setComponent(player1, Components.DISPLAY_NAME, "player1");
        data.setComponent(player2, Components.DISPLAY_NAME, "player2");
        data.setComponent(player1, Components.NEXT_PLAYER, player2);
        data.setComponent(player2, Components.NEXT_PLAYER, player1);

        data.setComponent(hero1, Components.DISPLAY_NAME, "hero1");
        data.setComponent(hero1, Components.HEALTH, 20);
        data.setComponent(hero1, Components.OWNED_BY, player1);

        data.setComponent(hero2, Components.DISPLAY_NAME, "hero2");
        data.setComponent(hero2, Components.HEALTH, 20);
        data.setComponent(hero2, Components.OWNED_BY, player2);
    }
}
