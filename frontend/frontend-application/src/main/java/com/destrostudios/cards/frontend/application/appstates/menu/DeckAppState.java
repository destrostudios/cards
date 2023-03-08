package com.destrostudios.cards.frontend.application.appstates.menu;

import com.destrostudios.cardgui.BoardSettings;
import com.destrostudios.cardgui.CardZone;
import com.destrostudios.cardgui.samples.tools.deckbuilder.DeckBuilderAppState;
import com.destrostudios.cardgui.samples.tools.deckbuilder.DeckBuilderSettings;
import com.destrostudios.cardgui.zones.SimpleIntervalZone;
import com.destrostudios.cards.frontend.application.appstates.BackgroundAppState;
import com.destrostudios.cards.frontend.application.appstates.services.CardGuiMapper;
import com.destrostudios.cards.frontend.application.appstates.services.DeckBuilderCardVisualizer;
import com.destrostudios.cards.frontend.application.appstates.services.IngameCardVisualizer;
import com.destrostudios.cards.frontend.application.appstates.services.cardpainter.model.CardModel;
import com.destrostudios.cards.frontend.application.modules.GameDataClientModule;
import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.entities.SimpleEntityData;
import com.destrostudios.cards.shared.entities.templates.EntityTemplate;
import com.destrostudios.cards.shared.files.FileManager;
import com.destrostudios.cards.shared.model.Card;
import com.destrostudios.cards.shared.rules.Components;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.simsilica.lemur.Button;
import lombok.Getter;

import java.util.*;
import java.util.function.Predicate;

public class DeckAppState extends MenuAppState implements ActionListener {

    private static final String DECK_FILE_PATH = "./deck.txt";

    private AmbientLight ambientLight;
    private DirectionalLight directionalLight;
    private HashMap<CardModel, Integer> collectionCards;
    private HashMap<String, CardModel> templatesToCardModelsMap;
    private HashMap<CardModel, String> cardModelsToTemplatesMap;
    @Getter
    private List<String> libraryTemplates;
    private BitmapText textTitle;
    private Button buttonPreviousPage;
    private Button buttonNextPage;
    private Button[] buttonFilterManaCost;
    private Integer filteredManaCost;

    @Override
    public void initialize(AppStateManager stateManager, Application application){
        super.initialize(stateManager, application);
        stateManager.attach(new BackgroundAppState("images/background.png"));
        initCamera();
        initLight();
        initDeck();
        initCards();
        initDeckBuilder();
        initGui();
        initListeners();
    }

    private void initCamera() {
        mainApplication.getCamera().setLocation(new Vector3f(0, 15, 0));
        // Look straight down
        mainApplication.getCamera().lookAtDirection(new Vector3f(0, -1, 0).normalizeLocal(), Vector3f.UNIT_Z.mult(-1));
    }

    private void initLight() {
        ambientLight = new AmbientLight(ColorRGBA.White);
        mainApplication.getRootNode().addLight(ambientLight);
        Vector3f lightDirection = new Vector3f(1, -5, -1).normalizeLocal();
        directionalLight = new DirectionalLight(lightDirection, ColorRGBA.White.mult(0.5f));
        mainApplication.getRootNode().addLight(directionalLight);
    }

    private void initDeck() {
        libraryTemplates = FileManager.getFileLines(DECK_FILE_PATH);
    }

    private void initCards() {
        collectionCards = new HashMap<>();
        templatesToCardModelsMap = new HashMap<>();
        cardModelsToTemplatesMap = new HashMap<>();
        EntityData data = new SimpleEntityData(Components.ALL);
        GameDataClientModule gameDataModule = mainApplication.getToolsClient().getModule(GameDataClientModule.class);
        for (Card card : gameDataModule.getCards()) {
            int cardEntity = data.createEntity();
            EntityTemplate.loadTemplate(data, cardEntity, card.getPath());
            CardModel cardModel = new CardModel();
            CardGuiMapper.updateModel(data, cardEntity, cardModel, true);
            collectionCards.put(cardModel, Integer.MAX_VALUE);

            templatesToCardModelsMap.put(card.getPath(), cardModel);
            cardModelsToTemplatesMap.put(cardModel, card.getPath());
        }
    }

