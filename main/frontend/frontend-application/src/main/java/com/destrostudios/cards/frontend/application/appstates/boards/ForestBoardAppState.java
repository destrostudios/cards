package com.destrostudios.cards.frontend.application.appstates.boards;

import com.destrostudios.cards.frontend.application.appstates.*;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.heightmap.AbstractHeightMap;
import com.jme3.terrain.heightmap.ImageBasedHeightMap;
import com.jme3.texture.Texture;
import com.jme3.util.SkyFactory;
import com.jme3.water.WaterFilter;

public class ForestBoardAppState extends MyBaseAppState {

    private Vector3f lightDirection = new Vector3f(1, -1, 1).normalizeLocal();

    @Override
    public void initialize(AppStateManager stateManager, Application application) {
        super.initialize(stateManager, application);
        initLight();
        initSky("miramar");
        initTerrain();
        initWater();
    }

    private void initLight() {
        mainApplication.getRootNode().addLight(new AmbientLight(ColorRGBA.White.mult(1)));
        mainApplication.getRootNode().addLight(new DirectionalLight(lightDirection, ColorRGBA.White.mult(1)));
    }

    private void initSky(String skyName){
        Texture textureWest = mainApplication.getAssetManager().loadTexture("textures/skies/" + skyName + "/left.png");
        Texture textureEast = mainApplication.getAssetManager().loadTexture("textures/skies/" + skyName + "/right.png");
        Texture textureNorth = mainApplication.getAssetManager().loadTexture("textures/skies/" + skyName + "/front.png");
        Texture textureSouth = mainApplication.getAssetManager().loadTexture("textures/skies/" + skyName + "/back.png");
        Texture textureUp = mainApplication.getAssetManager().loadTexture("textures/skies/" + skyName + "/up.png");
        Texture textureDown = mainApplication.getAssetManager().loadTexture("textures/skies/" + skyName + "/down.png");
        mainApplication.getRootNode().attachChild(SkyFactory.createSky(mainApplication.getAssetManager(), textureWest, textureEast, textureNorth, textureSouth, textureUp, textureDown));
    }

    private void initTerrain() {
        AssetManager assetManager = mainApplication.getAssetManager();
        Texture heightMapImage = assetManager.loadTexture("textures/boards/forest_height.png");
        AbstractHeightMap heightmap = new ImageBasedHeightMap(heightMapImage.getImage());
        heightmap.load();
        TerrainQuad terrain = new TerrainQuad("my terrain", 65, 513, heightmap.getHeightMap());

        Material materialTerrain = new Material(assetManager, "Common/MatDefs/Terrain/TerrainLighting.j3md");
        boolean triPlanarMapping = true;
        materialTerrain.setBoolean("useTriPlanarMapping", true);
        materialTerrain.setTexture("AlphaMap", assetManager.loadTexture("textures/boards/forest_alpha.png"));

        Texture grass = assetManager.loadTexture("textures/terrain/3dsa_fantasy_forest/green_grass.png");
        grass.setWrap(Texture.WrapMode.Repeat);
        materialTerrain.setTexture("DiffuseMap", grass);
        materialTerrain.setFloat("DiffuseMap_0_scale", getTextureScale(terrain, 16, triPlanarMapping));

        Texture dirt = assetManager.loadTexture("textures/terrain/3dsa_fantasy_forest/soil.png");
        dirt.setWrap(Texture.WrapMode.Repeat);
        materialTerrain.setTexture("DiffuseMap_1", dirt);
        materialTerrain.setFloat("DiffuseMap_1_scale", getTextureScale(terrain, 8, triPlanarMapping));

        Texture rock = assetManager.loadTexture("textures/terrain/3dsa_fantasy_forest/dry_leaves.png");
        rock.setWrap(Texture.WrapMode.Repeat);
        materialTerrain.setTexture("DiffuseMap_2", rock);
        materialTerrain.setFloat("DiffuseMap_2_scale", getTextureScale(terrain, 4, triPlanarMapping));

        terrain.setMaterial(materialTerrain);
        terrain.setLocalTranslation(0, -1, 1.4f);
        terrain.setLocalScale(0.035f, 0.006f, 0.035f);
        mainApplication.getRootNode().attachChild(terrain);
    }

    private static float getTextureScale(TerrainQuad terrain, float scale, boolean triPlanarMapping) {
        return (triPlanarMapping ? (1 / (terrain.getTotalSize() / scale)) : scale);
    }

    private void initWater() {
        WaterFilter waterFilter = new WaterFilter(mainApplication.getRootNode(), lightDirection);
        waterFilter.setCenter(new Vector3f(0, 0, 1.4f));
        waterFilter.setWaterHeight(-0.65f);
        waterFilter.setShapeType(WaterFilter.AreaShape.Square);
        waterFilter.setRadius(9);
        waterFilter.setMaxAmplitude(0.12f);
        waterFilter.setFoamIntensity(0.2f);
        waterFilter.setFoamHardness(0.8f);
        waterFilter.setRefractionStrength(1.2f);
        waterFilter.setShininess(0.3f);
        waterFilter.setSpeed(0.2f);
        waterFilter.setUseRipples(false);
        waterFilter.setWaterTransparency(0.6f);
        waterFilter.setWaterColor(new ColorRGBA(0, 0.2f, 0.8f, 1));
        waterFilter.setColorExtinction(waterFilter.getColorExtinction().mult(0.07f));
        waterFilter.setShoreHardness(10);
        waterFilter.setUseHQShoreline(true);
        getAppState(PostFilterAppState.class).addFilter(waterFilter);
    }
}
