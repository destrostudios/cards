package com.destrostudios.cards.shared.entities;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

/**
 *
 * @author Philipp
 */
public class Query {

    private final int componentKey;
    private final Predicate<EntityValue>[] predicates;
    private final Comparator<EntityValue> comparator;
    private final List<EntityValue> list = new ArrayList<>();
    private int count;

    public Query(int componentKey, Comparator<EntityValue> comparator, Predicate<EntityValue>[] predicates) {
        this.componentKey = componentKey;
        this.predicates = predicates;
        this.comparator = comparator;
    }

    public List<EntityValue> fetch(EntityData data) {
        count = 0;
        data.foreachEntityWithComponent(componentKey, this::offer);
        List<EntityValue> result = list.subList(0, count);
        result.sort(comparator);
        return result;
    }

    private void offer(int key, int value) {
        EntityValue keyValue;
        if (count == list.size()) {
            keyValue = new EntityValue();
            list.add(keyValue);
        } else {
            keyValue = list.get(count);
        }
        keyValue.setEntity(key);
        keyValue.setValue(value);
        for (Predicate<EntityValue> predicate : predicates) {
            if (!predicate.test(keyValue)) {
                return;
            }
        }
        count++;
    }

}
