package com.destrostudios.cards.shared.rules.util;

public class MixedManaAmount {

    public MixedManaAmount() {
        this(0, 0, 0, 0, 0, 0);
    }

    public MixedManaAmount(int neutral, int white, int red, int green, int blue, int black) {
        this.neutral = neutral;
        this.white = white;
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.black = black;
    }
    private int neutral;
    private int white;
    private int red;
    private int green;
    private int blue;
    private int black;

    public boolean isNotEmpty() {
        return ((neutral > 0) || (white > 0) || (red > 0) || (green > 0) || (blue > 0) || (black > 0));
    }

    public int getNeutral() {
        return neutral;
    }

    public int getWhite() {
        return white;
    }

    public int getRed() {
        return red;
    }

    public int getGreen() {
        return green;
    }

    public int getBlue() {
        return blue;
    }

    public int getBlack() {
        return black;
    }

    @Override
    public String toString() {
        return "MixedManaAmount{" + "neutral=" + neutral + ", white=" + white + ", red=" + red + ", green=" + green + ", blue=" + blue + ", black=" + black + '}';
    }
}
