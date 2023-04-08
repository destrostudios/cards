package com.destrostudios.cards.frontend.application.appstates.services.cardpainter;

import java.awt.*;

public class AWTUtil {

    public static void drawOutlinedText(Graphics2D graphics, String text, int x, int y, Color outlineColor, Color textColor, int outlineStrength) {
        graphics.setColor(outlineColor);
        graphics.drawString(text, x - outlineStrength, y - outlineStrength);
        graphics.drawString(text, x, y - outlineStrength);
        graphics.drawString(text, x + outlineStrength, y - outlineStrength);
        graphics.drawString(text, x - outlineStrength, y);
        graphics.drawString(text, x, y);
        graphics.drawString(text, x + outlineStrength, y);
        graphics.drawString(text, x - outlineStrength, y + outlineStrength);
        graphics.drawString(text, x, y + outlineStrength);
        graphics.drawString(text, x + outlineStrength, y + outlineStrength);
        graphics.setColor(textColor);
        graphics.drawString(text, x, y);
    }
}
