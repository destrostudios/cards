package com.destrostudios.cards.frontend.application.appstates;

import com.destrostudios.cardgui.BoardSettings;
import com.destrostudios.cardgui.CardZone;
import com.destrostudios.cardgui.samples.tools.deckbuilder.DeckBuilderAppState;
import com.destrostudios.cardgui.samples.tools.deckbuilder.DeckBuilderSettings;
import com.destrostudios.cardgui.zones.SimpleIntervalZone;
import com.destrostudios.cards.frontend.application.appstates.services.CardGuiMapper;
import com.destrostudios.cards.frontend.application.appstates.services.DeckBuilderCardVisualizer;
import com.destrostudios.cards.frontend.application.appstates.services.IngameCardVisualizer;
import com.destrostudios.cards.frontend.application.appstates.services.cardpainter.model.CardModel;
import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.entities.SimpleEntityData;
import com.destrostudios.cards.shared.entities.templates.EntityTemplate;
import com.destrostudios.cards.shared.rules.AllCards;
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
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Command;
import com.simsilica.lemur.HAlignment;
import com.simsilica.lemur.VAlignment;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

public class DeckAppState extends MyBaseAppState implements ActionListener {

    private static final float BUTTON_HEIGHT_DEFAULT = 50;

    private List<CardModel> allCardModels;
    private AmbientLight ambientLight;
    private DirectionalLight directionalLight;
    private Node guiNode;
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
        initAllCardModels();
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

    private void initAllCardModels() {
        allCardModels = new LinkedList<>();
        EntityData data = new SimpleEntityData();
        for (String template : AllCards.TEMPLATES) {
            int card = data.createEntity();
            EntityTemplate.loadTemplate(data, card, template);
            CardModel cardModel = new CardModel();
            CardGuiMapper.updateModel(data, card, cardModel);
            allCardModels.add(cardModel);
        }
        allCardModels.sort(Comparator.comparing(CardModel::getManaCostDetails));
    }

    private void initDeckBuilder() {
        CardZone collectionZone = new SimpleIntervalZone(new Vector3f(-2, 0, 0), new Vector3f(3.65f, 1, 5));
        CardZone deckZone = new SimpleIntervalZone(new Vector3f(8.25f, 0, -4.715f), new Vector3f(1, 1, 0.57f));
        IngameCardVisualizer collectionCardVisualizer = new IngameCardVisualizer(false, false, 4.25f);
        DeckBuilderCardVisualizer deckCardVisualizer = new DeckBuilderCardVisualizer();
        Comparator<CardModel> deckCardOrder = Comparator.comparing(CardModel::getManaCostDetails);
        DeckBuilderSettings<CardModel> settings = DeckBuilderSettings.<CardModel>builder()
                .allCardModels(allCardModels)
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
        mainApplication.getStateManager().attach(new DeckBuilderAppState<>(mainApplication.getRootNode(), settings));
    }

    private void initGui() {
        DeckBuilderAppState<CardModel> deckBuilderAppState = getAppState(DeckBuilderAppState.class);
        int width = mainApplication.getContext().getSettings().getWidth();
        int height = mainApplication.getContext().getSettings().getHeight();

        guiNode = new Node();

        BitmapFont guiFont = mainApplication.getAssetManager().loadFont("Interface/Fonts/Default.fnt");
        BitmapText textTitle = new BitmapText(guiFont);
        textTitle.setSize(20);
        textTitle.setText("Deckbuilder");
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
            System.out.println(deckBuilderAppState.getDeck().size());
        });
        buttonSave.setLocalTranslation(width - 56 - buttonSaveWidth, 86 + BUTTON_HEIGHT_DEFAULT, 0);

        mainApplication.getGuiNode().attachChild(guiNode);
        updateGui();
    }

    private Button addButton(String text, float width, float height, Command<Button> command) {
        Button button = new Button(text);
        button.setPreferredSize(new Vector3f(width, height, 0));
        button.setTextHAlignment(HAlignment.Center);
        button.setTextVAlignment(VAlignment.Center);
        button.setFontSize(16);
        button.setColor(ColorRGBA.White);
        button.setFocusColor(ColorRGBA.White);
        button.addCommands(Button.ButtonAction.Up, command);
        guiNode.attachChild(button);
        return button;
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
            deckBuilderAppState.goToNextColletionPage();
            updateGui();
        }
    }

    private void updateGui() {
        DeckBuilderAppState<CardModel> deckBuilderAppState = getAppState(DeckBuilderAppState.class);
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
        mainApplication.getGuiNode().detachChild(guiNode);
        mainApplication.getStateManager().detach(getAppState(DeckBuilderAppState.class));
        mainApplication.getRootNode().removeLight(ambientLight);
        mainApplication.getRootNode().removeLight(directionalLight);
        mainApplication.getStateManager().detach(getAppState(BackgroundAppState.class));
    }
}