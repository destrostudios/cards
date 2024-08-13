package com.destrostudios.cards.frontend.application.appstates.menu;

import com.destrostudios.cardgui.*;
import com.destrostudios.cardgui.events.MoveCardEvent;
import com.destrostudios.cardgui.samples.tools.deckbuilder.DeckBuilderAppState;
import com.destrostudios.cardgui.samples.tools.deckbuilder.DeckBuilderDeckCardModel;
import com.destrostudios.cardgui.samples.tools.deckbuilder.DeckBuilderSettings;
import com.destrostudios.cardgui.zones.SimpleIntervalZone;
import com.destrostudios.cards.frontend.application.CompositeComparator;
import com.destrostudios.cards.frontend.application.appstates.BackgroundAppState;
import com.destrostudios.cards.frontend.application.appstates.services.BoardUtil;
import com.destrostudios.cards.frontend.application.appstates.services.DeckBuilderCardVisualizer;
import com.destrostudios.cards.frontend.application.appstates.services.IngameCardVisualizer;
import com.destrostudios.cards.frontend.application.appstates.services.cardpainter.model.CardModel;
import com.destrostudios.cards.frontend.application.appstates.services.zones.DeckBuilderDeckZone;
import com.destrostudios.cards.frontend.application.gui.GuiUtil;
import com.destrostudios.cards.shared.rules.GameConstants;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
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
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Quad;
import com.simsilica.lemur.Button;

import java.util.Comparator;

public abstract class DeckAppState<DBAS extends DeckBuilderAppState<CardModel>> extends MenuAppState {

    private AmbientLight ambientLight;
    private DirectionalLight directionalLight;
    protected DBAS deckBuilderAppState;
    private SimpleIntervalZone inspectionZone;
    private Card<CardModel> inspectionCard;
    private Geometry inspectionBackdrop;
    protected BitmapText textTitle;
    protected Button buttonBack;

    @Override
    public void initialize(AppStateManager stateManager, Application application){
        super.initialize(stateManager, application);
        getAppState(BackgroundAppState.class).setBackground("deck");
        setTopDownCamera();
        initLight();
        initDeckBuilder();
        initGui();
    }

    private void initLight() {
        ambientLight = new AmbientLight(ColorRGBA.White);
        mainApplication.getRootNode().addLight(ambientLight);
        Vector3f lightDirection = new Vector3f(1, -5, -1).normalizeLocal();
        directionalLight = new DirectionalLight(lightDirection, ColorRGBA.White.mult(0.5f));
        mainApplication.getRootNode().addLight(directionalLight);
    }

