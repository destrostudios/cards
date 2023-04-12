package com.destrostudios.cards.shared.entities;

public class MultiMapAggregator extends ListAggregator {

    public MultiMapAggregator(IntMap<?>[] intMaps) {
        super(createSharedKeysList(intMaps));
    }

    private static IntList createSharedKeysList(IntMap<?>[] intMaps) {
        IntList intList = new IntList(intMaps[0].size());
        intMaps[0].foreachKey(key -> {
            for (int i = 1; i < intMaps.length; i++) {
                if (!intMaps[i].hasKey(key)) {
                    return;
                }
            }
            intList.add(key);
        });
        return intList;
    }
}
