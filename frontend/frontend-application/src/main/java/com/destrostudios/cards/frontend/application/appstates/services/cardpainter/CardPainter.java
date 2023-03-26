package com.destrostudios.cards.frontend.application.appstates.services.cardpainter;

import com.destrostudios.cards.frontend.application.appstates.services.cardpainter.model.CardModel;
import com.destrostudios.cards.shared.rules.cards.Foil;
import com.jme3.texture.Texture2D;

import java.util.HashMap;
import java.util.function.Consumer;

public class CardPainter {

    public static final int TEXTURE_WIDTH = 400;
    public static final int TEXTURE_HEIGHT = 560;
    public static final int ARTWORK_WIDTH_FULL = 761;
    public static final int ARTWORK_WIDTH = 330;
    public static final int ARTWORK_HEIGHT = 241;

    private static HashMap<String, Texture2D> CACHED_TEXTURES = new HashMap<>();

    public static Texture2D[] getAll(CardModel cardModel, boolean fullArt) {
        Texture2D textureContent;
        Texture2D textureStats = null;
        Texture2D textureCost = null;
        Texture2D textureFoil = getFoil_None();
        if (cardModel.isFront()) {
            textureContent = getContent(cardModel, fullArt);
            textureCost = getCost(cardModel, fullArt);
            textureStats = getStats(cardModel, fullArt);
            if (fullArt) {
                if (cardModel.getFoil() != null) {
                    textureFoil = getFoil_FullArt();
                }
            } else {
                if (cardModel.getFoil() != null) {
                    textureFoil = getFoil_NonFullArt(cardModel.getFoil());
                }
            }
        } else {
            textureContent = getBack();
        }
        return new Texture2D[] { textureContent, textureCost, textureStats, textureFoil };
    }

    public static Texture2D getContent(CardModel cardModel, boolean fullArt) {
        return get(
            // Other properties currently never change for a given title
            "content_" + fullArt + "_" + cardModel.getTitle(),
            t -> {
                if (fullArt) {
                    t.draw(g -> CardPainterAWT.drawCardFront_Minified_Artwork(g, cardModel));
                } else {
                    t.draw(g -> CardPainterAWT.drawCardFront_Full_Content(g, cardModel));
                    t.draw(g -> CardPainterAWT.drawCardFront_Full_Artwork(g, cardModel));
                }
            }
        );
    }

    public static Texture2D getCost(CardModel cardModel, boolean fullArt) {
        return get(
            "cost_" + cardModel.getManaCostDetails() + "_" + cardModel.getManaCostFullArt() + "_" + cardModel.getManaCostModification() + "_" + fullArt,
            t -> t.draw(g -> CardPainterAWT.drawCardFront_CardCost(g, cardModel, fullArt))
        );
    }

    public static Texture2D getStats(CardModel cardModel, boolean fullArt) {
        return get(
            "stats_" + cardModel.getAttack() + "_" + cardModel.getAttackModification() + "_" + cardModel.getHealth() + "_" + cardModel.getHealthModification() + "_" + cardModel.isDamaged() + "_" + fullArt,
            t -> t.draw(g -> CardPainterAWT.drawCardFront_Stats(g, cardModel, fullArt))
        );
    }

    public static Texture2D getFoil_None() {
        return get("foil_none", graphics -> {});
    }

    public static Texture2D getFoil_FullArt() {
        return get("foil_full", t -> t.getPaintableImage().setBackground_Alpha(255));
    }

    public static Texture2D getFoil_NonFullArt(Foil foil) {
        return get(
            "foil_non-full_" + foil,
            t -> {
                if (foil == Foil.ARTWORK) {
                    int artworkX = 35;
                    int artworkY = 68;
                    for (int x = 0; x < ARTWORK_WIDTH; x++) {
                        for (int y = 0; y < ARTWORK_HEIGHT; y++) {
                            t.getPaintableImage().setPixel_Alpha(artworkX + x, artworkY + y, 255);
                        }
                    }
                } else if (foil == Foil.FULL) {
                    t.getPaintableImage().setBackground_Alpha(255);
                }
            }
        );
    }

    public static Texture2D getBack() {
        return get("back", t -> t.draw(CardPainterAWT::drawCardBack));
    }

    private static Texture2D get(String key, Consumer<AWTPaintableTexture> painter) {
        return CACHED_TEXTURES.computeIfAbsent(key, k -> {
            AWTPaintableTexture texture = new AWTPaintableTexture(TEXTURE_WIDTH, TEXTURE_HEIGHT);
            texture.getPaintableImage().setBackground_Alpha(0);
            painter.accept(texture);
            texture.flipAndUpdateTexture();
            return texture.getTexture();
        });
    }
}
