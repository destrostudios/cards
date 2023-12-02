package com.destrostudios.cards.frontend.application.appstates.menu;

import com.destrostudios.cardgui.CardZone;
import com.destrostudios.cardgui.samples.tools.cardpack.CardPackAppState;
import com.destrostudios.cardgui.samples.tools.cardpack.CardPackSettings;
import com.destrostudios.cardgui.zones.SimpleIntervalZone;
import com.destrostudios.cards.frontend.application.appstates.LoadingAppState;
import com.destrostudios.cards.frontend.application.appstates.services.CardGuiMapper;
import com.destrostudios.cards.frontend.application.appstates.services.IngameCardVisualizer;
import com.destrostudios.cards.frontend.application.appstates.services.cardpainter.CardPainter;
import com.destrostudios.cards.frontend.application.appstates.services.cardpainter.model.CardModel;
import com.destrostudios.cards.frontend.application.gui.GuiUtil;
import com.destrostudios.cards.frontend.application.modules.GameDataClientModule;
import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.entities.SimpleEntityData;
import com.destrostudios.cards.shared.model.internal.PackResult;
import com.destrostudios.cards.shared.rules.Components;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.math.Vector3f;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Command;

import java.util.List;
import java.util.stream.Collectors;

public class PacksAppState extends MenuAppState {

    private Button buttonOpen;
    private Button buttonBack;
    private CardPackAppState<CardModel> cardPackAppState;

    @Override
    public void initialize(AppStateManager stateManager, Application application) {
        super.initialize(stateManager, application);
        setTopDownCamera();
        addButtons();
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
        GameDataClientModule gameDataClientModule = getModule(GameDataClientModule.class);
        Integer packs = ((gameDataClientModule.getUser() != null) ? gameDataClientModule.getPacks() : null);
        if (packs != null) {
            buttonOpen.setText((packs > 0) ? "Open pack (" + packs + ")" : "No packs left");
        }
        boolean hasPacksLeft = ((packs != null) && (packs > 0));
        boolean hasNoPackOpeningOngoing = ((cardPackAppState == null) || cardPackAppState.areAllCardsRevealed());
        boolean canOpenPack = (hasPacksLeft && hasNoPackOpeningOngoing);
        GuiUtil.setButtonEnabled(buttonOpen, canOpenPack);
        GuiUtil.setButtonEnabled(buttonBack, hasNoPackOpeningOngoing);
    }

    private void addButtons() {
        float margin = 50;
        float buttonWidth = 200;
        float buttonHeight = 100;
        float x = ((width / 2f) - (buttonWidth / 2));
        float y = ((height / 2f) + (buttonHeight / 2));
        buttonOpen = addButton(null, x, y, buttonWidth, buttonHeight, b -> openPack());
        x = margin;
        y = (margin + buttonHeight);
        buttonBack = addButton("Back", x, y, buttonWidth, buttonHeight, b -> switchTo(new MainMenuAppState()));
    }

    private Button addButton(String text, float x, float y, float buttonWidth, float buttonHeight, Command<Button> command) {
        Button button = addButton(text, buttonWidth, buttonHeight, command);
        button.setLocalTranslation(x, y, 0);
        return button;
    }

    private void openPack() {
        removePackResult();
        getModule(GameDataClientModule.class).openPack();
        waitForPackResult();
    }

    private void waitForPackResult() {
        mainApplication.getStateManager().attach(new LoadingAppState() {

            private PackResult packResult;
            private List<CardModel> cards;
            private boolean readyToShow;

            @Override
            public void update(float tpf) {
                super.update(tpf);
                if (packResult == null) {
                    packResult = getModule(GameDataClientModule.class).getPackResult();
                    if (packResult != null) {
                        new Thread(() -> {
                            cards = createCardModels(packResult);
                            for (CardModel cardModel : cards) {
                                CardPainter.getAll(cardModel, false);
                            }
                            readyToShow = true;
                        }).start();
                    }
                }
            }

            @Override
            protected boolean shouldClose() {
                return readyToShow;
            }

            @Override
            protected void close() {
                super.close();
                showPackResult(cards);
            }
        });
    }

    private List<CardModel> createCardModels(PackResult packResult) {
        EntityData data = new SimpleEntityData(Components.ALL);
        return packResult.getCards().stream()
                .map(cardIdentifier -> CardGuiMapper.createModel(data, cardIdentifier))
                .collect(Collectors.toList());
    }

    private void showPackResult(List<CardModel> cards) {
        CardZone cardZone = new SimpleIntervalZone(new Vector3f(0, 0, 0.38f), new Vector3f(3.9f, 3.9f, 4));
        CardPackSettings<CardModel> settings = CardPackSettings.<CardModel>builder()
                .cards(cards)
                .cardZone(cardZone)
                .cardVisualizer(IngameCardVisualizer.forPack())
                .packOpenDuration(0.5f)
                .packOpenDurationFactorScale(0.8f)
                .build();
        cardPackAppState = new CardPackAppState<>(mainApplication.getRootNode(), settings) {

            @Override
            protected void initialize(Application app) {
                super.initialize(app);
                openPack();
            }
        };
        mainApplication.getStateManager().attach(cardPackAppState);
    }

    @Override
    public void cleanup() {
        super.cleanup();
        removePackResult();
    }

    private void removePackResult() {
        if (cardPackAppState != null) {
            mainApplication.getStateManager().detach(cardPackAppState);
        }
    }
}
