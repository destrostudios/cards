package com.destrostudios.cards.frontend.application.appstates.services.cardpainter;

import com.destrostudios.cards.frontend.application.appstates.services.cardpainter.model.CardModel;
import com.destrostudios.cards.frontend.application.appstates.services.cardpainter.model.Cost;
import com.destrostudios.cards.frontend.application.appstates.services.cardpainter.model.Spell;
import com.destrostudios.cards.frontend.application.appstates.services.cardpainter.model.StatModification;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;
import java.util.List;

public class CardPainterAWT {

    private static final int fontSizeText = 20;
    private static final Font fontTitle = new Font("Tahoma", Font.BOLD, fontSizeText);
    private static final Font fontDescription = new Font("Tahoma", Font.PLAIN, fontSizeText);
    private static final Font fontKeywords = new Font("Tahoma", Font.BOLD, fontSizeText);
    private static final Font fontTribes = new Font("Tahoma", Font.PLAIN, fontSizeText);
    private static final Font fontsManaCostLarge = new Font("Tahoma", Font.BOLD, 100);
    private static final Font fontsManaCostSmall = new Font("Tahoma", Font.BOLD, 30);
    private static final Font fontStatsLarge = new Font("Tahoma", Font.BOLD, 100);
    private static final Font fontStatsSmall = new Font("Tahoma", Font.BOLD, 50);

    private static final int lineWidth = 306;
    private static final int lineHeight = 24;

    private static int tmpX;
    private static int tmpY;

    public static void drawCardBack(Graphics2D graphics) {
        graphics = (Graphics2D) graphics.create();
        graphics.drawImage(CardImages.getCachedImage("images/cardbacks/yugioh.png"), 0, 0, CardPainter.TEXTURE_WIDTH, CardPainter.TEXTURE_HEIGHT, null);
        graphics.dispose();
    }

    public static void drawCardFront_Full_Content(Graphics2D graphics, CardModel cardModel) {
        graphics = (Graphics2D) graphics.create();
        List<String> drawnKeywords = new LinkedList<>();
        drawnKeywords.addAll(cardModel.getKeywords());
        graphics.drawImage(CardImages.getCachedImage("images/templates/template_" + cardModel.getType() + ".png"), 0, 0, CardPainter.TEXTURE_WIDTH, CardPainter.TEXTURE_HEIGHT, null);
        graphics.setFont(fontTitle);
        graphics.setColor(Color.BLACK);
        String title = cardModel.getTitle();
        int textStartX = 43;
        if (title != null) {
            graphics.drawString(title, textStartX, 60);
        }
        tmpY = 350;
        if(drawnKeywords.size() > 0){
            String keywordsText = "";
            for(int i=0;i<drawnKeywords.size();i++){
                if(i != 0){
                    keywordsText += " ";
                }
                String keyword = drawnKeywords.get(i);
                keywordsText += keyword + (keyword.equals("Cast")?":":".");
            }
            tmpX = textStartX;
            graphics.setFont(fontKeywords);
            drawStringMultiLine(graphics, keywordsText, lineWidth, tmpX, textStartX, tmpY, -2);
            tmpY += lineHeight;
        }
        graphics.setFont(fontDescription);
        String description = cardModel.getDescription();
        if(description != null){
            tmpX = textStartX;
            drawStringMultiLine(graphics, description, lineWidth, tmpX, textStartX, tmpY, -2);
            tmpY += lineHeight;
        }
        List<Spell> spells = cardModel.getSpells();
        if (spells != null) {
            for (Spell spell : spells) {
                tmpX = textStartX;
                drawSpellDescription(graphics, spell.getCost(), spell.getDescription(), lineWidth, tmpX, textStartX, tmpY);
                tmpY += lineHeight;
            }
        }
        String flavourText = cardModel.getFlavourText();
        if(flavourText != null){
            tmpX = textStartX;
            drawStringMultiLine(graphics, flavourText, lineWidth, tmpX, textStartX, tmpY, -2);
            tmpY += lineHeight;
        }
        if (cardModel.getTribes().size() > 0) {
            graphics.setFont(fontTribes);
            String tribesText = String.join(", ", cardModel.getTribes());
            Rectangle2D tribesBounds = graphics.getFontMetrics().getStringBounds(tribesText, graphics);
            tmpX = (int) ((CardPainter.TEXTURE_WIDTH / 2) - (tribesBounds.getWidth() / 2));
            graphics.drawString(tribesText, tmpX, 501);
        }
        graphics.dispose();
    }

    public static void drawCardFront_Full_Artwork(Graphics2D graphics, CardModel cardModel) {
        graphics = (Graphics2D) graphics.create();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(35, 68, CardPainter.ARTWORK_WIDTH, CardPainter.ARTWORK_HEIGHT);
        String imageFilePath = CardImages.getCardImageFilePath(cardModel);
        Image cachedImage = CardImages.getCachedImage(imageFilePath, CardPainter.ARTWORK_WIDTH, CardPainter.ARTWORK_HEIGHT);
        graphics.drawImage(cachedImage, 35, 68, null);
        graphics.dispose();
    }

