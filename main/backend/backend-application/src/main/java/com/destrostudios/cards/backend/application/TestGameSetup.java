package com.destrostudios.cards.backend.application;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
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
        initSimpleLandOnBoard(Components.Color.GREEN, "Forest", players[0], 0);
        initSimpleLandOnBoard(Components.Color.GREEN, "Forest", players[0], 1);
        initSimpleLandOnBoard(Components.Color.GREEN, "Forest", players[1], 0);
        initSimpleLandOnBoard(Components.Color.BLUE, "Island", players[1], 1);
        initSimpleLandOnBoard(Components.Color.BLUE, "Island", players[1], 2);

        int card1 = data.createEntity();
        data.setComponent(card1, Components.Color.NEUTRAL);
        data.setComponent(card1, Components.CREATURE_CARD);
        data.setComponent(card1, Components.DISPLAY_NAME, "card100");
        data.setComponent(card1, Components.ATTACK, 2);
        data.setComponent(card1, Components.HEALTH, 2);
        int playSpell1 = data.createEntity();
        data.setComponent(playSpell1, Components.Spell.CastCondition.FROM_HAND);
        data.setComponent(playSpell1, Components.Spell.Effect.ADD_TO_BOARD);
        int playSpell1Cost = data.createEntity();
        data.setComponent(playSpell1Cost, Components.ManaAmount.WHITE, 3);
        data.setComponent(playSpell1Cost, Components.ManaAmount.GREEN, 1);
        data.setComponent(playSpell1, Components.Spell.COST_ENTITY, playSpell1Cost);
        data.setComponent(card1, Components.SPELL_ENTITIES, new int[]{playSpell1});
        data.setComponent(card1, Components.OWNED_BY, players[0]);
        data.setComponent(card1, Components.BOARD);
        data.setComponent(card1, Components.CREATURE_ZONE, 0);

        int card2 = data.createEntity();
        data.setComponent(card2, Components.Color.RED);
        data.setComponent(card2, Components.ENCHANTMENT_CARD);
        data.setComponent(card2, Components.DISPLAY_NAME, "card101");
        int playSpell2 = data.createEntity();
        data.setComponent(playSpell2, Components.Spell.CastCondition.FROM_HAND);
        data.setComponent(playSpell2, Components.Spell.Effect.ADD_TO_BOARD);
        int playSpell2Cost = data.createEntity();
        data.setComponent(playSpell2Cost, Components.ManaAmount.WHITE, 2);
        data.setComponent(playSpell2, Components.Spell.COST_ENTITY, playSpell2Cost);
        data.setComponent(card2, Components.SPELL_ENTITIES, new int[]{playSpell2});
        data.setComponent(card2, Components.OWNED_BY, players[0]);
        data.setComponent(card2, Components.BOARD);
        data.setComponent(card2, Components.ENCHANTMENT_ZONE, 0);

        int card3 = data.createEntity();
        data.setComponent(card3, Components.Color.BLUE);
        data.setComponent(card3, Components.Color.RED);
        data.setComponent(card3, Components.CREATURE_CARD);
        data.setComponent(card3, Components.DISPLAY_NAME, "Shyvana");
        data.setComponent(card3, Components.ATTACK, 1);
        data.setComponent(card3, Components.HEALTH, 1);
        data.setComponent(card3, Components.DAMAGED);
        data.setComponent(card3, Components.Tribe.HUMAN);
        data.setComponent(card3, Components.Tribe.DRAGON);
        data.setComponent(card3, Components.Ability.TAUNT);
        int playSpell3 = data.createEntity();
        data.setComponent(playSpell3, Components.Spell.CastCondition.FROM_HAND);
        data.setComponent(playSpell3, Components.Spell.Effect.ADD_TO_BOARD);
        int playSpell3Cost = data.createEntity();
        data.setComponent(playSpell3Cost, Components.ManaAmount.RED, 1);
        data.setComponent(playSpell3Cost, Components.ManaAmount.BLUE, 1);
        data.setComponent(playSpell3, Components.Spell.COST_ENTITY, playSpell3Cost);
        int boardSpell3 = data.createEntity();
        int boardSpell3Cost = data.createEntity();
        data.setComponent(boardSpell3Cost, Components.Cost.TAP);
        data.setComponent(boardSpell3Cost, Components.ManaAmount.RED, 2);
        data.setComponent(boardSpell3, Components.Spell.COST_ENTITY, boardSpell3Cost);
        data.setComponent(card3, Components.SPELL_ENTITIES, new int[]{playSpell3, boardSpell3});
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
        data.setComponent(card4, Components.ATTACK, 1);
        data.setComponent(card4, Components.HEALTH, 1);
        data.setComponent(card4, Components.Tribe.GOD);
        data.setComponent(card4, Components.Ability.CHARGE);
        data.setComponent(card4, Components.Ability.DIVINE_SHIELD);
        data.setComponent(card4, Components.Ability.HEXPROOF);
        data.setComponent(card4, Components.Ability.IMMUNE);
        data.setComponent(card4, Components.Ability.TAUNT);
        int playSpell4 = data.createEntity();
        data.setComponent(playSpell4, Components.Spell.CastCondition.FROM_HAND);
        data.setComponent(playSpell4, Components.Spell.Effect.ADD_TO_BOARD);
        int playSpell4Cost = data.createEntity();
        data.setComponent(playSpell4Cost, Components.ManaAmount.NEUTRAL, 1);
        data.setComponent(playSpell4, Components.Spell.COST_ENTITY, playSpell4Cost);
        int boardSpell4 = data.createEntity();
        int boardSpell4Cost = data.createEntity();
        data.setComponent(boardSpell4Cost, Components.ManaAmount.BLUE, 1);
        data.setComponent(boardSpell4, Components.Spell.COST_ENTITY, boardSpell4Cost);
        data.setComponent(card4, Components.SPELL_ENTITIES, new int[]{playSpell4, boardSpell4});
        data.setComponent(card4, Components.OWNED_BY, players[1]);
        data.setComponent(card4, Components.BOARD);
        data.setComponent(card4, Components.CREATURE_ZONE, 1);
    }

    private int initSimpleLandOnBoard(ComponentDefinition<Void> colorComponent, String displayName, int ownerEntity, int zoneIndex) {
        int landCard = data.createEntity();
        data.setComponent(landCard, colorComponent);
        data.setComponent(landCard, Components.LAND_CARD);
        data.setComponent(landCard, Components.DISPLAY_NAME, displayName);
        int playSpell = data.createEntity();
        data.setComponent(playSpell, Components.Spell.CastCondition.FROM_HAND);
        data.setComponent(playSpell, Components.Spell.Effect.ADD_TO_BOARD);
        int tapSpell = data.createEntity();
        data.setComponent(tapSpell, Components.Spell.CastCondition.FROM_BOARD);
        int tapSpellCost = data.createEntity();
        data.setComponent(tapSpellCost, Components.Cost.TAP);
        data.setComponent(tapSpell, Components.Spell.COST_ENTITY, tapSpellCost);
        data.setComponent(landCard, Components.SPELL_ENTITIES, new int[]{playSpell, tapSpell});
        data.setComponent(landCard, Components.OWNED_BY, ownerEntity);
        data.setComponent(landCard, Components.BOARD);
        data.setComponent(landCard, Components.LAND_ZONE, zoneIndex);
        return landCard;
    }

    private void initLibraryAndHandCardsEntities(int handCards1, int handCards2) {
        int librarySize = 48;
        int handSize = 5;

        for (int i = 0; i < 2 * librarySize; i++) {
            int card = data.createEntity();
            data.setComponent(card, Components.Color.NEUTRAL);
            data.setComponent(card, i % 2 == 0 ? Components.CREATURE_CARD : Components.SPELL_CARD);
            data.setComponent(card, Components.DISPLAY_NAME, "card" + i);
            data.setComponent(card, Components.OWNED_BY, i < librarySize ? players[0] : players[1]);
            data.setComponent(card, Components.LIBRARY, i % librarySize);
            addSimplePlayFromHandSpell(card, 1);
        }

        data.setComponent(handCards1, Components.OWNED_BY, players[0]);
        data.setComponent(handCards2, Components.OWNED_BY, players[1]);

        for (int i = 0; i < 2 * handSize; i++) {
            int card = data.createEntity();
            data.setComponent(card, Components.Color.NEUTRAL);
            switch(i % 4) {
                case 0:
                    data.setComponent(card, Components.DISPLAY_NAME, "Creature #" + i);
                    data.setComponent(card, Components.CREATURE_CARD);
                    break;
                case 1:
                    data.setComponent(card, Components.DISPLAY_NAME, "Land #" + i);
                    data.setComponent(card, Components.LAND_CARD);
                    break;
                case 2:
                    data.setComponent(card, Components.DISPLAY_NAME, "Enchantment #" + i);
                    data.setComponent(card, Components.ENCHANTMENT_CARD);
                    break;
                case 3:
                    data.setComponent(card, Components.DISPLAY_NAME, "Spell #" + i);
                    data.setComponent(card, Components.SPELL_CARD);
                    break;
            }
            data.setComponent(card, Components.OWNED_BY, i < handSize ? players[0] : players[1]);
            data.setComponent(card, Components.HAND_CARDS, i % handSize);
            addSimplePlayFromHandSpell(card, 1);
        }
    }

    private void addSimplePlayFromHandSpell(int card, int neutralManaAmount) {
        int playSpell = data.createEntity();
        data.setComponent(playSpell, Components.Spell.CastCondition.FROM_HAND);
        if (!data.hasComponent(card, Components.SPELL_CARD)) {
            data.setComponent(playSpell, Components.Spell.Effect.ADD_TO_BOARD);
        }
        int playSpellCost = data.createEntity();
        data.setComponent(playSpellCost, Components.ManaAmount.NEUTRAL, neutralManaAmount);
        data.setComponent(playSpell, Components.Spell.COST_ENTITY, playSpellCost);
        data.setComponent(card, Components.SPELL_ENTITIES, new int[]{playSpell});
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
