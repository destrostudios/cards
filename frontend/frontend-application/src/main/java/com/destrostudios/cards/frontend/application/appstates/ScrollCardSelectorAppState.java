package com.destrostudios.cards.frontend.application.appstates;

import com.destrostudios.cardgui.samples.tools.scrollselector.ScrollSelectorAppState;
import com.destrostudios.cardgui.samples.tools.scrollselector.ScrollSelectorSettings;
import com.destrostudios.cardgui.zones.SimpleIntervalZone;
import com.destrostudios.cards.frontend.application.appstates.services.BoardUtil;
import com.destrostudios.cards.frontend.application.appstates.services.IngameCardVisualizer;
import com.destrostudios.cards.frontend.application.appstates.services.cardpainter.model.CardModel;
import com.destrostudios.cards.frontend.application.gui.GuiUtil;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.math.Vector3f;
import com.simsilica.lemur.Button;

import java.util.List;

public abstract class ScrollCardSelectorAppState extends SelectorAppState {

    public ScrollCardSelectorAppState(String description, Runnable onCancel) {
        super(description, onCancel, true);
    }
    private ScrollSelectorAppState<CardModel> scrollSelectorAppState;

    @Override
    public void initialize(AppStateManager stateManager, Application application) {
        super.initialize(stateManager, application);
        ScrollSelectorSettings<CardModel> settings = ScrollSelectorSettings.<CardModel>builder()
                .cards(createCardModels())
                .cardZone(new SimpleIntervalZone(getCenter(), new Vector3f(0.5f, 0.01f, 0)))
                .cardVisualizer(IngameCardVisualizer.forIngame_InspectOrSelect())
                .cardSelectionChangeCallback(this::onCardSelectionChange)
                .boardSettings(BoardUtil.getDefaultSettings("scroll_card_selector").build())
                .build();
        scrollSelectorAppState = new ScrollSelectorAppState<>(rootNode, settings);
        mainApplication.getStateManager().attach(scrollSelectorAppState);

        updateSubmitButton();
    }

    protected abstract List<CardModel> createCardModels();

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

    private void updateSubmitButton() {
        List<CardModel> selectedCardModels = getSelectedCardModels();
        buttonSubmit.setText("Submit (" + selectedCardModels.size() + ")");
        GuiUtil.setButtonEnabled(buttonSubmit, isValidSelection(selectedCardModels));
    }

    protected List<CardModel> getSelectedCardModels() {
        return scrollSelectorAppState.getSelectedCards();
    }

    protected boolean isValidSelection(List<CardModel> selectedCardModels) {
        return true;
    }

    @Override
    public void cleanup() {
        super.cleanup();
        mainApplication.getStateManager().detach(scrollSelectorAppState);
    }
}
