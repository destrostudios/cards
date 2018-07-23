package com.destrostudios.cards.shared.rules;

import com.destrostudios.cards.shared.rules.util.MixedManaAmount;

import java.util.LinkedList;
import java.util.List;

public class ManaPermutations {

    public static List<MixedManaAmount> generatePayablePermutations(MixedManaAmount existingManaAmount, MixedManaAmount costManaAmount) {
        LinkedList<MixedManaAmount> permutations = new LinkedList<>();

        int[] existingMana = toManaArray(existingManaAmount);
        int[] costMana = toManaArray(costManaAmount);

        boolean isEnoughNonNeutral = true;
        for (int i = 1; i < existingMana.length; i++) {
            if (existingMana[i] < costMana[i]) {
                isEnoughNonNeutral = false;
                break;
            }
        }
        if (isEnoughNonNeutral) {
            int[] fixedMana = new int[existingMana.length];
            for (int i = 0; i < existingMana.length; i++) {
                fixedMana[i] = Math.min(costMana[i], existingMana[i]);
            }
            addPermutations(permutations, existingMana, costMana, fixedMana, 1);
        }

        return permutations;
    }

    private static void addPermutations(LinkedList<MixedManaAmount> permutations, int[] existingMana, int[] costMana, int[] payedMana, int manaIndex) {
        int[] newPayedMana = copyArray(payedMana);
        int remainingNeutralMana = (costMana[0] - newPayedMana[0]);
        for (int i = 1; i < newPayedMana.length; i++) {
            int asNeutralPayedMana = (newPayedMana[i] - costMana[i]);
            if (asNeutralPayedMana > 0) {
                remainingNeutralMana -= asNeutralPayedMana;
            }
        }
        int payableMana = (existingMana[manaIndex] - newPayedMana[manaIndex]);
        for (int additionalMana = 0; additionalMana <= payableMana; additionalMana++) {
            if (remainingNeutralMana == 0) {
                permutations.add(toMixedManaAmount(newPayedMana));
                break;
            }
            else if (manaIndex < (existingMana.length - 1)) {
                addPermutations(permutations, existingMana, costMana, newPayedMana, manaIndex + 1);
            }
            newPayedMana[manaIndex]++;
            remainingNeutralMana--;
        }
    }

    private static int[] copyArray(int[] array) {
        int[] copy = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            copy[i] = array[i];
        }
        return copy;
    }

    private static int[] toManaArray(MixedManaAmount mixedManaAmount) {
        return new int[]{ mixedManaAmount.getNeutral(), mixedManaAmount.getWhite(), mixedManaAmount.getRed(), mixedManaAmount.getGreen(), mixedManaAmount.getBlue(), mixedManaAmount.getBlack() };
    }

    private static MixedManaAmount toMixedManaAmount(int[] mana) {
        return new MixedManaAmount(mana[0], mana[1], mana[2], mana[3], mana[4], mana[5]);
    }

    // TODO: Write more tests and move to unit test class
    public static void main(String[] args) {
        List<MixedManaAmount> result = generatePayablePermutations(
                new MixedManaAmount(1, 2, 3, 1, 0, 1),
                new MixedManaAmount(3, 1, 0, 1, 0, 0)
        );
        for (MixedManaAmount mixedManaAmount : result) {
            System.out.println(mixedManaAmount);
        }
    }
}
