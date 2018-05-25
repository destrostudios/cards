package com.destrostudios.cards.shared.network;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Philipp
 */
public class TrackedRandom {

    private final List<Integer> history = new ArrayList<>();
    private final Random random;

    public TrackedRandom(Random random) {
        this.random = random;
    }

    public int nextInt() {
        int result = random.nextInt();
        history.add(result);
        return result;
    }

    public int nextInt(int bound) {
        int result = random.nextInt(bound);
        history.add(result);
        return result;
    }

    public List<Integer> getHistory() {
        return history;
    }

    public Random getRandom() {
        return random;
    }
}
