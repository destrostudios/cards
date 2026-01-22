package com.destrostudios.cards.frontend.application.appstates.menu;

import com.destrostudios.cardgui.BoardObject;
import com.destrostudios.cardgui.CardZone;
import com.destrostudios.cardgui.InteractivitySource;
import com.destrostudios.cardgui.interactivities.ClickInteractivity;
import com.destrostudios.cardgui.samples.tools.deckbuilder.DeckBuilderSettings;
import com.destrostudios.cardgui.samples.tools.deckbuilder.collection.CollectionDeckBuilderAppState;
import com.destrostudios.cardgui.samples.tools.deckbuilder.collection.CollectionDeckBuilderSettings;
import com.destrostudios.cardgui.transformations.ConstantButTargetedTransformation;
import com.destrostudios.cardgui.zones.SimpleIntervalZone;
import com.destrostudios.cards.frontend.application.appstates.LoadingAppState;
import com.destrostudios.cards.frontend.application.appstates.services.CollectionCardAmountVisualizer;
import com.destrostudios.cards.frontend.application.appstates.services.IngameCardVisualizer;
import com.destrostudios.cards.frontend.application.appstates.services.cardpainter.model.CardType;
import com.destrostudios.cards.frontend.application.appstates.services.cardpainter.deckpower.DeckPower;
import com.destrostudios.cards.frontend.application.appstates.services.cardpainter.deckpower.DeckPowerVisualizer;
import com.destrostudios.cards.frontend.application.appstates.services.cardpainter.model.CardModel;
import com.destrostudios.cards.frontend.application.gui.GuiUtil;
import com.destrostudios.cards.frontend.application.modules.GameDataClientModule;
import com.destrostudios.cards.shared.model.*;
import com.destrostudios.cards.shared.model.internal.NewCardListCard;
import com.destrostudios.cards.shared.rules.GameConstants;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.TextField;

import java.util.*;

public class CollectionDeckAppState extends CachedModelsDeckAppState<CollectionDeckBuilderAppState<CardModel>> implements ActionListener {

    private static final int HIGHEST_BUTTON_MANA_COST = 10;

    public CollectionDeckAppState(Mode mode, Deck deck) {
        this.mode = mode;
        this.deck = deck;
    }
    private Mode mode;
    private Deck deck;
    private boolean isSelectingPower;
    private Node collectionGuiNode;
    private TextField textFieldName;
    private Label labelPowerName;
    private Power selectedPower;
    private DeckPower selectedPowerBoardObject;
    private Button buttonPreviousPage;
    private Button buttonNextPage;
    private Button[] buttonFilterManaCost;
    private Integer filteredManaCost;

    @Override
    protected String getTitle() {
        return "Deckbuilder - " + (isSelectingPower ? "Companion powers" : "Collection");
    }

    @Override
    public void initialize(AppStateManager stateManager, Application application){
        super.initialize(stateManager, application);
        initListeners();
    }

    @Override
    protected CollectionDeckBuilderAppState<CardModel> createDeckBuilder(DeckBuilderSettings<CardModel> deckBuilderSettings, Comparator<CardModel> cardOrder) {
        collectionGuiNode = new Node();
        guiNode.attachChild(collectionGuiNode);

        CardZone collectionZone = new SimpleIntervalZone(new Vector3f(-2, 0, 0), new Vector3f(3.65f, 1, 5));
        CollectionCardAmountVisualizer collectionCardAmountVisualizer = new CollectionCardAmountVisualizer(collectionGuiNode);

        CardList collection = getModule(GameDataClientModule.class).getUser().getCollectionCardList();
        HashMap<CardModel, Integer> collectionCards = mapCardList(collection);

        List<CardModel> powerCards = mapPowers(getModule(GameDataClientModule.class).getPowers());
        putOneOfEachCard(collectionCards, powerCards);

        HashMap<CardModel, Integer> deck = mapCardList(this.deck.getDeckCardList());

        CollectionDeckBuilderSettings<CardModel> settings = CollectionDeckBuilderSettings.<CardModel>builder()
            .deckBuilderSettings(deckBuilderSettings)
            .collectionZone(collectionZone)
            .collectionCardVisualizer(IngameCardVisualizer.forCollection())
            .collectionCardAmountVisualizer(collectionCardAmountVisualizer)
            .collectionCardsPerRow(4)
            .collectionRowsPerPage(2)
            .build();

        return new CollectionDeckBuilderAppState<>(mainApplication.getRootNode(), settings) {

            @Override
            protected void initialize(Application app) {
                super.initialize(app);
                setCollectionCards(collectionCards);
                setCollectionCardOrder(cardOrder);
                setFilter(null);
                setDeck(deck);
                initPower();
            }
        };
    }