    private void initDeckBuilder() {
        CardZone collectionZone = new SimpleIntervalZone(new Vector3f(-2, 0, 0), new Vector3f(3.65f, 1, 5));
        CardZone deckZone = new SimpleIntervalZone(new Vector3f(8.25f, 0, -4.715f), new Vector3f(1, 1, 0.57f));
        IngameCardVisualizer collectionCardVisualizer = new IngameCardVisualizer(false, false, 4.25f);
        DeckBuilderCardVisualizer deckCardVisualizer = new DeckBuilderCardVisualizer();
        Comparator<CardModel> deckCardOrder = Comparator.comparing(CardModel::getManaCostDetails);
        DeckBuilderSettings<CardModel> settings = DeckBuilderSettings.<CardModel>builder()
                .collectionCards(collectionCards)
                .collectionZone(collectionZone)
                .deckZone(deckZone)
                .collectionCardVisualizer(collectionCardVisualizer)
                .deckCardVisualizer(deckCardVisualizer)
                .deckCardOrder(deckCardOrder)
                .deckCardsMaximumTotal(30)
                .collectionCardsPerRow(4)
                .collectionRowsPerPage(2)
                .boardSettings(BoardSettings.builder()
                        .inputActionPrefix("deckbuilder")
                        .build())
                .build();

        HashMap<CardModel, Integer> deck = new HashMap<>();
        for (String template : libraryTemplates) {
            CardModel cardModel = templatesToCardModelsMap.get(template);
            deck.put(cardModel, deck.computeIfAbsent(cardModel, cm -> 0) + 1);
        }

        mainApplication.getStateManager().attach(new DeckBuilderAppState<>(mainApplication.getRootNode(), settings) {

            @Override
            protected void initialize(Application app) {
                super.initialize(app);
                setCollectionCardOrder(Comparator.comparing(CardModel::getManaCostDetails));
                setDeck(deck);
            }
        });
    }

    private void initGui() {
        DeckBuilderAppState<CardModel> deckBuilderAppState = getAppState(DeckBuilderAppState.class);

        BitmapFont guiFont = mainApplication.getAssetManager().loadFont("Interface/Fonts/Default.fnt");
        textTitle = new BitmapText(guiFont);
        textTitle.setSize(20);
        float margin = 30;
        float x = margin;
        float y = (mainApplication.getContext().getSettings().getHeight() - margin);
        textTitle.setLocalTranslation(x, y, 0);
        guiNode.attachChild(textTitle);

        // Pagination
        int buttonPaginationWidth = 50;
        int buttonPaginationHeight = 250;
        float buttonPreviousX = 55;
        float buttonNextX = (1255 - buttonPaginationWidth);
        float buttonPaginationY = (height / 2f) + (buttonPaginationHeight / 2f);
        buttonPreviousPage = addButton("<", buttonPaginationWidth, buttonPaginationHeight, b -> goToPreviousPage());
        buttonPreviousPage.setLocalTranslation(buttonPreviousX, buttonPaginationY, 0);
        buttonNextPage = addButton(">", buttonPaginationWidth, buttonPaginationHeight, b -> goToNextPage());
        buttonNextPage.setLocalTranslation(buttonNextX, buttonPaginationY, 0);

        // Filter
        x = 55;
        int buttonManaFilterWidth = 50;
        buttonFilterManaCost = new Button[11];
        for (int i = 0; i < buttonFilterManaCost.length; i++) {
            final int manaCost = i;
            Predicate<CardModel> filter = cardModel -> cardModel.getManaCostDetails() == manaCost;
            Button button = addButton("" + i, buttonManaFilterWidth, BUTTON_HEIGHT_DEFAULT, b -> {
                if (deckBuilderAppState.getCollectionCardFilter() == filter) {
                    deckBuilderAppState.setCollectionCardFilter(null);
                    filteredManaCost = null;
                } else {
                    deckBuilderAppState.setCollectionCardFilter(filter);
                    filteredManaCost = manaCost;
                }
                deckBuilderAppState.goToCollectionPage(0);
                updateGui();
            });
            button.setLocalTranslation(x, 89, 0);
            buttonFilterManaCost[i] = button;
            x += buttonManaFilterWidth;
        }

        // Save
        float buttonSaveWidth = 293;
        Button buttonSave = addButton("Save", buttonSaveWidth, BUTTON_HEIGHT_DEFAULT, b -> {
            libraryTemplates.clear();
            for (Map.Entry<CardModel, Integer> entry : deckBuilderAppState.getDeck().entrySet()) {
                String template = cardModelsToTemplatesMap.get(entry.getKey());
                for (int i = 0; i < entry.getValue(); i++) {
                    libraryTemplates.add(template);
                }
            }
            FileManager.putFileContent(DECK_FILE_PATH, String.join("\n", libraryTemplates));
            updateGui();
        });
        buttonSave.setLocalTranslation(width - 56 - buttonSaveWidth, 86 + BUTTON_HEIGHT_DEFAULT, 0);

        updateGui();
    }

