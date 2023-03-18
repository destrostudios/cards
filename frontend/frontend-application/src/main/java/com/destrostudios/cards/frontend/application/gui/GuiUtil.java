package com.destrostudios.cards.frontend.application.gui;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Command;
import com.simsilica.lemur.HAlignment;
import com.simsilica.lemur.VAlignment;
import com.simsilica.lemur.component.TbtQuadBackgroundComponent;

public class GuiUtil {

    public static final float BUTTON_HEIGHT_DEFAULT = 50;
    public static final ColorRGBA BUTTON_COLOR_SELECTED = new ColorRGBA(0.98f, 0.51f, 0, 0.8f);
    public static final ColorRGBA BUTTON_COLOR_HIGHLIGHT = new ColorRGBA(0, 0.51f, 0.98f, 0.7f);
    private static TbtQuadBackgroundComponent DEFAULT_BUTTON_BACKGROUND;

    public static Button createButton(String text, float width, float height, Command<Button> command) {
        Button button = new Button(text);
        button.setPreferredSize(new Vector3f(width, height, 0));
        button.setTextHAlignment(HAlignment.Center);
        button.setTextVAlignment(VAlignment.Center);
        button.setFontSize(16);
        button.setColor(ColorRGBA.White);
        button.setFocusColor(ColorRGBA.White);
        button.addCommands(Button.ButtonAction.Up, command);
        return button;
    }

    public static void setButtonEnabled(Button button, boolean enabled) {
        button.setEnabled(enabled);
        setButtonBackground(button, (enabled ? null : ColorRGBA.LightGray));
        if (!enabled) {
            ((TbtQuadBackgroundComponent) button.getBackground()).setAlpha(0.5f);
        }
    }

    public static void setButtonBackground(Button button, ColorRGBA backgroundColor) {
        TbtQuadBackgroundComponent background = getDefaultButtonBackground().clone();
        if (backgroundColor != null) {
            background.setColor(backgroundColor);
        }
        button.setBackground(background);
    }

    private static TbtQuadBackgroundComponent getDefaultButtonBackground() {
        if (DEFAULT_BUTTON_BACKGROUND == null) {
            DEFAULT_BUTTON_BACKGROUND = (TbtQuadBackgroundComponent) new Button(null).getBackground();
        }
        return DEFAULT_BUTTON_BACKGROUND;
    }
}
