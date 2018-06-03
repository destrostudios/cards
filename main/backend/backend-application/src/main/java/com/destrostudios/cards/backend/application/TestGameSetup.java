package com.destrostudios.cards.backend.application;

import com.destrostudios.cards.backend.application.templates.CardPool;
import com.destrostudios.cards.backend.application.templates.LandCards;
import com.destrostudios.cards.backend.application.templates.TestCards;
import com.destrostudios.cards.backend.application.templates.TestLibraries;
import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.rules.Components;
import java.util.concurrent.ThreadLocalRandom;

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

        initPlayers();
        initLibrary(players[0], TestLibraries.red(), 20);
        initLibrary(players[1], TestLibraries.red(), 20);
//        initLibraryAndHandCardsEntities();
//        initBoardCardsEntities();
    }

    private void initLibrary(int player, CardPool cards, int librarySize) {
        for (int i = 0; i < librarySize; i++) {
            int card = cards.selectRandomCard(ThreadLocalRandom.current()::nextInt).create(data);
            data.setComponent(card, Components.OWNED_BY, player);
            data.setComponent(card, Components.LIBRARY, i);
        }
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

        int card3 = TestCards.shyvana(data);
        data.setComponent(card3, Components.OWNED_BY, players[1]);
        data.setComponent(card3, Components.BOARD);
        data.setComponent(card3, Components.CREATURE_ZONE, 0);

        int card4 = TestCards.aetherAdept(data);

        data.setComponent(card4, Components.OWNED_BY, players[1]);
        data.setComponent(card4, Components.BOARD);
        data.setComponent(card4, Components.CREATURE_ZONE, 1);
    }

    private int initSimpleLandOnBoard(ComponentDefinition<Void> colorComponent, String displayName, int ownerEntity, int zoneIndex) {
        int landCard = LandCards.land(data, displayName, colorComponent);
        data.setComponent(landCard, Components.OWNED_BY, ownerEntity);
        data.setComponent(landCard, Components.BOARD);
        data.setComponent(landCard, Components.LAND_ZONE, zoneIndex);
        return landCard;
    }

    private void initLibraryAndHandCardsEntities() {
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

        for (int i = 0; i < 2 * handSize; i++) {
            int card = data.createEntity();
            data.setComponent(card, Components.Color.NEUTRAL);
            switch (i % 4) {
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

    private void initPlayers() {
        data.setComponent(players[0], Components.DISPLAY_NAME, "player1");
        data.setComponent(players[0], Components.HEALTH, 20);
        data.setComponent(players[0], Components.NEXT_PLAYER, players[1]);

        data.setComponent(players[1], Components.DISPLAY_NAME, "player2");
        data.setComponent(players[1], Components.HEALTH, 20);
        data.setComponent(players[1], Components.NEXT_PLAYER, players[0]);
    }

    public int[] getPlayers() {
        return players;
    }
}
