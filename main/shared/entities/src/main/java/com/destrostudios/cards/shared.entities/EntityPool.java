package com.destrostudios.cards.shared.entities;

import com.destrostudios.cards.shared.entities.collections.IntSet;
import java.util.Random;

/**
 *
 * @author Philipp
 */
public class EntityPool {

    private final Random random;
    private final IntSet entities = new IntSet();

    public EntityPool(Random random) {
        this.random = random;
    }

    public int create() {
        int entityKey;
        do {
            entityKey = random.nextInt();
        } while (entities.hasKey(entityKey));
        entities.set(entityKey);
        return entityKey;
    }

    public IntSet getEntities() {
        return entities;
    }

}
