package com.destrostudios.cards.frontend.application;

import com.destrostudios.cards.frontend.application.appstates.services.cardpainter.CardImages;
import com.destrostudios.cards.frontend.application.appstates.services.cardpainter.CardPainter;
import com.destrostudios.cards.shared.files.FileAssets;
import com.destrostudios.cards.shared.rules.cards.Foil;
import com.jme3.asset.AssetManager;

import java.io.File;

public class AssetPreloader {

    public static void preload(AssetManager assetManager) {
        preloadDirectory(assetManager, "images/");
        preloadDirectory(assetManager, "textures/");
        // Pre-generate a few textures, a few ones like mana cost and stats to some degree could also be preloaded with reasonable effort
        CardPainter.getFoil_None();
        CardPainter.getFoil_FullArt();
        for (Foil foil : Foil.values()) {
            CardPainter.getFoil_NonFullArt(foil);
        }
        CardPainter.getBack();
    }

    private static void preloadDirectory(AssetManager assetManager, String directoryPath) {
        File directory = new File(FileAssets.ROOT + directoryPath);
        for (File file : directory.listFiles()) {
            String assetPath = (directoryPath + file.getName());
            if (file.isDirectory()) {
                preloadDirectory(assetManager, assetPath + "/");
            } else {
                preloadAsset(assetManager, assetPath);
            }
        }
    }

    private static void preloadAsset(AssetManager assetManager, String assetPath) {
        if (assetPath.endsWith(".png")) {
            if (assetPath.startsWith("images/cardbacks") || assetPath.startsWith("images/templates")) {
                CardImages.getCachedImage(assetPath, CardPainter.TEXTURE_WIDTH, CardPainter.TEXTURE_HEIGHT);
            } else if (assetPath.startsWith("images/cards/")) {
                CardImages.getCachedImage(assetPath, CardPainter.ARTWORK_WIDTH, CardPainter.ARTWORK_HEIGHT);
                CardImages.getCachedImage(assetPath, CardPainter.ARTWORK_WIDTH_FULL, CardPainter.TEXTURE_HEIGHT);
            } else {
                assetManager.loadTexture(assetPath);
            }
        } else if (assetPath.endsWith(".j3o")) {
            assetManager.loadModel(assetPath);
        } else if (assetPath.endsWith(".j3md")) {
            assetManager.loadMaterial(assetPath);
        }
    }
}