    @Override
    protected Integer getMaximumUniqueDeckCards(CardModel cardModel) {
        return cardModel.isLegendary() ? GameConstants.MAXIMUM_DECK_CARD_AMOUNT_LEGENDARY : GameConstants.MAXIMUM_DECK_CARD_AMOUNT_NON_LEGENDARY;
    }

    private void initPower() {
        deckBuilderAppState.getBoard().registerVisualizer_Class(DeckPower.class, new DeckPowerVisualizer());

        selectedPowerBoardObject = new DeckPower();
        selectedPowerBoardObject.position().setTransformation(new ConstantButTargetedTransformation<>(new Vector3f(8.51f, 0, -5.6f)));
        selectedPowerBoardObject.setInteractivity(InteractivitySource.MOUSE_LEFT, new ClickInteractivity() {

            @Override
            public void trigger(BoardObject source, BoardObject target) {
                setSelectingPower(!isSelectingPower);
            }
        });
        deckBuilderAppState.getBoard().register(selectedPowerBoardObject);

        setSelectedPower(getPowerModel(deck.getPower()));
    }

    @Override
    protected void initGui(float rightColumnX, float rightColumnWidth) {
        super.initGui(rightColumnX, rightColumnWidth);

        float x;
        float y;

        float startY = labelDeckSize.getLocalTranslation().getY() + 15;

        // Deck name
        x = rightColumnX - 10 - rightColumnWidth;
        y = startY;

        Label labelName = new Label("Deck name:");
        labelName.setLocalTranslation(x, y, 0);
        labelName.setColor(ColorRGBA.White);
        guiNode.attachChild(labelName);
        y -= 20;
        textFieldName = new TextField(deck.getDeckCardList().getName());
        textFieldName.setLocalTranslation(x, y, 0);
        textFieldName.setPreferredWidth(rightColumnWidth);
        textFieldName.setFontSize(16);
        guiNode.attachChild(textFieldName);

        // Power
        x = rightColumnX;
        y = startY;

        Label labelPowerTitle = new Label("Companion power:");
        labelPowerTitle.setLocalTranslation(x, y, 0);
        labelPowerTitle.setColor(ColorRGBA.White);
        guiNode.attachChild(labelPowerTitle);
        y -= 20;
        labelPowerName = new Label("");
        labelPowerName.setLocalTranslation(x, y, 0);
        labelPowerName.setColor(ColorRGBA.White);
        guiNode.attachChild(labelPowerName);

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

        // Hide these buttons when a card is inspected
        collectionGuiNode.attachChild(buttonPreviousPage);
        collectionGuiNode.attachChild(buttonNextPage);

        // Filter
        x = 55;
        int buttonManaFilterWidth = 50;
        buttonFilterManaCost = new Button[HIGHEST_BUTTON_MANA_COST + 1];
        for (int i = 0; i < buttonFilterManaCost.length; i++) {
            int manaCost = i;
            String buttonText = manaCost + (manaCost < HIGHEST_BUTTON_MANA_COST ? "" : "+");
            Button button = addButton(buttonText, buttonManaFilterWidth, GuiUtil.BUTTON_HEIGHT_DEFAULT, _ -> toggleFilter(manaCost));
            button.setLocalTranslation(x, 89, 0);
            buttonFilterManaCost[i] = button;
            x += buttonManaFilterWidth;
        }

        // Save
        Button buttonSave = addButton("Save", rightColumnWidth, GuiUtil.BUTTON_HEIGHT_DEFAULT, b -> saveDeck());
        buttonSave.setLocalTranslation(rightColumnX, 86 + GuiUtil.BUTTON_HEIGHT_DEFAULT, 0);
    }

