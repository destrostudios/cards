package com.destrostudios.cards.backend.application.templates;

import java.util.HashMap;
import java.util.Map;
import java.util.function.IntUnaryOperator;

/**
 *
 * @author Philipp
 */
public class CardPool {

    private final Map<EntityCreator, Integer> weightedCards = new HashMap<>();

    public void put(EntityCreator card, int weight) {
        weightedCards.put(card, weight);
    }

    public EntityCreator selectRandomCard(IntUnaryOperator random) {
        int sum = 0;
        EntityCreator selected = null;
        for (Map.Entry<EntityCreator, Integer> entry : weightedCards.entrySet()) {
            int weight = entry.getValue();
            sum += weight;
            if (random.applyAsInt(sum) < weight) {
                selected = entry.getKey();
            }
        }
        return selected;
    }
}
