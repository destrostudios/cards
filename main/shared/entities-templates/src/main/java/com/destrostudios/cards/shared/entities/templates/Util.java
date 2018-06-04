package com.destrostudios.cards.shared.entities.templates;

import java.util.Collection;
import java.util.Iterator;

public class Util {

    public static int[] convertToArray_Integer(Collection<Integer> collection){
        int[] array = new int[collection.size()];
        Iterator<Integer> iterator = collection.iterator();
        for(int i=0;i<array.length;i++){
            array[i] = iterator.next();
        }
        return array;
    }
}
