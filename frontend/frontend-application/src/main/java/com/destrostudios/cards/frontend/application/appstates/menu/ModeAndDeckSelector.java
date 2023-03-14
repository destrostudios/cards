package com.destrostudios.cards.frontend.application.appstates.menu;

import com.destrostudios.cards.frontend.application.FrontendJmeApplication;
import com.destrostudios.cards.frontend.application.gui.GuiUtil;
import com.destrostudios.cards.frontend.application.modules.GameDataClientModule;
import com.destrostudios.cards.shared.model.CardList;
import com.destrostudios.cards.shared.model.UserModeDeck;
import com.destrostudios.cards.shared.rules.GameConstants;
import com.simsilica.lemur.Button;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;

public class ModeAndDeckSelector extends ModeSelector {

    private HashMap<UserModeDeck, Button> deckButtons = new HashMap<>();
    @Getter
    private UserModeDeck deck;

    @Override
    public void init(FrontendJmeApplication mainApplication) {
        super.init(mainApplication);
        addLabel("Decks:", -100);
    }

    @Override
    protected void onModeSelected() {
        updateDecks();
    }

    public void updateDecks() {
        List<UserModeDeck> decks = getModule(GameDataClientModule.class).getDecks(getMode().getId());
        if (!decks.contains(deck)) {
            selectDeck(null);
        }
        deckButtons.values().forEach(button -> guiNode.detachChild(button));
        deckButtons.clear();
        int buttonsPerRow = 6;
        float margin = 50;
        float gap = 10;
        float buttonWidth = ((width - (2 * margin) - ((buttonsPerRow - 1) * gap)) / buttonsPerRow);
        float buttonHeight = 100;
        float x = 0;
        float y = -130;
        int i = 0;
        for (UserModeDeck deck : decks) {
            Button button = GuiUtil.createButton(getDeckName(deck), buttonWidth, buttonHeight, b -> selectDeck(deck));
            button.setLocalTranslation(x, y, 0);
            guiNode.attachChild(button);
            deckButtons.put(deck, button);
            x += (buttonWidth + gap);
            i++;
            if ((i % buttonsPerRow) == 0) {
                x = 0;
                y -= (buttonHeight + gap);
            }
        }
    }

    private String getDeckName(UserModeDeck deck) {
        CardList deckCardList = deck.getDeckCardList();
        String name = deckCardList.getName();
        return ((name != null) ? name : "Unnamed deck") + "\n(" + deckCardList.getSize() + "/" + GameConstants.MAXIMUM_DECK_SIZE + ")";
    }

    private void selectDeck(UserModeDeck deck) {
        if (deck != this.deck) {
            if (this.deck != null) {
                setButtonSelected(deckButtons.get(this.deck), false);
            }
            this.deck = deck;
            if (deck != null) {
                setButtonSelected(deckButtons.get(deck), true);
            }
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        setNonSelectedButtonsEnabled(deckButtons, d -> d == deck, enabled);
    }
}
