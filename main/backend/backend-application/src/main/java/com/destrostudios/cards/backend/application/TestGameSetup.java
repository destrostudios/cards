package com.destrostudios.cards.backend.application;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.rules.Components;

/**
 *
 * @author Philipp
 */
public class TestGameSetup {
    
    private final EntityData data;
    private final int[] players = new int[2];

    public TestGameSetup(EntityData data) {
        this.data = data;
    }

    public void apply() {
        players[0] = data.createEntity();
        players[1] = data.createEntity();
        int hero1 = data.createEntity();
        int hero2 = data.createEntity();
        int handCards1 = data.createEntity();
        int handCards2 = data.createEntity();

        initPlayerAndHeroEntities(hero1, hero2);
        initLibraryAndHandCardsEntities(handCards1, handCards2);
        initBoardCardsEntities();
    }

    private void initBoardCardsEntities() {
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
        data.setComponent(card1, Components.OWNED_BY, players[0]);
        data.setComponent(card1, Components.BOARD);
        data.setComponent(card1, Components.CREATURE_ZONE, 0);

        int card2 = data.createEntity();
        data.setComponent(card2, Components.ENCHANTMENT_CARD);
        data.setComponent(card2, Components.DISPLAY_NAME, "card101");
        int cost2 = data.createEntity();
        data.setComponent(cost2, Components.ManaAmount.WHITE, 2);
        data.setComponent(card2, Components.COST_ENTITY, cost2);
        data.setComponent(card2, Components.OWNED_BY, players[0]);
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
        data.setComponent(card3, Components.OWNED_BY, players[1]);
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
        data.setComponent(card4, Components.OWNED_BY, players[1]);
        data.setComponent(card4, Components.BOARD);
        data.setComponent(card4, Components.CREATURE_ZONE, 1);
    }

    private void initLibraryAndHandCardsEntities(int handCards1, int handCards2) {
        int librarySize = 45;
        int handSize = 5;

        for (int i = 0; i < 2 * librarySize; i++) {
            int card = data.createEntity();
            data.setComponent(card, Components.Color.NEUTRAL);
            data.setComponent(card, i % 2 == 0 ? Components.CREATURE_CARD : Components.SPELL_CARD);
            data.setComponent(card, Components.DISPLAY_NAME, "card" + i);
            data.setComponent(card, Components.OWNED_BY, i < librarySize ? players[0] : players[1]);
            data.setComponent(card, Components.LIBRARY, i % librarySize);
        }

        data.setComponent(handCards1, Components.OWNED_BY, players[0]);
        data.setComponent(handCards2, Components.OWNED_BY, players[1]);

        for (int i = 0; i < 2 * handSize; i++) {
            int card = data.createEntity();
            data.setComponent(card, Components.Color.NEUTRAL);
            data.setComponent(card, i % 2 == 0 ? Components.CREATURE_CARD : Components.SPELL_CARD);
            data.setComponent(card, Components.DISPLAY_NAME, "card" + i);
            data.setComponent(card, Components.OWNED_BY, i < handSize ? players[0] : players[1]);
            data.setComponent(card, Components.HAND_CARDS, i % handSize);
        }
    }

    private void initPlayerAndHeroEntities(int hero1, int hero2) {
        data.setComponent(players[0], Components.DISPLAY_NAME, "player1");
        data.setComponent(players[1], Components.DISPLAY_NAME, "player2");
        data.setComponent(players[0], Components.NEXT_PLAYER, players[1]);
        data.setComponent(players[1], Components.NEXT_PLAYER, players[0]);

        data.setComponent(hero1, Components.DISPLAY_NAME, "hero1");
        data.setComponent(hero1, Components.HEALTH, 20);
        data.setComponent(hero1, Components.OWNED_BY, players[0]);

        data.setComponent(hero2, Components.DISPLAY_NAME, "hero2");
        data.setComponent(hero2, Components.HEALTH, 20);
        data.setComponent(hero2, Components.OWNED_BY, players[1]);
    }

    public int[] getPlayers() {
        return players;
    }
}