    @Override
    protected void back() {
        switchTo(new CollectionAppState());
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

    public void setSelectingPower(boolean isSelectingPower) {
        this.isSelectingPower = isSelectingPower;
        selectedPowerBoardObject.getModel().setSelected(isSelectingPower);
        updateTitle();
        setFilter(null);
    }

    private void toggleFilter(Integer manaCost) {
        setFilter(Objects.equals(filteredManaCost, manaCost) ? null : manaCost);
    }

    private void setFilter(Integer manaCost) {
        filteredManaCost = manaCost;
        deckBuilderAppState.setCollectionCardFilter(cardModel -> {
            if ((isSelectingPower && (cardModel.getType() == CardType.POWER))
            || (!isSelectingPower && ((cardModel.getType() == CardType.CREATURE) || (cardModel.getType() == CardType.SPELL)))) {
                if (filteredManaCost == null) {
                    return true;
                } else  if (filteredManaCost < HIGHEST_BUTTON_MANA_COST) {
                    return cardModel.getManaCostDetails() == filteredManaCost;
                } else {
                    return cardModel.getManaCostDetails() >= filteredManaCost;
                }
            }
            return false;
        });
        updateCollectionGui();
    }

    private void goToPreviousPage() {
        if (deckBuilderAppState.getCollectionPage() > 0) {
            deckBuilderAppState.goToPreviousCollectionPage();
            updateCollectionGui();
        }
    }

    private void goToNextPage() {
        if (deckBuilderAppState.getCollectionPage() < (deckBuilderAppState.getCollectionPagesCount() - 1)) {
            deckBuilderAppState.goToNextCollectionPage();
            updateCollectionGui();
        }
    }

    private void updateCollectionGui() {
        buttonPreviousPage.setCullHint(deckBuilderAppState.getCollectionPage() > 0 ? Spatial.CullHint.Inherit : Spatial.CullHint.Always);
        buttonNextPage.setCullHint(deckBuilderAppState.getCollectionPage() < (deckBuilderAppState.getCollectionPagesCount() - 1) ? Spatial.CullHint.Inherit : Spatial.CullHint.Always);
        for (int i = 0; i < buttonFilterManaCost.length; i++) {
            boolean isSelected = ((filteredManaCost != null) && (filteredManaCost == i));
            ColorRGBA color = isSelected ? ColorRGBA.Green : ColorRGBA.White;
            buttonFilterManaCost[i].setColor(color);
            buttonFilterManaCost[i].setFocusColor(color);
        }
    }

    private void saveDeck() {
        LinkedList<NewCardListCard> cards = new LinkedList<>();
        for (Map.Entry<CardModel, Integer> entry : deckBuilderAppState.getDeck().entrySet()) {
            CardIdentifier cardIdentifier = getCardIdentifier(entry.getKey());
            cards.add(new NewCardListCard(cardIdentifier.getCard().getId(), cardIdentifier.getFoil().getId(), entry.getValue()));
        }
        String name = textFieldName.getText();
        if (name.isEmpty()) {
            name = null;
        }
        getModule(GameDataClientModule.class).updateDeck(mode, deck, name, selectedPower, cards);
        mainApplication.getStateManager().attach(new LoadingAppState() {

            @Override
            protected boolean shouldClose() {
                return (getModule(GameDataClientModule.class).getUser() != null);
            }
        });
    }

    @Override
    protected boolean interceptOnTryToAddCard(CardModel cardModel) {
        if (isSelectingPower) {
            setSelectedPower(cardModel);
            setSelectingPower(false);
            return false;
        }
        return super.interceptOnTryToAddCard(cardModel);
    }

    public void setSelectedPower(CardModel cardModel) {
        selectedPower = getPower(cardModel);
        labelPowerName.setText(cardModel.getName());
        selectedPowerBoardObject.getModel().setName(cardModel.getName());
    }

    @Override
    public void cleanup() {
        super.cleanup();
        mainApplication.getInputManager().deleteMapping("left");
        mainApplication.getInputManager().deleteMapping("right");
        mainApplication.getInputManager().removeListener(this);
    }
}