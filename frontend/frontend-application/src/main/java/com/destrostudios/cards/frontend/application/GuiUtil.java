package com.destrostudios.cards.frontend.application;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Command;
import com.simsilica.lemur.HAlignment;
import com.simsilica.lemur.VAlignment;

public class GuiUtil {

    public static final float BUTTON_HEIGHT_DEFAULT = 50;

    public static Button addButton(Node guiNode, String text, float width, float height, Command<Button> command) {
        Button button = new Button(text);
        button.setPreferredSize(new Vector3f(width, height, 0));
        button.setTextHAlignment(HAlignment.Center);
        button.setTextVAlignment(VAlignment.Center);
        button.setFontSize(16);
        button.setColor(ColorRGBA.White);
        button.setFocusColor(ColorRGBA.White);
        button.addCommands(Button.ButtonAction.Up, command);
        guiNode.attachChild(button);
        return button;
    }
}
