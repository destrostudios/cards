package com.destrostudios.cards.frontend.application.appstates.menu;

import com.destrostudios.cardgui.*;
import com.destrostudios.cardgui.Card;
import com.destrostudios.cardgui.events.MoveCardEvent;
import com.destrostudios.cardgui.samples.tools.deckbuilder.DeckBuilderDeckCardModel;
import com.destrostudios.cardgui.samples.tools.deckbuilder.DeckBuilderSettings;
import com.destrostudios.cardgui.samples.tools.deckbuilder.collection.CollectionDeckBuilderAppState;
import com.destrostudios.cardgui.samples.tools.deckbuilder.collection.CollectionDeckBuilderSettings;
import com.destrostudios.cardgui.zones.SimpleIntervalZone;
import com.destrostudios.cards.frontend.application.CompositeComparator;
import com.destrostudios.cards.frontend.application.appstates.BackgroundAppState;
import com.destrostudios.cards.frontend.application.appstates.LoadingAppState;
import com.destrostudios.cards.frontend.application.appstates.services.CardGuiMapper;
import com.destrostudios.cards.frontend.application.appstates.services.CollectionCardAmountVisualizer;
import com.destrostudios.cards.frontend.application.appstates.services.DeckBuilderCardVisualizer;
import com.destrostudios.cards.frontend.application.appstates.services.IngameCardVisualizer;
import com.destrostudios.cards.frontend.application.appstates.services.cardpainter.model.CardModel;
import com.destrostudios.cards.frontend.application.appstates.services.zones.DeckBuilderDeckZone;
import com.destrostudios.cards.frontend.application.gui.GuiUtil;
import com.destrostudios.cards.frontend.application.modules.GameDataClientModule;
import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.entities.SimpleEntityData;
import com.destrostudios.cards.shared.model.*;
import com.destrostudios.cards.shared.model.internal.NewCardListCard;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameConstants;
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
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Quad;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.TextField;

import java.util.*;
import java.util.function.Predicate;

public class DeckAppState extends MenuAppState implements ActionListener {

    public DeckAppState(Mode mode, Deck deck) {
        this.mode = mode;
        this.deck = deck;
    }
    private Mode mode;
    private Deck deck;
    private AmbientLight ambientLight;
    private DirectionalLight directionalLight;
    private HashMap<CardModel, Integer> collectionCards;
    private HashMap<String, CardModel> cardsToCardModelsMap;
    private HashMap<CardModel, CardListCard> cardModelsToCardsMap;
    private Node collectionGuiNode;
    private CollectionDeckBuilderAppState<CardModel> deckBuilderAppState;
    private SimpleIntervalZone inspectionZone;
    private Card<CardModel> inspectionCard;
    private Geometry inspectionBackdrop;
    private BitmapText textTitle;
    private TextField textFieldName;
    private Button buttonPreviousPage;
    private Button buttonNextPage;
    private Button[] buttonFilterManaCost;
    private Integer filteredManaCost;

    @Override
    public void initialize(AppStateManager stateManager, Application application){
        super.initialize(stateManager, application);
        getAppState(BackgroundAppState.class).setBackground("deck");
        setTopDownCamera();
        initLight();
        initCards();
        initDeckBuilder();
        initGui();
        initListeners();
    }

    private void initLight() {
        ambientLight = new AmbientLight(ColorRGBA.White);
        mainApplication.getRootNode().addLight(ambientLight);
        Vector3f lightDirection = new Vector3f(1, -5, -1).normalizeLocal();
        directionalLight = new DirectionalLight(lightDirection, ColorRGBA.White.mult(0.5f));
        mainApplication.getRootNode().addLight(directionalLight);
    }

    private void initCards() {
        collectionCards = new HashMap<>();
        cardsToCardModelsMap = new HashMap<>();
        cardModelsToCardsMap = new HashMap<>();
        CardList collection = getModule(GameDataClientModule.class).getCollection();
        EntityData data = new SimpleEntityData(Components.ALL);
        for (CardListCard cardListCard : collection.getCards()) {
            CardModel cardModel = CardGuiMapper.createModel(data, cardListCard);
            collectionCards.put(cardModel, cardListCard.getAmount());

            String cardKey = getCardListCardKey(cardListCard);
            cardsToCardModelsMap.put(cardKey, cardModel);
            cardModelsToCardsMap.put(cardModel, cardListCard);
        }
    }