    public static void drawCardFront_Minified_Artwork(Graphics2D graphics, CardModel cardModel) {
        graphics = (Graphics2D) graphics.create();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, CardPainter.TEXTURE_WIDTH, CardPainter.TEXTURE_HEIGHT);
        String imageFilePath = CardImages.getCardImageFilePath(cardModel);
        graphics.drawImage(CardImages.getCachedImage(imageFilePath, CardPainter.ARTWORK_WIDTH_FULL, CardPainter.TEXTURE_HEIGHT), -181, 0, null);
        graphics.dispose();
    }

    public static void drawCardFront_Stats(Graphics2D graphics, CardModel cardModel, boolean fullArt) {
        graphics = (Graphics2D) graphics.create();
        drawStats(graphics, cardModel, fullArt);
        graphics.dispose();
    }

    public static void drawCardFront_CardCost(Graphics2D graphics, CardModel cardModel, boolean fullArt) {
        graphics = (Graphics2D) graphics.create();
        drawCardCost(graphics, cardModel, fullArt);
        graphics.dispose();
    }

    private static void drawCardCost(Graphics2D graphics, CardModel cardModel, boolean fullArt) {
        tmpY = (fullArt ? 120 : 62);
        int outlineStrength = (fullArt ? 3 : 1);
        Font font = (fullArt ? fontsManaCostLarge : fontsManaCostSmall);
        Integer manaCost = (fullArt ? cardModel.getManaCostFullArt() : cardModel.getManaCostDetails());
        if (manaCost != null) {
            String manaCostText = ("" + manaCost);
            graphics.setFont(font);
            Rectangle2D manaCostTextBounds = graphics.getFontMetrics().getStringBounds(manaCostText, graphics);
            int centerX = (fullArt ? 335 : 350);
            tmpX = (int) (centerX - (manaCostTextBounds.getWidth() / 2));
            Color color = ((cardModel.getManaCostModification() == StatModification.DECREASED) ? Color.GREEN : ((cardModel.getManaCostModification() == StatModification.INCREASED) ? Color.RED : Color.WHITE));
            AWTUtil.drawOutlinedText(graphics, manaCostText, tmpX, tmpY, Color.BLACK, color, outlineStrength);
        }
    }

    private static void drawStats(Graphics2D graphics, CardModel cardModel, boolean fullArt) {
        tmpY = 513;
        int outlineStrength = (fullArt ? 3 : 1);
        Font font = (fullArt ? fontStatsLarge : fontStatsSmall);
        Integer attack = cardModel.getAttack();
        if (attack != null) {
            String attackText = ("" + attack);
            graphics.setFont(font);
            Rectangle2D attackBounds = graphics.getFontMetrics().getStringBounds(attackText, graphics);
            int centerX = (fullArt ? 65 : 60);
            tmpX = (int) (centerX - (attackBounds.getWidth() / 2));
            Color color = ((cardModel.getAttackModification() == StatModification.INCREASED) ? Color.GREEN : Color.WHITE);
            AWTUtil.drawOutlinedText(graphics, attackText, tmpX, tmpY, Color.BLACK, color, outlineStrength);
        }
        Integer health = cardModel.getHealth();
        if (health != null) {
            String healthText = ("" + health);
            graphics.setFont(font);
            Rectangle2D healthBounds = graphics.getFontMetrics().getStringBounds(healthText, graphics);
            int centerX = (fullArt ? 335 : 340);
            tmpX = (int) (centerX - (healthBounds.getWidth() / 2));
            Color color = (cardModel.isDamaged() ? Color.RED : ((cardModel.getHealthModification() == StatModification.INCREASED) ? Color.GREEN : Color.WHITE));
            AWTUtil.drawOutlinedText(graphics, healthText, tmpX, tmpY, Color.BLACK, color, outlineStrength);
        }
    }

    private static void drawSpellDescription(Graphics2D graphics, Cost cost, String description, int lineWidth, int startX, int followingX, int y){
        graphics.setFont(fontDescription);
        String text = "";
        if ((cost != null) && (cost.getManaCost() != null)) {
            text += "(" + cost.getManaCost() + "): ";
        }
        text += description;
        drawStringMultiLine(graphics, text, lineWidth, startX, followingX, y, -2);
    }

    // http://stackoverflow.com/questions/4413132/problems-with-newline-in-graphics2d-drawstring
    public static void drawStringMultiLine(Graphics2D graphics, String text, int lineWidth, int startX, int followingX, int y, int linesGap){
        FontMetrics fontMetrics = graphics.getFontMetrics();
        int x = startX;
        String currentLine = text;
        if(fontMetrics.stringWidth(currentLine) < (lineWidth - (x - followingX))){
            graphics.drawString(currentLine, x, y);
        }
        else{
            String[] words = text.split(" ");
            currentLine = words[0];
            for(int i=1;i<words.length;i++){
                if(fontMetrics.stringWidth(currentLine + words[i]) < (lineWidth - (x - followingX))){
                    currentLine += " " + words[i];
                }
                else{
                    graphics.drawString(currentLine, x, y);
                    y += (fontMetrics.getHeight() + linesGap);
                    x = followingX;
                    currentLine = words[i];
                }
            }
            if(currentLine.trim().length() > 0){
                graphics.drawString(currentLine, x, y);
            }
        }
        x += fontMetrics.stringWidth(currentLine);
        tmpX = x;
        tmpY = y;
    }
}
