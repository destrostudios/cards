package com.destrostudios.cards.frontend.application.appstates;

import com.destrostudios.cards.frontend.application.appstates.services.cardpainter.model.CardModel;
import com.destrostudios.cards.shared.rules.util.ArrayUtil;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TargetsScrollSelectorAppState extends ScrollCardSelectorAppState {

    public TargetsScrollSelectorAppState(String description, List<int[]> validTargets, Function<Integer, CardModel> getCardModel, Runnable onCancel, Consumer<int[]> onSubmit) {
        super(description, onCancel);
        this.validTargets = validTargets;
        this.getCardModel = getCardModel;
        this.onSubmit = onSubmit;
    }
    private List<int[]> validTargets;
    private Function<Integer, CardModel> getCardModel;
    private Consumer<int[]> onSubmit;
    private HashMap<CardModel, Integer> cardModelsToTargets;

    @Override
    protected List<CardModel> createCardModels() {
        HashSet<Integer> allTargets = new HashSet<>();
        for (int[] targets : validTargets) {
            for (int target : targets) {
                allTargets.add(target);
            }
        }

        cardModelsToTargets = new HashMap<>();
        return allTargets.stream()
                .map(target -> {
                    CardModel cardModel = new CardModel();
                    cardModel.set(getCardModel.apply(target));
                    cardModel.setPlayable(false);
                    cardModelsToTargets.put(cardModel, target);
                    return cardModel;
                })
                .collect(Collectors.toList());
    }

    @Override
    protected boolean isValidSelection(List<CardModel> selectedCardModels) {
        int[] selectedTargets = getSelectedTargets();
        return validTargets.stream().anyMatch(targets -> ArrayUtil.equalsUnsortedAndUnique(targets, selectedTargets));
    }

    @Override
    protected void onSubmit() {
        super.onSubmit();
        onSubmit.accept(getSelectedTargets());
    }

    private int[] getSelectedTargets() {
        List<CardModel> selectedCardModels = getSelectedCardModels();
        int[] selectedTargets = new int[selectedCardModels.size()];
        int i = 0;
        for (CardModel selectedCardModel : selectedCardModels) {
            selectedTargets[i] = cardModelsToTargets.get(selectedCardModel);
            i++;
        }
        return selectedTargets;
    }
}