    private void initDeckBuilder() {
        collectionGuiNode = new Node();
        guiNode.attachChild(collectionGuiNode);

        CardZone collectionZone = new SimpleIntervalZone(new Vector3f(-2, 0, 0), new Vector3f(3.65f, 1, 5));
        CardZone deckZone = new DeckBuilderDeckZone(new Vector3f(8.25f, 0, -5));
        CollectionCardAmountVisualizer collectionCardAmountVisualizer = new CollectionCardAmountVisualizer(collectionGuiNode);
        DeckBuilderCardVisualizer deckCardVisualizer = new DeckBuilderCardVisualizer();
        Comparator<CardModel> cardOrder = new CompositeComparator<>(
            Comparator.comparing(CardModel::getManaCostDetails),
            Comparator.comparing(CardModel::getTitle),
            Comparator.comparing(cardModel -> ((cardModel.getFoil() != null) ? cardModel.getFoil().ordinal() : -1))
        );
        CollectionDeckBuilderSettings<CardModel> settings = CollectionDeckBuilderSettings.<CardModel>builder()
            .deckBuilderSettings(DeckBuilderSettings.<CardModel>builder()
                .deckZone(deckZone)
                .deckCardVisualizer(deckCardVisualizer)
                .deckCardOrder(cardOrder)
                .deckCardsMaximumTotal(GameConstants.MAXIMUM_DECK_SIZE)
                .deckCardsMaximumUnique(cardModel -> cardModel.isLegendary() ? GameConstants.MAXIMUM_DECK_CARD_AMOUNT_LEGENDARY : GameConstants.MAXIMUM_DECK_CARD_AMOUNT_NON_LEGENDARY)
                .boardSettings(BoardSettings.builder()
                    .inputActionPrefix("deckbuilder")
                    .hoverInspectionDelay(0f)
                    .isInspectable(transformedBoardObject -> (transformedBoardObject instanceof Card card) && (card.getZonePosition().getZone() == deckZone))
                    .inspector(new Inspector() {

                        @Override
                        public void inspect(BoardAppState boardAppState, TransformedBoardObject<?> transformedBoardObject, Vector3f vector3f) {
                            Card<DeckBuilderDeckCardModel<CardModel>> deckCard = (Card<DeckBuilderDeckCardModel<CardModel>>) transformedBoardObject;
                            deckBuilderAppState.getBoard().triggerEvent(new MoveCardEvent(inspectionCard, inspectionZone, new Vector3f()));
                            inspectionCard.finishTransformations();
                            inspectionCard.getModel().set(deckCard.getModel().getCardModel());
                            inspectionBackdrop.setCullHint(Spatial.CullHint.Inherit);
                            collectionGuiNode.setCullHint(Spatial.CullHint.Always);
                        }

                        @Override
                        public boolean isReadyToUninspect() {
                            return true;
                        }

                        @Override
                        public void uninspect() {
                            deckBuilderAppState.getBoard().unregister(inspectionCard);
                            inspectionBackdrop.setCullHint(Spatial.CullHint.Always);
                            collectionGuiNode.setCullHint(Spatial.CullHint.Inherit);
                        }
                    })
                    .build())
                .build())
            .collectionCards(collectionCards)
            .collectionZone(collectionZone)
            .collectionCardVisualizer(IngameCardVisualizer.forCollection())
            .collectionCardAmountVisualizer(collectionCardAmountVisualizer)
            .collectionCardsPerRow(4)
            .collectionRowsPerPage(2)
            .build();

        HashMap<CardModel, Integer> deck = new HashMap<>();
        for (CardListCard cardListCard : this.deck.getDeckCardList().getCards()) {
            CardModel cardModel = cardsToCardModelsMap.get(getCardListCardKey(cardListCard));
            deck.put(cardModel, cardListCard.getAmount());
        }

        deckBuilderAppState = new CollectionDeckBuilderAppState<>(mainApplication.getRootNode(), settings) {

            @Override
            protected void initialize(Application app) {
                super.initialize(app);
                setCollectionCardOrder(cardOrder);
                setDeck(deck);
            }

            @Override
            protected void initBoard() {
                super.initBoard();
                inspectionZone = new SimpleIntervalZone(new Vector3f(-1.97f, 0.2f, 0), Quaternion.IDENTITY, Vector3f.UNIT_XYZ);
                board.addZone(inspectionZone);
                board.registerVisualizer_ZonePosition(
                    zonePosition -> zonePosition.getZone() == inspectionZone,
                    IngameCardVisualizer.forCollection()
                );
            }
        };
        mainApplication.getStateManager().attach(deckBuilderAppState);

        // Inspection

        inspectionCard = new Card<>(new CardModel());
        inspectionBackdrop = new Geometry("inspectionBackdrop", new Quad(16.37f, 9.93f));
        inspectionBackdrop.setLocalTranslation(-10.17f, 0.1f, 4.97f);
        inspectionBackdrop.setLocalRotation(new Quaternion().fromAngleAxis(-1 * FastMath.HALF_PI, Vector3f.UNIT_X));
        Material materialInspectionBackdrop = new Material(mainApplication.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        materialInspectionBackdrop.setColor("Color", new ColorRGBA(0, 0, 0, 0.75f));
        materialInspectionBackdrop.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        inspectionBackdrop.setMaterial(materialInspectionBackdrop);
        inspectionBackdrop.setQueueBucket(RenderQueue.Bucket.Transparent);
        inspectionBackdrop.setCullHint(Spatial.CullHint.Always);
        mainApplication.getRootNode().attachChild(inspectionBackdrop);
    }

    private String getCardListCardKey(CardListCard cardListCard) {
        return cardListCard.getCard().getId() + "_" + cardListCard.getFoil().getId();
    }

    private void initGui() {
        float rightColumnWidth = 293;
        float rightColumnX = width - 56 - rightColumnWidth;

        // Title
        BitmapFont guiFont = mainApplication.getAssetManager().loadFont("Interface/Fonts/Default.fnt");
        textTitle = new BitmapText(guiFont);
        textTitle.setSize(20);
        float x = 56;
        float y = (mainApplication.getContext().getSettings().getHeight() - 30);
        textTitle.setLocalTranslation(x, y, 0);
        guiNode.attachChild(textTitle);

        // Deck name
        y += 5;
        Label labelName = new Label("Deck name:");
        labelName.setLocalTranslation(rightColumnX, y, 0);
        labelName.setColor(ColorRGBA.White);
        guiNode.attachChild(labelName);
        y -= 20;
        textFieldName = new TextField(deck.getDeckCardList().getName());
        textFieldName.setLocalTranslation(rightColumnX, y, 0);
        textFieldName.setPreferredWidth(rightColumnWidth);
        textFieldName.setFontSize(16);
        guiNode.attachChild(textFieldName);

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
        buttonFilterManaCost = new Button[11];
        for (int i = 0; i < buttonFilterManaCost.length; i++) {
            final int manaCost = i;
            Predicate<CardModel> filter = cardModel -> cardModel.getManaCostDetails() == manaCost;
            Button button = addButton("" + i, buttonManaFilterWidth, GuiUtil.BUTTON_HEIGHT_DEFAULT, b -> {
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
        Button buttonSave = addButton("Save", rightColumnWidth, GuiUtil.BUTTON_HEIGHT_DEFAULT, b -> saveDeck());
        buttonSave.setLocalTranslation(rightColumnX, 86 + GuiUtil.BUTTON_HEIGHT_DEFAULT, 0);

        // Back
        Button buttonBack = addButton("Back", rightColumnWidth, GuiUtil.BUTTON_HEIGHT_DEFAULT, b -> switchTo(new DecksAppState()));
        buttonBack.setLocalTranslation(rightColumnX, 18 + GuiUtil.BUTTON_HEIGHT_DEFAULT, 0);

        updateGui();
    }

    private void initListeners() {
        InputManager inputManager = mainApplication.getInputManager();
        inputManager.addMapping("left", new KeyTrigger(KeyInput.KEY_LEFT));
        inputManager.addMapping("right", new KeyTrigger(KeyInput.KEY_RIGHT));
        inputManager.addListener(this, "left", "right");
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
        textTitle.setText("Deckbuilder (" + deckBuilderAppState.getDeckSize() + "/" + GameConstants.MAXIMUM_DECK_SIZE + ")");
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
        if (deckBuilderAppState.getCollectionPage() > 0) {
            deckBuilderAppState.goToPreviousCollectionPage();
            updateGui();
        }
    }

    private void goToNextPage() {
        if (deckBuilderAppState.getCollectionPage() < (deckBuilderAppState.getCollectionPagesCount() - 1)) {
            deckBuilderAppState.goToNextCollectionPage();
            updateGui();
        }
    }

    private void updateGui() {
        buttonPreviousPage.setCullHint(deckBuilderAppState.getCollectionPage() > 0 ? Spatial.CullHint.Inherit : Spatial.CullHint.Always);
        buttonNextPage.setCullHint(deckBuilderAppState.getCollectionPage() < (deckBuilderAppState.getCollectionPagesCount() - 1) ? Spatial.CullHint.Inherit : Spatial.CullHint.Always);
        for (int i = 0; i < buttonFilterManaCost.length; i++) {
            boolean isSelected = ((filteredManaCost != null) && (filteredManaCost == i));
            buttonFilterManaCost[i].setColor(isSelected ? ColorRGBA.Green : ColorRGBA.White);
        }
    }

    private void saveDeck() {
        LinkedList<NewCardListCard> cards = new LinkedList<>();
        for (Map.Entry<CardModel, Integer> entry : deckBuilderAppState.getDeck().entrySet()) {
            CardListCard cardListCard = cardModelsToCardsMap.get(entry.getKey());
            cards.add(new NewCardListCard(cardListCard.getCard().getId(), cardListCard.getFoil().getId(), entry.getValue()));
        }
        String name = textFieldName.getText();
        if (name.isEmpty()) {
            name = null;
        }
        getModule(GameDataClientModule.class).updateDeck(mode, deck, name, cards);
        mainApplication.getStateManager().attach(new LoadingAppState() {

            @Override
            protected boolean shouldClose() {
                return (getModule(GameDataClientModule.class).getUser() != null);
            }
        });
    }

    @Override
    public void cleanup() {
        super.cleanup();
        mainApplication.getInputManager().deleteMapping("left");
        mainApplication.getInputManager().deleteMapping("right");
        mainApplication.getInputManager().removeListener(this);
        mainApplication.getRootNode().detachChild(inspectionBackdrop);
        mainApplication.getStateManager().detach(deckBuilderAppState);
        mainApplication.getRootNode().removeLight(ambientLight);
        mainApplication.getRootNode().removeLight(directionalLight);
        getAppState(BackgroundAppState.class).resetBackground();
    }
}