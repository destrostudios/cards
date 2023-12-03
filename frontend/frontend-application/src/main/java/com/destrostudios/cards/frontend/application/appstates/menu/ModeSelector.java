package com.destrostudios.cards.frontend.application.appstates.menu;

import com.destrostudios.cards.frontend.application.FrontendJmeApplication;
import com.destrostudios.cards.frontend.application.gui.GuiComponent;
import com.destrostudios.cards.frontend.application.gui.GuiUtil;
import com.destrostudios.cards.frontend.application.modules.GameDataClientModule;
import com.destrostudios.cards.shared.model.Mode;
import com.simsilica.lemur.Button;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;

public class ModeSelector extends GuiComponent {

    public ModeSelector() {
        this(null);
    }

    public ModeSelector(Predicate<Mode> modeFilter) {
        this.modeFilter = modeFilter;
    }
    private Predicate<Mode> modeFilter;
    private HashMap<Mode, Button> modeButtons = new HashMap<>();
    @Getter
    private Mode mode;

    @Override
    public void init(FrontendJmeApplication mainApplication) {
        super.init(mainApplication);
        addLabel("Mode:", 0);
        initModes();
    }

    private void initModes() {
        float gap = 10;
        float buttonWidth = 100;
        float x = 0;
        float y = -30;
        List<Mode> modes = getModule(GameDataClientModule.class).getModes();
        if (modeFilter != null) {
            modes = modes.stream().filter(modeFilter).toList();
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

    public void selectMode(Mode mode) {
        if (mode != this.mode) {
            if (this.mode != null) {
                setButtonSelected(modeButtons.get(this.mode), false);
            }
            this.mode = mode;
            if (mode != null) {
                setButtonSelected(modeButtons.get(mode), true);
            }
        }
    }
}
