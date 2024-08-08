package com.destrostudios.cards.shared.rules.util;

import com.destrostudios.cards.shared.entities.IntList;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestArrayUtil {

    @Test
    public void testEqualsUnsortedAndUniqueTrue() {
        assertTrue(ArrayUtil.equalsUnsortedAndUnique(new int[] { 1, 2, 3, 4 }, new int[] { 3, 1, 2, 4 }));
    }

    @Test
    public void testEqualsUnsortedAndUniqueFalseDifferentLengths() {
        assertFalse(ArrayUtil.equalsUnsortedAndUnique(new int[] { 1, 2, 3, 4 }, new int[] { 3, 1, 2 }));
    }

    @Test
    public void testEqualsUnsortedAndUniqueFalseDifferentValues() {
        assertFalse(ArrayUtil.equalsUnsortedAndUnique(new int[] { 1, 2, 3, 4 }, new int[] { 3, 1, 2, 5 }));
    }

    @Test
    public void testGetAllSubsets() {
        IntList list = new IntList();
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

    @Test
    public void testGetSubsets() {
        IntList list = new IntList();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);

        List<IntList> subsets = ArrayUtil.getSubsets(list, 2);

        int[][] expectedSubsets = new int[][] {
            new int[] { 1, 2 },
            new int[] { 1, 3 },
            new int[] { 1, 4 },
            new int[] { 2, 3 },
            new int[] { 2, 4 },
            new int[] { 3, 4 },
        };
        assertEquals(expectedSubsets.length, subsets.size());
        for (int i = 0; i < expectedSubsets.length; i++) {
            assertArrayEquals(expectedSubsets[i], subsets.get(i).toArray());
        }
    }
}
