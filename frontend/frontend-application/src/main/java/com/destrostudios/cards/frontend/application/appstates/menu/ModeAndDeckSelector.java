package com.destrostudios.cards.frontend.application.appstates.menu;

import com.destrostudios.cards.frontend.application.FrontendJmeApplication;
import com.destrostudios.cards.frontend.application.gui.GuiComponent;
import com.destrostudios.cards.frontend.application.gui.GuiUtil;
import com.destrostudios.cards.frontend.application.modules.GameDataClientModule;
import com.destrostudios.cards.shared.model.CardList;
import com.destrostudios.cards.shared.model.Mode;
import com.destrostudios.cards.shared.model.UserModeDeck;
import com.destrostudios.cards.shared.rules.GameConstants;
import com.jme3.math.ColorRGBA;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Label;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;

public class ModeAndDeckSelector extends GuiComponent {

    private HashMap<Mode, Button> modeButtons = new HashMap<>();
    private HashMap<UserModeDeck, Button> deckButtons = new HashMap<>();
    @Getter
    private Mode mode;
    @Getter
    private UserModeDeck deck;

    @Override
    public void init(FrontendJmeApplication mainApplication) {
        super.init(mainApplication);
        addLabel("Mode:", 0);
        addLabel("Decks:", -100);
        initModes();
    }

    private void addLabel(String text, float y) {
        Label label = new Label(text);
        label.setFontSize(16);
        label.setLocalTranslation(0, y, 0);
        label.setColor(ColorRGBA.White);
        guiNode.attachChild(label);
    }

    private void initModes() {
        float gap = 10;
        float buttonWidth = 100;
        float x = 0;
        float y = -30;
        List<Mode> modes = getModule(GameDataClientModule.class).getModes();
        for (Mode mode : modes) {
            Button button = GuiUtil.createButton(mode.getTitle(), buttonWidth, GuiUtil.BUTTON_HEIGHT_DEFAULT, b -> selectMode(mode));
            button.setLocalTranslation(x, y, 0);
            guiNode.attachChild(button);
            modeButtons.put(mode, button);
            x += (buttonWidth + gap);
        }
        selectMode(modes.get(0));
    }

    private void selectMode(Mode mode) {
        if (mode != this.mode) {
            if (this.mode != null) {
                setButtonSelected(modeButtons.get(this.mode), false);
            }
            this.mode = mode;
            if (mode != null) {
                setButtonSelected(modeButtons.get(mode), true);
            }
            updateDecks();
        }
    }

    public void updateDecks() {
        List<UserModeDeck> decks = getModule(GameDataClientModule.class).getDecks(mode.getId());
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

    private void setButtonSelected(Button button, boolean selected) {
        GuiUtil.setButtonBackground(button, (selected ? ColorRGBA.Orange : null));
    }

    private String getDeckName(UserModeDeck deck) {
        CardList deckCardList = deck.getDeckCardList();
        String name = deckCardList.getName();
        return ((name != null) ? name : "Unnamed deck") + "\n(" + deckCardList.getSize() + "/" + GameConstants.MAXIMUM_DECK_SIZE + ")";
    }
}
