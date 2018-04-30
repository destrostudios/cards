package com.destrostudios.cards.network;

import com.destrostudios.cards.shared.entities.collections.IntArrayList;
import java.util.Random;

/**
 *
 * @author Philipp
 */
public class TrackedRandom {

    private final IntArrayList history = new IntArrayList();
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

    public IntArrayList getHistory() {
        return history;
    }

    public Random getRandom() {
        return random;
    }
}
