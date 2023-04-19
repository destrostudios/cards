package com.destrostudios.cards.backend.application.modules.bot;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.entities.SimpleEntityData;
import com.destrostudios.cards.shared.rules.Components;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestCardsBotEval {

    @Test
    public void testEval_WinPlayer1() {
        assertArrayEquals(CardsBotEval.eval(createDataWithPlayers(), 0, CardsBotEval.getDefaultWeights()), new float[] { 1, 0 });
    }

    @Test
    public void testEval_WinPlayer2() {
        assertArrayEquals(CardsBotEval.eval(createDataWithPlayers(), 1, CardsBotEval.getDefaultWeights()), new float[] { 0, 1 });
    }

    private static EntityData createDataWithPlayers() {
        EntityData Data = new SimpleEntityData(Components.ALL);
        Data.setComponent(0, Components.NEXT_PLAYER, 1);
        Data.setComponent(1, Components.NEXT_PLAYER, 0);
        return Data;
    }

    @Test
    public void testGetPlayerScore_OwnHealth_Higher() {
        assertTrue(getScore(getPlayer(p -> p.setPlayerHealth(2)), getPlayer()) > getScore(getPlayer(), getPlayer()));
    }

    @Test
    public void testGetPlayerScore_OpponentHealth_Lower() {
        assertTrue(getScore(getPlayer(), getPlayer()) > getScore(getPlayer(), getPlayer(p -> p.setPlayerHealth(2))));
    }

    @Test
    public void testGetPlayerScore_CreaturesOnBoard_OverEmpty() {
        assertTrue(getScore(getPlayer(p -> p.setCreaturesHealth(1)), getPlayer()) > getScore(getPlayer(), getPlayer()));
    }

    @Test
    public void testGetPlayerScore_CreaturesOnBoard_HigherAttack() {
        assertTrue(getScore(getPlayer(p -> {
            p.setCreaturesAttack(2);
            p.setCreaturesHealth(1);
        }), getPlayer()) > getScore(getPlayer(p -> {
            p.setCreaturesAttack(1);
            p.setCreaturesHealth(1);
        }), getPlayer()));
    }

    @Test
    public void testGetPlayerScore_CreaturesOnBoard_HigherHealth() {
        assertTrue(getScore(getPlayer(p -> {
            p.setCreaturesHealth(2);
        }), getPlayer()) > getScore(getPlayer(p -> {
            p.setCreaturesHealth(1);
        }), getPlayer()));
    }

    @Test
    public void testGetPlayerScore_OwnCardsInHand_Higher() {
        assertTrue(getScore(getPlayer(p -> p.setCardsInHand(1)), getPlayer()) > getScore(getPlayer(), getPlayer()));
    }

    @Test
    public void testGetPlayerScore_OpponentCardsInHand_Lower() {
        assertTrue(getScore(getPlayer(), getPlayer()) > getScore(getPlayer(), getPlayer(p -> p.setCardsInHand(1))));
    }

    @Test
    public void testGetPlayerScore_EqualScores_SameBoardDifference() {
        // 1/1+1/1 vs 1/1+1/1
        assertEquals(getScore(getPlayer(p -> {
            p.setCreaturesAttack(2);
            p.setCreaturesHealth(2);
        }), getPlayer(p -> {
            p.setCreaturesAttack(2);
            p.setCreaturesHealth(2);
        })), getScore(getPlayer(p -> {
            p.setCreaturesAttack(1);
            p.setCreaturesHealth(1);
        }), getPlayer(p -> {
            p.setCreaturesAttack(1);
            p.setCreaturesHealth(1);
        })));
    }

    @Test
    public void testGetPlayerScore_ClearEnemyBoardWithOwnCreaturesRemaining() {
        // 1/1+1/1 vs 2/1 -> Trade 1/1 into 2/1
        assertTrue(getScore(getPlayer(p -> {
            p.setCreaturesAttack(2);
            p.setCreaturesHealth(2);
        }), getPlayer(p -> {
            p.setCreaturesAttack(2);
            p.setCreaturesHealth(1);
        })) < getScore(getPlayer(p -> {
            p.setCreaturesAttack(1);
            p.setCreaturesHealth(1);
        }), getPlayer()));
    }

    @Test
    public void testGetPlayerScore_Trade_LowerStatsIntoHigherWithCreaturesRemaining() {
        // 1/1+1/1 vs 4/1+1/1 -> Trade 1/1 into 4/1
        assertTrue(getScore(getPlayer(p -> {
            p.setCreaturesAttack(2);
            p.setCreaturesHealth(2);
        }), getPlayer(p -> {
            p.setCreaturesAttack(4);
            p.setCreaturesHealth(2);
        })) < getScore(getPlayer(p -> {
            p.setCreaturesAttack(1);
            p.setCreaturesHealth(1);
        }), getPlayer(p -> {
            p.setCreaturesAttack(1);
            p.setCreaturesHealth(1);
        })));
    }

    private static float getScore(CardsBotEval.EvalPlayerInfo ownPlayerInfo, CardsBotEval.EvalPlayerInfo opponentPlayerInfo) {
        return CardsBotEval.getPlayerScore(ownPlayerInfo, opponentPlayerInfo, CardsBotEval.getDefaultWeights());
    }

    private static CardsBotEval.EvalPlayerInfo getPlayer(Consumer<CardsBotEval.EvalPlayerInfo> modify) {
        CardsBotEval.EvalPlayerInfo playerInfo = getPlayer();
        modify.accept(playerInfo);
        return playerInfo;
    }

    private static CardsBotEval.EvalPlayerInfo getPlayer() {
        return new CardsBotEval.EvalPlayerInfo(1, 0, 0, 0);
    }

    @Test
    public void testMapScoresToProbabilities_Equal() {
        assertArrayEquals(CardsBotEval.mapScoresToProbabilities(new float[] { 1, 1 }), new float[] { 0.5f, 0.5f });
    }

    @Test
    public void testMapScoresToProbabilities_BothPositive() {
        assertArrayEquals(CardsBotEval.mapScoresToProbabilities(new float[] { 1, 3 }), new float[] { 0.25f, 0.75f });
    }

    @Test
    public void testMapScoresToProbabilities_BothNegative() {
        assertArrayEquals(CardsBotEval.mapScoresToProbabilities(new float[] { -4, -1 }), new float[] { 0.2f, 0.8f });
    }

    @Test
    public void testMapScoresToProbabilities_OneNegativeOnePositive() {
        assertArrayEquals(CardsBotEval.mapScoresToProbabilities(new float[] { -3, 5 }), new float[] { 0.1f, 0.9f });
    }
}
