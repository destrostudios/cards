package com.destrostudios.cards.frontend.application.appstates.menu;

import com.destrostudios.cardgui.CardZone;
import com.destrostudios.cardgui.samples.tools.deckbuilder.DeckBuilderSettings;
import com.destrostudios.cardgui.samples.tools.deckbuilder.draft.DraftDeckBuilderAppState;
import com.destrostudios.cardgui.samples.tools.deckbuilder.draft.DraftDeckBuilderSettings;
import com.destrostudios.cardgui.zones.CenteredIntervalZone;
import com.destrostudios.cards.frontend.application.appstates.services.IngameCardVisualizer;
import com.destrostudios.cards.frontend.application.appstates.services.cardpainter.model.CardModel;
import com.destrostudios.cards.frontend.application.gui.GuiUtil;
import com.destrostudios.cards.shared.model.CardIdentifier;
import com.destrostudios.cards.shared.model.Deck;
import com.jme3.math.Vector3f;
import com.simsilica.lemur.Button;

import java.util.Comparator;
import java.util.List;

public class ArenaAppState extends CachedModelsDeckAppState<DraftDeckBuilderAppState<CardModel>> {

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

    private void setDeck(Deck deck) {
        deckBuilderAppState.setDeck(mapCardList(deck.getDeckCardList()));
    }

    private void setDraftCards(List<CardIdentifier> draftCards) {
        deckBuilderAppState.setDraftCards(mapCards(draftCards));
    }

    @Override
    protected void initGui(float rightColumnX, float rightColumnWidth) {
        super.initGui(rightColumnX, rightColumnWidth);

        // Play
        Button buttonPlay = addButton("Play", rightColumnWidth, GuiUtil.BUTTON_HEIGHT_DEFAULT, b -> play());
        buttonPlay.setLocalTranslation(rightColumnX, 86 + GuiUtil.BUTTON_HEIGHT_DEFAULT, 0);
    }

    @Override
    protected void back() {
        switchTo(new PlayAppState());
    }

    private void play() {
        System.out.println("Play");
    }
}
