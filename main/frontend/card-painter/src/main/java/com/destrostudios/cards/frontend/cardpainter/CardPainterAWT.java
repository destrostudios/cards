package com.destrostudios.cards.frontend.cardpainter;

import com.destrostudios.cards.frontend.cardpainter.model.CardModel;
import com.destrostudios.cards.frontend.cardpainter.model.Spell;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
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
    private static final Font fontStats = new Font("Tahoma", Font.BOLD, 35);
    private Graphics2D graphics;

    private static int tmpX;
    private static int tmpY;
    public static void drawCard(Graphics2D graphics, ImageObserver imageObserver, CardModel cardModel){
        graphics = (Graphics2D) graphics.create();
        if(cardModel.isFront()){
            List<String> drawnKeywords = new LinkedList<>();
            if (cardModel.getKeywords() != null) {
                drawnKeywords.addAll(cardModel.getKeywords());
            }
            if (cardModel.getMechanics() != null) {
                drawnKeywords.addAll(cardModel.getMechanics());
            }
            boolean hasCastSpell = false;
            Spell castSpell = cardModel.getCastSpell();
            if(castSpell != null){
                drawnKeywords.add("Cast");
                hasCastSpell = true;
            }
            graphics.setColor(Color.WHITE);
            graphics.fillRect(74, 43, 155, 155);
            String imageFilePath = CardImages.getCardImageFilePath(cardModel);
            graphics.drawImage(CardImages.getCachedImage(imageFilePath, 155, 155), 74, 43, 155, 155, imageObserver);
            List<Integer> manaTypes = cardModel.getManaTypes();
            graphics.drawImage(getCardBackgroundImage(manaTypes), 0, 0, imageObserver);
            graphics.setFont(fontTitle);
            graphics.setColor(Color.BLACK);
            FontMetrics fontMetrics = graphics.getFontMetrics();
            if(fontMetrics.getHeight() > 0){
                String title = cardModel.getTitle();
                Rectangle2D titleBounds = fontMetrics.getStringBounds(title, graphics);
                int x = 150 - (int) (titleBounds.getWidth() / 2);
                int y;
                for(int i=0;i<title.length();i++){
                    String letter = title.substring(i, i + 1);
                    Rectangle2D letterBounds = fontMetrics.getStringBounds(letter, graphics);
                    y = (int) (203 + (Math.cos((x - 47) / 46.0) * 8.3));
                    graphics.translate(x, y);
                    graphics.drawImage(getLetterImage(graphics, letter), 0, 0, null);
                    graphics.translate(-1 * x, -1 * y);
                    x += (letterBounds.getWidth() + 0.75);
                }
            }
            int startX = 60;
            int lineWidth = 184;
            tmpY = 274;
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
                tmpX = startX;
                drawStringMultiLine(graphics, keywordsText, lineWidth, tmpX, startX, tmpY, -2);
                if(hasCastSpell){
                    /*tmpX += 3;
                    drawSpellDescription(graphics, castSpell, lineWidth, tmpX, startX, tmpY);*/
                }
                tmpY += 18;
            }
            String description = cardModel.getDescription();
            if(description != null){
                graphics.setFont(fontDescription);
                tmpX = startX;
                drawStringMultiLine(graphics, description, 180, tmpX, startX, tmpY, -2);
                tmpY += 18;
            }
            List<Spell> spells = cardModel.getSpells();
            if (spells != null) {
                for (Spell spell : spells) {
                    tmpX = startX;
                    /*if (!spell.getCost().isEmpty()) {
                        drawCost(graphics, spell.getCost(), lineWidth, tmpX, startX, tmpY);
                        tmpX += 3;
                    }
                    drawSpellDescription(graphics, spell, lineWidth, tmpX, startX, tmpY);
                    tmpY += 18;*/
                }
            }
            String flavourText = cardModel.getFlavourText();
            if(flavourText != null){
                tmpX = startX;
                graphics.setFont(fontFlavorText);
                drawStringMultiLine(graphics, flavourText, lineWidth, tmpX, startX, tmpY, -2);
                tmpY += 18;
            }
            Integer attackDamage = cardModel.getAttackDamage();
            Integer lifepoints = cardModel.getLifepoints();
            if((attackDamage != null) && (lifepoints != null)){
                graphics.drawImage(CardImages.getCachedImage("images/templates/stats.png"), 0, 0, 300, 400, imageObserver);
                graphics.setFont(fontStats);
                graphics.setColor(Color.WHITE);
                fontMetrics = graphics.getFontMetrics();
                String attackDamageText = ("" + attackDamage);
                Rectangle2D attackDamageBounds = fontMetrics.getStringBounds(attackDamageText, graphics);
                graphics.drawString(attackDamageText, (int) (48 - (attackDamageBounds.getWidth() / 2)), 372);
                String lifepointsText = ("" + lifepoints);
                Rectangle2D lifepointsBounds = fontMetrics.getStringBounds(lifepointsText, graphics);
                tmpX = (int) (254 - (lifepointsBounds.getWidth() / 2));
                tmpY = (int) (394 - (lifepointsBounds.getHeight() / 2));
                if(cardModel.isDamaged()){
                    graphics.setColor(Color.BLACK);
                    graphics.drawString(lifepointsText, tmpX - 1, tmpY - 1);
                    graphics.drawString(lifepointsText, tmpX + 0, tmpY - 1);
                    graphics.drawString(lifepointsText, tmpX + 1, tmpY - 1);
                    graphics.drawString(lifepointsText, tmpX - 1, tmpY + 0);
                    graphics.drawString(lifepointsText, tmpX + 0, tmpY + 0);
                    graphics.drawString(lifepointsText, tmpX + 1, tmpY + 0);
                    graphics.drawString(lifepointsText, tmpX - 1, tmpY + 1);
                    graphics.drawString(lifepointsText, tmpX + 0, tmpY + 1);
                    graphics.drawString(lifepointsText, tmpX + 1, tmpY + 1);
                    graphics.setColor(Color.RED);
                }
                graphics.drawString(lifepointsText, tmpX, tmpY);
                List<String> tribes = cardModel.getTribes();
                if (tribes != null) {
                    if (tribes.size() > 0) {
                        String tribesText = "";
                        for (int i = 0; i < tribes.size(); i++) {
                            if (i != 0) {
                                tribesText += ", ";
                            }
                            String tribeName = tribes.get(i);
                            tribesText += tribeName.substring(0, 1).toUpperCase() + tribeName.substring(1).toLowerCase();
                        }
                        graphics.setFont(fontTribes);
                        graphics.setColor(Color.BLACK);
                        fontMetrics = graphics.getFontMetrics();
                        Rectangle2D tribesBounds = fontMetrics.getStringBounds(tribesText, graphics);
                        graphics.drawString(tribesText, 150 - (int) (tribesBounds.getWidth() / 2), 357);
                    }
                }
            }
        }
        else{
            graphics.drawImage(CardImages.getCachedImage("images/back.png"), 0, 0, 300, 400, imageObserver);
        }
        graphics.dispose();
    }

    private static HashMap<String, BufferedImage> cardBackgroundImages = new HashMap<>();
    public static BufferedImage getCardBackgroundImage(List<Integer> manaTypes){
        String key = "";
        for(int i=0;i<manaTypes.size();i++){
            if(i != 0){
                key += ",";
            }
            key += manaTypes.get(i);
        }
        BufferedImage image = cardBackgroundImages.get(key);
        if(image == null){
            int width = 300;
            int height = 400;
            int partWidth = Math.round(((float) width) / manaTypes.size());
            image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D imageGraphics = image.createGraphics();
            int x = 0;
            int lineX;
            for(int manaType : manaTypes){
                for(int i=(int) (-0.5f * partWidth);i<(1.5f * partWidth);i++){
                    lineX = (x + i);
                    if(lineX > 0){
                        if(lineX >= width){
                            break;
                        }
                        Image templateImage = CardImages.getCachedImage("images/templates/mana_" + manaType + ".png");
                        float alpha;
                        if(((i < (0.5 * partWidth)) && (lineX < (partWidth / 2)))
                                || (i > (0.5 * partWidth))){
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

    /*private static void drawSpellDescription(Graphics2D graphics, Spell spell, int lineWidth, int startX, int followingX, int y){
        graphics.setFont(fontEffects);
        String description = spell.getDescription();
        String text = description;
        ManaSpell manaSpell = null;
        if(spell instanceof ManaSpell){
            manaSpell = (ManaSpell) spell;
            if(text.length() > 0){
                text += " ";
            }
            text += "Gain";
        }
        drawStringMultiLine(graphics, text, lineWidth, startX, followingX, y, -2);
        if(manaSpell != null){
            tmpX += 3;
            drawManaAmount(graphics, manaSpell.getGainedManaAmount(), lineWidth, tmpX, followingX, tmpY);
            tmpX += 1;
            graphics.drawString(".", tmpX, tmpY);
        }
    }

    private static final int effectsIconSize = 15;
    private static final int effectsGapSize = 2;
    private static void drawCost(Graphics2D graphics, Object cost, int lineWidth, int startX, int followingX, int y){
        if(cost.isTap()){
            drawTapIcon(graphics, startX, y);
        }
        drawManaAmount(graphics, cost.getMana(), lineWidth, tmpX, followingX, y);
    }

    private static void drawTapIcon(Graphics2D graphics, int x, int y){
        graphics.drawImage(CardImages.getCachedImage("images/tap.png", effectsIconSize, effectsIconSize), x, y - 12, effectsIconSize, effectsIconSize, null);
        x += (effectsIconSize + effectsGapSize);
        tmpX = x;
    }

    private static void drawManaAmount(Graphics2D graphics, ManaAmount manaAmount, int lineWidth, int startX, int followingX, int y){
        tmpY = y;
        for(Mana mana : Mana.values()){
            int amount = manaAmount.getMana(mana);
            for(int i=0;i<amount;i++){
                if(tmpX > (followingX + lineWidth)){
                    tmpX = followingX;
                    tmpY += 18;
                }
                graphics.drawImage(CardImages.getCachedImage("images/mana/" + mana.ordinal() + ".png", effectsIconSize, effectsIconSize), tmpX, tmpY - 12, effectsIconSize, effectsIconSize, null);
                tmpX += (effectsIconSize + effectsGapSize);
            }
        }
    }*/

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

    private static HashMap<String, BufferedImage> letterImages = new HashMap<String, BufferedImage>();
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
