package com.destrostudios.cards.backend.application.modules.bot;

import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestCardsBotEval {

    @Test
    public void testOwnHealth_Higher() {
        assertTrue(getScore(getPlayer(p -> p.setPlayerHealth(2)), getPlayer()) > getScore(getPlayer(), getPlayer()));
    }

    @Test
    public void testOpponentHealth_Lower() {
        assertTrue(getScore(getPlayer(), getPlayer()) > getScore(getPlayer(), getPlayer(p -> p.setPlayerHealth(2))));
    }

    @Test
    public void testCreaturesOnBoard_OverEmpty() {
        assertTrue(getScore(getPlayer(p -> p.setCreaturesHealth(1)), getPlayer()) > getScore(getPlayer(), getPlayer()));
    }

    @Test
    public void testCreaturesOnBoard_HigherAttack() {
        assertTrue(getScore(getPlayer(p -> {
            p.setCreaturesAttack(2);
            p.setCreaturesHealth(1);
        }), getPlayer()) > getScore(getPlayer(p -> {
            p.setCreaturesAttack(1);
            p.setCreaturesHealth(1);
        }), getPlayer()));
    }

    @Test
    public void testCreaturesOnBoard_HigherHealth() {
        assertTrue(getScore(getPlayer(p -> {
            p.setCreaturesHealth(2);
        }), getPlayer()) > getScore(getPlayer(p -> {
            p.setCreaturesHealth(1);
        }), getPlayer()));
    }

    @Test
    public void testOwnCardsInHand_Higher() {
        assertTrue(getScore(getPlayer(p -> p.setCardsInHand(1)), getPlayer()) > getScore(getPlayer(), getPlayer()));
    }

    @Test
    public void testOpponentCardsInHand_Lower() {
        assertTrue(getScore(getPlayer(), getPlayer()) > getScore(getPlayer(), getPlayer(p -> p.setCardsInHand(1))));
    }

    @Test
    public void testEqualScores_SameBoardDifference() {
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
    public void testTrade_ClearEnemyBoardWithOwnCreaturesRemaining() {
        // 1/1+1/1 vs 2/1
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
    public void testTrade_LowerStatsIntoHigherWithCreaturesRemaining() {
        // 1/1+1/1 vs 4/1+1/1
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
}
