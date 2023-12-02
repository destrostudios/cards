package com.destrostudios.cards.frontend.application.appstates.menu;

import com.destrostudios.cards.frontend.application.FrontendJmeApplication;
import com.destrostudios.cards.frontend.application.gui.GuiComponent;
import com.destrostudios.cards.frontend.application.gui.GuiUtil;
import com.destrostudios.cards.frontend.application.modules.GameDataClientModule;
import com.destrostudios.cards.shared.model.Mode;
import com.jme3.math.ColorRGBA;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Label;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class ModeSelector extends GuiComponent {

    public ModeSelector(boolean onlyModesWithUserDecks) {
        this.onlyModesWithUserDecks = onlyModesWithUserDecks;
    }
    private boolean onlyModesWithUserDecks;
    private HashMap<Mode, Button> modeButtons = new HashMap<>();
    @Getter
    private Mode mode;

    @Override
    public void init(FrontendJmeApplication mainApplication) {
        super.init(mainApplication);
        addLabel("Mode:", 0);
        initModes();
    }

    protected void addLabel(String text, float y) {
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
        if (onlyModesWithUserDecks) {
            modes = modes.stream().filter(Mode::isHasUserDecks).toList();
        }
        for (Mode mode : modes) {
            Button button = GuiUtil.createButton(mode.getTitle(), buttonWidth, GuiUtil.BUTTON_HEIGHT_DEFAULT, b -> selectMode(mode));
            button.setLocalTranslation(x, y, 0);
            guiNode.attachChild(button);
            modeButtons.put(mode, button);
            x += (buttonWidth + gap);
        }
        selectMode(modes.get(0));
    }

    protected void selectMode(Mode mode) {
        if (mode != this.mode) {
            if (this.mode != null) {
                setButtonSelected(modeButtons.get(this.mode), false);
            }
            this.mode = mode;
            if (mode != null) {
                setButtonSelected(modeButtons.get(mode), true);
            }
            onModeSelected();
        }
    }

    protected void onModeSelected() {

    }

    protected void setButtonSelected(Button button, boolean selected) {
        GuiUtil.setButtonBackground(button, (selected ? GuiUtil.BUTTON_COLOR_SELECTED : null));
    }

    public void setEnabled(boolean enabled) {
        setNonSelectedButtonsEnabled(modeButtons, m -> m == mode, enabled);
    }

    protected static <K> void setNonSelectedButtonsEnabled(Map<K, Button> buttons, Predicate<K> isSelected, boolean enabled) {
        buttons.forEach((key, button) -> {
            if (!isSelected.test(key)) {
                GuiUtil.setButtonEnabled(button, enabled);
            }
        });
    }
}
