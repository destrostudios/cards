package com.destrostudios.cards.shared.entities;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

/**
 *
 * @author Philipp
 */
public class QueryBuilder {

    private Integer componentKey;
    private final List<Predicate<EntityValue>> predicates = new ArrayList<>();
    private Comparator<EntityValue> comparator;

    public QueryBuilder from(int componentKey) {
        this.componentKey = componentKey;
        return this;
    }

    public QueryBuilder where(Predicate<EntityValue> predicate) {
        predicates.add(predicate);
        return this;
    }

    public QueryBuilder orderBy(Comparator<EntityValue> comparator) {
        this.comparator = comparator;
        return this;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public Query build() {
        Comparator<EntityValue> c;
        if (comparator != null) {
            c = comparator.thenComparingInt(EntityValue::getEntity);
        } else {
            c = Comparator.comparingInt(EntityValue::getEntity);
        }
        return new Query(componentKey, c, predicates.toArray(new Predicate[predicates.size()]));
    }

}