    private void initListeners() {
        InputManager inputManager = mainApplication.getInputManager();
        inputManager.addMapping("left", new KeyTrigger(KeyInput.KEY_LEFT));
        inputManager.addMapping("right", new KeyTrigger(KeyInput.KEY_RIGHT));
        inputManager.addListener(this, "left", "right");
    }

    @Override
    public void onAction(String name, boolean isPressed, float lastTimePerFrame) {
        if ("left".equals(name) && isPressed) {
            goToPreviousPage();
        } else if ("right".equals(name) && isPressed) {
            goToNextPage();
        }
    }

    private void goToPreviousPage() {
        DeckBuilderAppState<CardModel> deckBuilderAppState = getAppState(DeckBuilderAppState.class);
        if (deckBuilderAppState.getCollectionPage() > 0) {
            deckBuilderAppState.goToPreviousCollectionPage();
            updateGui();
        }
    }

    private void goToNextPage() {
        DeckBuilderAppState<CardModel> deckBuilderAppState = getAppState(DeckBuilderAppState.class);
        if (deckBuilderAppState.getCollectionPage() < (deckBuilderAppState.getCollectionPagesCount() - 1)) {
            deckBuilderAppState.goToNextCollectionPage();
            updateGui();
        }
    }

    private void updateGui() {
        DeckBuilderAppState<CardModel> deckBuilderAppState = getAppState(DeckBuilderAppState.class);
        textTitle.setText("Deckbuilder (Your deck: " + libraryTemplates.size() + ")");
        buttonPreviousPage.setCullHint(deckBuilderAppState.getCollectionPage() > 0 ? Spatial.CullHint.Inherit : Spatial.CullHint.Always);
        buttonNextPage.setCullHint(deckBuilderAppState.getCollectionPage() < (deckBuilderAppState.getCollectionPagesCount() - 1) ? Spatial.CullHint.Inherit : Spatial.CullHint.Always);
        for (int i = 0; i < buttonFilterManaCost.length; i++) {
            boolean isSelected = ((filteredManaCost != null) && (filteredManaCost == i));
            buttonFilterManaCost[i].setColor(isSelected ? ColorRGBA.Green : ColorRGBA.White);
        }
    }

    @Override
    public void cleanup() {
        super.cleanup();
        mainApplication.getInputManager().deleteMapping("left");
        mainApplication.getInputManager().deleteMapping("right");
        mainApplication.getInputManager().removeListener(this);
        mainApplication.getStateManager().detach(getAppState(DeckBuilderAppState.class));
        mainApplication.getRootNode().removeLight(ambientLight);
        mainApplication.getRootNode().removeLight(directionalLight);
        mainApplication.getStateManager().detach(getAppState(BackgroundAppState.class));
    }
}