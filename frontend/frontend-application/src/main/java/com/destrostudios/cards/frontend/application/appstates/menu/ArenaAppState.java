package com.destrostudios.cards.frontend.application.appstates.menu;

import com.destrostudios.cardgui.CardZone;
import com.destrostudios.cardgui.samples.tools.deckbuilder.DeckBuilderSettings;
import com.destrostudios.cardgui.samples.tools.deckbuilder.draft.DraftDeckBuilderAppState;
import com.destrostudios.cardgui.samples.tools.deckbuilder.draft.DraftDeckBuilderSettings;
import com.destrostudios.cardgui.zones.CenteredIntervalZone;
import com.destrostudios.cards.frontend.application.appstates.services.IngameCardVisualizer;
import com.destrostudios.cards.frontend.application.appstates.services.cardpainter.model.CardModel;
import com.destrostudios.cards.frontend.application.gui.GuiUtil;
import com.destrostudios.cards.frontend.application.modules.GameDataClientModule;
import com.destrostudios.cards.frontend.application.modules.QueueClientModule;
import com.destrostudios.cards.shared.model.Card;
import com.destrostudios.cards.shared.model.Deck;
import com.destrostudios.cards.shared.model.Mode;
import com.destrostudios.cards.shared.model.Queue;
import com.destrostudios.cards.shared.model.internal.BaseCardIdentifier;
import com.destrostudios.cards.shared.rules.GameConstants;
import com.jme3.math.Vector3f;
import com.simsilica.lemur.Button;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class ArenaAppState extends CachedModelsDeckAppState<DraftDeckBuilderAppState<CardModel>> {

    private Button buttonPlay;
    private boolean waitingForUpdate = true;
    private boolean waitingForGame;

    @Override
    protected String getTitle() {
        return "Arena Deck";
    }

    @Override
    protected DraftDeckBuilderAppState<CardModel> createDeckBuilder(DeckBuilderSettings<CardModel> deckBuilderSettings, Comparator<CardModel> cardOrder) {
        CardZone draftZone = new CenteredIntervalZone(new Vector3f(-2, 0, 0), new Vector3f(3.65f, 1, 5));

        DraftDeckBuilderSettings<CardModel> settings = DraftDeckBuilderSettings.<CardModel>builder()
            .deckBuilderSettings(deckBuilderSettings)
            .draftZone(draftZone)
            .draftCardVisualizer(IngameCardVisualizer.forCollection())
            .build();

        return new DraftDeckBuilderAppState<>(mainApplication.getRootNode(), settings);
    }

    @Override
    protected boolean isAllowedToAddCard(CardModel cardModel) {
        return !waitingForUpdate;
    }

    @Override
    protected void onCardAdded(CardModel cardModel) {
        super.onCardAdded(cardModel);
        Card card = getCardIdentifier(cardModel).getCard();
        getModule(GameDataClientModule.class).addArenaCard(card);
        waitingForUpdate = true;
    }

    @Override
    protected void initGui(float rightColumnX, float rightColumnWidth) {
        super.initGui(rightColumnX, rightColumnWidth);

        // Play
        buttonPlay = addButton("Play", rightColumnWidth, GuiUtil.BUTTON_HEIGHT_DEFAULT, b -> play());
        buttonPlay.setLocalTranslation(rightColumnX, 86 + GuiUtil.BUTTON_HEIGHT_DEFAULT, 0);
    }

    @Override
    protected void back() {
        switchTo(new PlayAppState());
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
        if (waitingForUpdate && (getModule(GameDataClientModule.class).getUser() != null)) {
            updateCards();
        }
        boolean isLoading = !waitingForUpdate;
        GuiUtil.setButtonEnabled(buttonPlay, isLoading && isDeckComplete());
        GuiUtil.setButtonEnabled(buttonBack, isLoading);
    }

    private void updateCards() {
        Deck deck = getDeck();
        deckBuilderAppState.setDeck((deck != null) ? mapCardList(deck.getDeckCardList()) : new HashMap<>());
        deckBuilderAppState.setDraftCards(isDeckComplete() ? new LinkedList<>() : mapCards(getDraftCards()));
        waitingForUpdate = false;
    }

    private boolean isDeckComplete() {
        Deck deck = getDeck();
        return ((deck != null) && (deck.getDeckCardList().getSize() >= GameConstants.MAXIMUM_DECK_SIZE));
    }

    private void play() {
        if (!waitingForGame) {
            GameDataClientModule gameDataClientModule = getModule(GameDataClientModule.class);
            Mode modeArena = gameDataClientModule.getMode(GameConstants.MODE_NAME_ARENA);
            Queue queueBot = gameDataClientModule.getQueue(GameConstants.QUEUE_NAME_BOT);
            getModule(QueueClientModule.class).queue(modeArena, getDeck(), queueBot);
            GuiUtil.setButtonEnabled(buttonPlay, false);
            waitingForGame = true;
        }
    }

    private Deck getDeck() {
        return getModule(GameDataClientModule.class).getArenaDeck();
    }

    private List<BaseCardIdentifier> getDraftCards() {
        return getModule(GameDataClientModule.class).getUser().getArenaDraftCards();
    }
}
