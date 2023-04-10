package com.destrostudios.cards.shared.rules.util;

import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestArrayUtil {

    @Test
    public void testGetAllSubsets() {
        List<Integer> list = new LinkedList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);

        List<int[]> subsets = ArrayUtil.getAllSubsets(list);

        int[][] expectedSubsets = new int[][] {
            new int[0],
            new int[] { 1 },
            new int[] { 1, 2 },
            new int[] { 1, 2, 3 },
            new int[] { 1, 2, 3, 4 },
            new int[] { 1, 2, 4 },
            new int[] { 1, 3 },
            new int[] { 1, 3, 4 },
            new int[] { 1, 4 },
            new int[] { 2 },
            new int[] { 2, 3 },
            new int[] { 2, 3, 4 },
            new int[] { 2, 4 },
            new int[] { 3 },
            new int[] { 3, 4 },
            new int[] { 4 },
        };
        assertEquals(expectedSubsets.length, subsets.size());
        for (int i = 0; i < expectedSubsets.length; i++) {
            assertArrayEquals(expectedSubsets[i], subsets.get(i));
        }
    }
}
