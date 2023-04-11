package com.destrostudios.cards.backend.application.modules.bot;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameContext;
import com.destrostudios.cards.shared.rules.util.StatsUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.function.Function;

public class CardsBotEval {

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    static class EvalPlayerInfo {
        int playerHealth;
        int creaturesAttack;
        int creaturesHealth;
        int cardsInHand;
    }

    private static final float WEIGHT_PLAYER_HEALTH = 1;
    private static final float WEIGHT_CREATURES_ATTACK = 2;
    private static final float WEIGHT_CREATURES_HEALTH = 3;
    private static final float WEIGHT_CARDS_IN_HAND = 0.25f;
    private static final float SUM_WEIGHTS = (WEIGHT_PLAYER_HEALTH + WEIGHT_CREATURES_ATTACK + WEIGHT_CREATURES_HEALTH + WEIGHT_CARDS_IN_HAND);

    public static float[] eval(CardsBotState cardsBotState) {
        GameContext gameContext = cardsBotState.getGameContext();
        return eval(gameContext.getData(), gameContext.getWinner());
    }

    private static float[] eval(EntityData data, Integer winner) {
        List<Integer> players = data.query(Components.NEXT_PLAYER).list();
        EvalPlayerInfo[] playerInfos = collectPlayerInfos(data, players);
        float[] scores = new float[players.size()];
        int i = 0;
        for (int player : players) {
            float score;
            if (winner != null) {
                score = ((winner == player) ? 1 : 0);
            } else {
                EvalPlayerInfo ownPlayerInfo = playerInfos[i];
                EvalPlayerInfo opponentPlayerInfo = playerInfos[(i == 0) ? 1 : 0];
                score = getPlayerScore(ownPlayerInfo, opponentPlayerInfo);
            }
            scores[i] = score;
            i++;
        }
        return scores;
    }

    static float getPlayerScore(EvalPlayerInfo ownPlayerInfo, EvalPlayerInfo opponentPlayerInfo) {
        float scorePlayerHealth = getWeightedScore(ownPlayerInfo, opponentPlayerInfo, EvalPlayerInfo::getPlayerHealth, WEIGHT_PLAYER_HEALTH);
        float scoreCreaturesAttack = getWeightedScore(ownPlayerInfo, opponentPlayerInfo, EvalPlayerInfo::getCreaturesAttack, WEIGHT_CREATURES_ATTACK);
        float scoreCreaturesHealth = getWeightedScore(ownPlayerInfo, opponentPlayerInfo, EvalPlayerInfo::getCreaturesHealth, WEIGHT_CREATURES_HEALTH);
        float scoreCardsInHand = getWeightedScore(ownPlayerInfo, opponentPlayerInfo, EvalPlayerInfo::getCardsInHand, WEIGHT_CARDS_IN_HAND);
        float sumScores = (scorePlayerHealth + scoreCreaturesAttack + scoreCreaturesHealth + scoreCardsInHand);
        return (sumScores / SUM_WEIGHTS);
    }

    private static EvalPlayerInfo[] collectPlayerInfos(EntityData data, List<Integer> players) {
        EvalPlayerInfo[] playerInfos = new EvalPlayerInfo[players.size()];
        int i = 0;
        for (int player : players) {
            EvalPlayerInfo playerInfo = new EvalPlayerInfo();
            playerInfo.playerHealth = StatsUtil.getEffectiveHealth(data, player);
            playerInfos[i] = playerInfo;
            i++;
        }
        List<Integer> cardsOnBoard = data.query(Components.CREATURE_ZONE).list();
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
        List<Integer> cardsInHand = data.query(Components.HAND).list();
        for (int card : cardsInHand) {
            int owner = data.getComponent(card, Components.OWNED_BY);
            playerInfos[owner].cardsInHand++;
        }
        return playerInfos;
    }

    private static float getWeightedScore(EvalPlayerInfo ownPlayerInfo, EvalPlayerInfo opponentPlayerInfo, Function<EvalPlayerInfo, Integer> getValue, float weight) {
        float value = getValue.apply(ownPlayerInfo);
        float sum = value + getValue.apply(opponentPlayerInfo);
        return getWeightedScore(value, sum, weight);
    }

    private static float getWeightedScore(float value, float sum, float weight) {
        return weight * ((sum != 0) ? (value / sum) : 0.5f);
    }
}