    private void initDeckBuilder() {
        Comparator<CardModel> cardOrder = new CompositeComparator<>(
            Comparator.comparing(CardModel::getManaCostDetails),
            Comparator.comparing(CardModel::getTitle),
            Comparator.comparing(cardModel -> ((cardModel.getFoil() != null) ? cardModel.getFoil().ordinal() : -1))
        );
        deckBuilderAppState = createDeckBuilder(getDeckBuilderSettings(cardOrder), cardOrder);
        mainApplication.getStateManager().attach(deckBuilderAppState);

        // Inspection

        inspectionZone = new SimpleIntervalZone(new Vector3f(-1.97f, 0.2f, 0), Quaternion.IDENTITY, Vector3f.UNIT_XYZ);
        deckBuilderAppState.getBoard().addZone(inspectionZone);
        deckBuilderAppState.getBoard().registerVisualizer_ZonePosition(
            zonePosition -> zonePosition.getZone() == inspectionZone,
            IngameCardVisualizer.forCollection()
        );

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

    private DeckBuilderSettings<CardModel> getDeckBuilderSettings(Comparator<CardModel> cardOrder) {
        CardZone deckZone = new DeckBuilderDeckZone(new Vector3f(8.25f, 0, -5));
        DeckBuilderCardVisualizer deckCardVisualizer = new DeckBuilderCardVisualizer();
        return DeckBuilderSettings.<CardModel>builder()
            .deckZone(deckZone)
            .deckCardVisualizer(deckCardVisualizer)
            .deckCardOrder(cardOrder)
            .deckCardsMaximumTotal(GameConstants.MAXIMUM_DECK_SIZE)
            .deckCardsMaximumUnique(this::getMaximumUniqueDeckCards)
            .isAllowedToAddCard(this::isAllowedToAddCard)
            .cardAddedCallback(this::onCardAdded)
            .boardSettings(BoardUtil.getDefaultSettings("deckbuilder")
                .hoverInspectionDelay(0f)
                .isInspectable(transformedBoardObject -> (transformedBoardObject instanceof Card card) && (card.getZonePosition().getZone() == deckZone))
                .inspector(new Inspector() {

                    @Override
                    public void inspect(BoardAppState boardAppState, TransformedBoardObject<?> transformedBoardObject, Vector3f vector3f) {
                        Card<DeckBuilderDeckCardModel<CardModel>> deckCard = (Card<DeckBuilderDeckCardModel<CardModel>>) transformedBoardObject;
                        DeckAppState.this.inspect(deckCard.getModel().getCardModel());
                    }

                    @Override
                    public boolean isReadyToUninspect() {
                        return true;
                    }

                    @Override
                    public void uninspect() {
                        DeckAppState.this.uninspect();
                    }
                })
                .build())
            .build();
    }

    protected Integer getMaximumUniqueDeckCards(CardModel cardModel) {
        return null;
    }

    protected boolean isAllowedToAddCard(CardModel cardModel) {
        return true;
    }

    protected void onCardAdded(CardModel cardModel) {

    }

    protected void inspect(CardModel cardModel) {
        deckBuilderAppState.getBoard().triggerEvent(new MoveCardEvent(inspectionCard, inspectionZone, new Vector3f()));
        inspectionCard.finishTransformations();
        inspectionCard.getModel().set(cardModel);
        inspectionBackdrop.setCullHint(Spatial.CullHint.Inherit);
    }

    protected void uninspect() {
        deckBuilderAppState.getBoard().unregister(inspectionCard);
        inspectionBackdrop.setCullHint(Spatial.CullHint.Always);
    }

    protected abstract DBAS createDeckBuilder(DeckBuilderSettings<CardModel> deckBuilderSettings, Comparator<CardModel> cardOrder);

    private void initGui() {
        float rightColumnWidth = 293;
        float rightColumnX = width - 56 - rightColumnWidth;

        initGui(rightColumnX, rightColumnWidth);
    }

    protected void initGui(float rightColumnX, float rightColumnWidth) {
        // Title
        BitmapFont guiFont = mainApplication.getAssetManager().loadFont("Interface/Fonts/Default.fnt");
        textTitle = new BitmapText(guiFont);
        textTitle.setSize(20);
        float x = 56;
        float y = (mainApplication.getContext().getSettings().getHeight() - 30);
        textTitle.setLocalTranslation(x, y, 0);
        guiNode.attachChild(textTitle);

        // Back
        buttonBack = addButton("Back", rightColumnWidth, GuiUtil.BUTTON_HEIGHT_DEFAULT, b -> back());
        buttonBack.setLocalTranslation(rightColumnX, 18 + GuiUtil.BUTTON_HEIGHT_DEFAULT, 0);
    }

    protected abstract void back();

    @Override
    public void update(float tpf) {
        super.update(tpf);
        textTitle.setText(getTitle() + " (" + deckBuilderAppState.getDeckSize() + "/" + GameConstants.MAXIMUM_DECK_SIZE + ")");
    }

    protected abstract String getTitle();

    @Override
    public void cleanup() {
        super.cleanup();
        mainApplication.getRootNode().detachChild(inspectionBackdrop);
        mainApplication.getStateManager().detach(deckBuilderAppState);
        mainApplication.getRootNode().removeLight(ambientLight);
        mainApplication.getRootNode().removeLight(directionalLight);
        getAppState(BackgroundAppState.class).resetBackground();
    }
}