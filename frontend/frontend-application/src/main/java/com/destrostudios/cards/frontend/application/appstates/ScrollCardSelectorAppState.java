package com.destrostudios.cards.frontend.application.appstates;

import com.destrostudios.cardgui.samples.tools.scrollselector.ScrollSelectorAppState;
import com.destrostudios.cardgui.samples.tools.scrollselector.ScrollSelectorSettings;
import com.destrostudios.cardgui.zones.SimpleIntervalZone;
import com.destrostudios.cards.frontend.application.appstates.services.BoardUtil;
import com.destrostudios.cards.frontend.application.appstates.services.IngameCardVisualizer;
import com.destrostudios.cards.frontend.application.appstates.services.cardpainter.model.CardModel;
import com.destrostudios.cards.frontend.application.gui.GuiUtil;
import com.jme3.math.Vector3f;
import com.simsilica.lemur.Button;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class ScrollCardSelectorAppState extends CardSelectorAppState {

    public ScrollCardSelectorAppState(String description, List<int[]> validTargets, Function<Integer, CardModel> getCardModel, Runnable onCancel, Consumer<int[]> onSubmit) {
        super(description, validTargets, getCardModel, onCancel, onSubmit);
    }
    private ScrollSelectorAppState<CardModel> scrollSelectorAppState;

    @Override
    protected void initSelector(List<CardModel> cardModels) {
        ScrollSelectorSettings<CardModel> settings = ScrollSelectorSettings.<CardModel>builder()
                .cards(cardModels)
                .cardZone(new SimpleIntervalZone(new Vector3f(0, 3.8f, 1.12f), new Vector3f(0.5f, 0.01f, 0)))
                .cardVisualizer(IngameCardVisualizer.forIngame_InspectOrSelect())
                .cardSelectionChangeCallback(this::onCardSelectionChange)
                .boardSettings(BoardUtil.getDefaultSettings("scroll_card_selector").build())
                .build();
        scrollSelectorAppState = new ScrollSelectorAppState<>(rootNode, settings);
        mainApplication.getStateManager().attach(scrollSelectorAppState);
    }

    @Override
    protected void initGui(int width, int height) {
        super.initGui(width, height);

        float buttonWidth = 50;
        float buttonHeight = 351;

        float buttonMarginX = 76;
        float buttonPreviousX = buttonMarginX;
        float buttonNextX = (width - buttonMarginX - buttonWidth);
        float buttonY = (height / 2f) + 231;

        Button buttonPrevious = GuiUtil.createButton("<", buttonWidth, buttonHeight, _ -> {
            scrollSelectorAppState.focusPreviousCard();
        });
        buttonPrevious.setLocalTranslation(buttonPreviousX, buttonY, 0);
        guiNode.attachChild(buttonPrevious);

        Button buttonNext = GuiUtil.createButton(">", buttonWidth, buttonHeight, _ -> {
            scrollSelectorAppState.focusNextCard();
        });
        buttonNext.setLocalTranslation(buttonNextX, buttonY, 0);
        guiNode.attachChild(buttonNext);
    }

    private void onCardSelectionChange(CardModel cardModel, boolean selected) {
        cardModel.setPlayable(selected);
        updateSubmitButton();
    }

    @Override
    protected List<CardModel> getSelectedTargetsCardModels() {
        return scrollSelectorAppState.getSelectedCards();
    }

    @Override
    public void cleanup() {
        super.cleanup();
        mainApplication.getStateManager().detach(scrollSelectorAppState);
    }
}
