package com.destrostudios.cards.frontend.cardpainter;

import com.destrostudios.cards.frontend.cardpainter.model.CardModel;
import com.destrostudios.cards.frontend.cardpainter.model.Cost;
import com.destrostudios.cards.frontend.cardpainter.model.ManaCost;
import com.destrostudios.cards.frontend.cardpainter.model.Spell;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class CardPainterAWT {

    private static final Font fontTitle = new Font("Tahoma", Font.BOLD, 18);
    private static final Font fontDescription = new Font("Tahoma", Font.PLAIN, 13);
    private static final Font fontKeywords = new Font("Tahoma", Font.BOLD, 13);
    private static final Font fontEffects = new Font("Tahoma", Font.PLAIN, 13);
    private static final Font fontFlavorText = new Font("Tahoma", Font.ITALIC, 13);
    private static final Font fontTribes = new Font("Tahoma", Font.BOLD, 13);
    private static final Font fontStats = new Font("Tahoma", Font.BOLD, 50);

    private static final int lineWidth = 306;
    private static final int cardCostIconSize = 24;
    private static final int cardCostGapSize = 2;
    private static final int effectsIconSize = 15;
    private static final int effectsGapSize = 2;

    private static int tmpX;
    private static int tmpY;
    public static void drawCard(Graphics2D graphics, CardModel cardModel, int width, int height) {
        graphics = (Graphics2D) graphics.create();
        if (cardModel.isFront()) {
            List<String> drawnKeywords = new LinkedList<>();
            drawnKeywords.addAll(cardModel.getKeywords());
            String castDescription = cardModel.getCastDescription();
            if (castDescription != null) {
                drawnKeywords.add("Cast");
            }
            graphics.setColor(Color.WHITE);
            graphics.fillRect(35, 68, 329, 242);
            String imageFilePath = CardImages.getCardImageFilePath(cardModel);
            graphics.drawImage(CardImages.getCachedImage(imageFilePath, 329, 242), 35, 68, null);
            List<com.destrostudios.cards.frontend.cardpainter.model.Color> colors = cardModel.getColors();
            graphics.drawImage(getCardBackgroundImage(colors, width, height), 0, 0, null);
            graphics.setFont(fontTitle);
            graphics.setColor(Color.BLACK);
            String title = cardModel.getTitle();
            int textStartX = 45;
            if (title != null) {
                graphics.drawString(title, 45, 54);
            }
            if (cardModel.getManaCost() != null) {
                drawCardCostManaAmount(graphics, cardModel.getManaCost(), (width - textStartX - cardCostIconSize), (46 - (cardCostIconSize / 2)));
            }
            tmpY = 370;
            if(drawnKeywords.size() > 0){
                String keywordsText = "";
                for(int i=0;i<drawnKeywords.size();i++){
                    if(i != 0){
                        keywordsText += " ";
                    }
                    String keyword = drawnKeywords.get(i);
                    keywordsText += keyword + (keyword.equals("Cast")?":":".");
                }
                graphics.setFont(fontKeywords);
                tmpX = textStartX;
                drawStringMultiLine(graphics, keywordsText, lineWidth, tmpX, textStartX, tmpY, -2);
                if(castDescription != null){
                    tmpX += 3;
                    drawSpellDescription(graphics, castDescription, lineWidth, tmpX, textStartX, tmpY);
                }
                tmpY += 18;
            }
            String description = cardModel.getDescription();
            if(description != null){
                graphics.setFont(fontDescription);
                tmpX = textStartX;
                drawStringMultiLine(graphics, description, 180, tmpX, textStartX, tmpY, -2);
                tmpY += 18;
            }
            List<Spell> spells = cardModel.getSpells();
            for (Spell spell : spells) {
                tmpX = textStartX;
                if (spell.getCost() != null) {
                    drawCost(graphics, spell.getCost(), lineWidth, tmpX, textStartX, tmpY);
                    tmpX += 3;
                }
                drawSpellDescription(graphics, spell.getDescription(), lineWidth, tmpX, textStartX, tmpY);
                tmpY += 18;
            }
            String flavourText = cardModel.getFlavourText();
            if(flavourText != null){
                tmpX = textStartX;
                graphics.setFont(fontFlavorText);
                drawStringMultiLine(graphics, flavourText, lineWidth, tmpX, textStartX, tmpY, -2);
                tmpY += 18;
            }
            tmpY = 513;
            Integer attackDamage = cardModel.getAttackDamage();
            if (attackDamage != null) {
                graphics.drawImage(CardImages.getCachedImage("images/templates/stat.png"), 29, 458, 73, 73, null);
                String attackDamageText = ("" + attackDamage);
                graphics.setFont(fontStats);
                Rectangle2D attackDamageBounds = graphics.getFontMetrics().getStringBounds(attackDamageText, graphics);
                tmpX = (int) (65 - (attackDamageBounds.getWidth() / 2));
                drawOutlinedText(graphics, attackDamageText, tmpX, tmpY, Color.BLACK, Color.WHITE);
            }
            Integer lifepoints = cardModel.getLifepoints();
            if (lifepoints != null) {
                graphics.drawImage(CardImages.getCachedImage("images/templates/stat.png"), 298, 458, 73, 73, null);
                String lifepointsText = ("" + lifepoints);
                graphics.setFont(fontStats);
                Rectangle2D lifepointsBounds = graphics.getFontMetrics().getStringBounds(lifepointsText, graphics);
                tmpX = (int) (335 - (lifepointsBounds.getWidth() / 2));
                drawOutlinedText(graphics, lifepointsText, tmpX, tmpY, Color.BLACK, (cardModel.isDamaged() ? Color.RED : Color.WHITE));
            }
            List<String> tribes = cardModel.getTribes();
            if (tribes.size() > 0) {
                String tribesText = "";
                for (int i = 0; i < tribes.size(); i++) {
                    if (i != 0) {
                        tribesText += ", ";
                    }
                    tribesText += tribes.get(i);
                }
                graphics.setFont(fontTribes);
                graphics.setColor(Color.BLACK);
                graphics.drawString(tribesText, textStartX, 334);
            }
        }
        else{
            graphics.drawImage(CardImages.getCachedImage("images/back.png"), 0, 0, 300, 400, null);
        }
        graphics.dispose();
    }

    private static void drawOutlinedText(Graphics2D graphics, String text, int x, int y, Color outlineColor, Color textColor) {
        graphics.setColor(outlineColor);
        graphics.drawString(text, x - 1, y - 1);
        graphics.drawString(text, x + 0, y - 1);
        graphics.drawString(text, x + 1, y - 1);
        graphics.drawString(text, x - 1, y + 0);
        graphics.drawString(text, x + 0, y + 0);
        graphics.drawString(text, x + 1, y + 0);
        graphics.drawString(text, x - 1, y + 1);
        graphics.drawString(text, x + 0, y + 1);
        graphics.drawString(text, x + 1, y + 1);
        graphics.setColor(textColor);
        graphics.drawString(text, x, y);
    }

    private static HashMap<String, BufferedImage> cardBackgroundImages = new HashMap<>();
    public static BufferedImage getCardBackgroundImage(List<com.destrostudios.cards.frontend.cardpainter.model.Color> colors, int width, int height){
        String key = "";
        for(int i=0;i<colors.size();i++){
            if(i != 0){
                key += ",";
            }
            key += colors.get(i);
        }
        BufferedImage image = cardBackgroundImages.get(key);
        if(image == null){
            int partWidth = Math.round(((float) width) / colors.size());
            image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D imageGraphics = image.createGraphics();
            int x = 0;
            int lineX;
            for(com.destrostudios.cards.frontend.cardpainter.model.Color color : colors){
                for(int i=(int) (-0.5f * partWidth);i<(1.5f * partWidth);i++){
                    lineX = (x + i);
                    if(lineX > 0){
                        if(lineX >= width){
                            break;
                        }
                        Image templateImage = CardImages.getCachedImage("images/templates/template_" + color.ordinal() + ".png");
                        float alpha;
                        if(((i < (0.5 * partWidth)) && (lineX < (partWidth / 2))) || (i > (0.5 * partWidth))){
                            alpha = 1;
                        }
                        else{
                            alpha = (1 - (Math.abs(((((float) i) / partWidth) - 0.5f))));
                        }
                        imageGraphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
                        while(!imageGraphics.drawImage(templateImage, lineX, 0, lineX + 1, height, lineX, 0, lineX + 1, height, null)){
                            //http://stackoverflow.com/questions/20442295/drawimage-wont-work-but-drawrect-does
                        }
                    }
                }
                x += partWidth;
            }
            imageGraphics.dispose();
            cardBackgroundImages.put(key, image);
        }
        return image;
    }

    private static void drawSpellDescription(Graphics2D graphics, String description, int lineWidth, int startX, int followingX, int y){
        graphics.setFont(fontEffects);
        drawStringMultiLine(graphics, description, lineWidth, startX, followingX, y, -2);
    }

    private static void drawCost(Graphics2D graphics, Cost cost, int lineWidth, int startX, int followingX, int y){
        if(cost.isTap()){
            drawTapIcon(graphics, startX, y);
        }
        if (cost.getManaCost() != null) {
            drawSpellCostManaAmount(graphics, cost.getManaCost(), lineWidth, tmpX, followingX, y);
        }
    }

    private static void drawTapIcon(Graphics2D graphics, int x, int y){
        graphics.drawImage(CardImages.getCachedImage("images/tap.png", effectsIconSize, effectsIconSize), x, y - 12, effectsIconSize, effectsIconSize, null);
        x += (effectsIconSize + effectsGapSize);
        tmpX = x;
    }

    private static void drawSpellCostManaAmount(Graphics2D graphics, ManaCost manaCost, int lineWidth, int startX, int followingX, int y){
        tmpX = startX;
        tmpY = y;
        for(com.destrostudios.cards.frontend.cardpainter.model.Color color : com.destrostudios.cards.frontend.cardpainter.model.Color.values()){
            Integer amount = manaCost.get(color);
            if (amount != null) {
                for (int i = 0; i < amount; i++) {
                    if (tmpX > (followingX + lineWidth)) {
                        tmpX = followingX;
                        tmpY += 18;
                    }
                    graphics.drawImage(CardImages.getCachedImage("images/mana/" + color.ordinal() + ".png", effectsIconSize, effectsIconSize), tmpX, tmpY - 12, effectsIconSize, effectsIconSize, null);
                    tmpX += (effectsIconSize + effectsGapSize);
                }
            }
        }
    }

    private static void drawCardCostManaAmount(Graphics2D graphics, ManaCost manaCost, int endX, int y){
        tmpX = endX;
        tmpY = y;
        for(com.destrostudios.cards.frontend.cardpainter.model.Color color : com.destrostudios.cards.frontend.cardpainter.model.Color.values()){
            Integer amount = manaCost.get(color);
            if (amount != null) {
                for (int i = 0; i < amount; i++) {
                    graphics.drawImage(CardImages.getCachedImage("images/mana/" + color.ordinal() + ".png", cardCostIconSize, cardCostIconSize), tmpX, tmpY, cardCostIconSize, cardCostIconSize, null);
                    tmpX -= (cardCostGapSize + cardCostIconSize);
                }
            }
        }
    }

    //http://stackoverflow.com/questions/4413132/problems-with-newline-in-graphics2d-drawstring
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

    private static HashMap<String, BufferedImage> letterImages = new HashMap<>();
    private static BufferedImage getLetterImage(Graphics graphics, String letter){
        BufferedImage image = letterImages.get(letter);
        if(image == null){
            image = createStringImage(graphics, letter);
            letterImages.put(letter, image);
        }
        return image;
    }

    //http://stackoverflow.com/questions/10388118/how-to-make-rotated-text-look-good-with-java2d
    public static BufferedImage createStringImage(Graphics graphics, String text){
        int width = (graphics.getFontMetrics().stringWidth(text) + 5);
        int height = graphics.getFontMetrics().getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D imageGraphics = image.createGraphics();
        imageGraphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        imageGraphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        imageGraphics.setColor(Color.BLACK);
        imageGraphics.setFont(graphics.getFont());
        imageGraphics.drawString(text, 0, (height - graphics.getFontMetrics().getDescent()));
        imageGraphics.dispose();
        return image;
    }
}
