package com.destrostudios.cards.backend.application.modules.bot;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.entities.IntList;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameContext;
import com.destrostudios.cards.shared.rules.util.StatsUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.function.Function;

public class CardsBotEval {

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class Weights {
        public float playerHealth;
        public float playerHealthPortion;
        public float creaturesAttack;
        public float creaturesAttackPortion;
        public float creaturesHealth;
        public float creaturesHealthPortion;
        public float cardsInHand;
        public float cardsInHandPortion;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    static class EvalPlayerInfo {
        private int playerHealth;
        private int creaturesAttack;
        private int creaturesHealth;
        private int cardsInHand;
    }

    public static Weights getDefaultWeights() {
        return new Weights(
            0.2f,
            1,
            0.2f,
            3,
            0.2f,
            4,
            0.1f,
            0.25f
        );
    }

    public static float[] eval(CardsBotState cardsBotState) {
        return eval(cardsBotState, getDefaultWeights());
    }

    public static float[] eval(CardsBotState cardsBotState, Weights weights) {
        GameContext gameContext = cardsBotState.getGameContext();
        return eval(gameContext.getData(), gameContext.getWinner(), weights);
    }

    public static float[] eval(EntityData data, Integer winner, Weights weights) {
        IntList players = data.list(Components.NEXT_PLAYER);
        if (winner != null) {
            float[] probabilities = new float[players.size()];
            probabilities[winner] = 1;
            return probabilities;
        }
        EvalPlayerInfo[] playerInfos = collectPlayerInfos(data, players);
        float[] scores = new float[players.size()];
        for (int i = 0; i < scores.length; i++) {
            EvalPlayerInfo ownPlayerInfo = playerInfos[i];
            EvalPlayerInfo opponentPlayerInfo = playerInfos[(i == 0) ? 1 : 0];
            scores[i] = getPlayerScore(ownPlayerInfo, opponentPlayerInfo, weights);
            i++;
        }
        return mapScoresToProbabilities(scores);
    }

    public static float[] mapScoresToProbabilities(float[] scores) {
        float gapLeftAndRight = 1;
        float lower = Math.min(scores[0], scores[1]) - gapLeftAndRight;
        float upper = Math.max(scores[0], scores[1]) + gapLeftAndRight;
        float bounds = Math.abs(upper - lower);
        return new float[] {
            ((scores[0] - lower) / bounds),
            ((scores[1] - lower) / bounds),
        };
    }

    private static EvalPlayerInfo[] collectPlayerInfos(EntityData data, IntList players) {
        EvalPlayerInfo[] playerInfos = new EvalPlayerInfo[players.size()];
        int i = 0;
        for (int player : players) {
            EvalPlayerInfo playerInfo = new EvalPlayerInfo();
            playerInfo.playerHealth = StatsUtil.getEffectiveHealth(data, player);
            playerInfos[i] = playerInfo;
            i++;
        }
        IntList cardsOnBoard = data.list(Components.Zone.CREATURE_ZONE);
        for (int card : cardsOnBoard) {
            int owner = data.getComponent(card, Components.OWNED_BY);
            Integer attack = StatsUtil.getEffectiveAttack(data, card);
            Integer health = StatsUtil.getEffectiveHealth(data, card);
            if (attack != null) {
                playerInfos[owner].creaturesAttack += attack;
            }
            if (health != null) {
                playerInfos[owner].creaturesHealth += health;
            }
        }
        IntList cardsInHand = data.list(Components.Zone.HAND);
        for (int card : cardsInHand) {
            int owner = data.getComponent(card, Components.OWNED_BY);
            playerInfos[owner].cardsInHand++;
        }
        return playerInfos;
    }

    static float getPlayerScore(EvalPlayerInfo ownPlayerInfo, EvalPlayerInfo opponentPlayerInfo, Weights weights) {
        float score = 0;
        score += weights.playerHealth * (ownPlayerInfo.playerHealth - opponentPlayerInfo.playerHealth);
        score += weights.playerHealthPortion * getPortion(ownPlayerInfo, opponentPlayerInfo, EvalPlayerInfo::getPlayerHealth);
        score += weights.creaturesAttack * (ownPlayerInfo.creaturesAttack - opponentPlayerInfo.creaturesAttack);
        score += weights.creaturesAttackPortion * getPortion(ownPlayerInfo, opponentPlayerInfo, EvalPlayerInfo::getCreaturesAttack);
        score += weights.creaturesHealth * (ownPlayerInfo.creaturesHealth - opponentPlayerInfo.creaturesHealth);
        score += weights.creaturesHealthPortion * getPortion(ownPlayerInfo, opponentPlayerInfo, EvalPlayerInfo::getCreaturesHealth);
        score += weights.cardsInHand * (ownPlayerInfo.cardsInHand - opponentPlayerInfo.cardsInHand);
        score += weights.cardsInHandPortion * getPortion(ownPlayerInfo, opponentPlayerInfo, EvalPlayerInfo::getCardsInHand);
        return score;
    }

    private static float getPortion(EvalPlayerInfo ownPlayerInfo, EvalPlayerInfo opponentPlayerInfo, Function<EvalPlayerInfo, Integer> getValue) {
        float value = getValue.apply(ownPlayerInfo);
        float sum = value + getValue.apply(opponentPlayerInfo);
        return getPortion(value, sum);
    }

    private static float getPortion(float value, float sum) {
        return ((sum != 0) ? (value / sum) : 0.5f);
    }
}
