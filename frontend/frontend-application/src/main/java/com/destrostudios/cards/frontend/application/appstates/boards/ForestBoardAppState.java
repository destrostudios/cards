package com.destrostudios.cards.frontend.application.appstates.boards;

import com.destrostudios.cards.frontend.application.appstates.*;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.filters.TranslucentBucketFilter;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Spatial;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.heightmap.AbstractHeightMap;
import com.jme3.terrain.heightmap.ImageBasedHeightMap;
import com.jme3.texture.Texture;
import com.jme3.util.SkyFactory;
import com.jme3.water.WaterFilter;

public class ForestBoardAppState extends MyBaseAppState {

    private Spatial sky;
    private TerrainQuad terrain;
    private WaterFilter waterFilter;
    private TranslucentBucketFilter translucentBucketFilter;

    @Override
    public void initialize(AppStateManager stateManager, Application application) {
        super.initialize(stateManager, application);
        initSky("miramar");
        initTerrain();
        initWater();
        getAppState(LightAndShadowAppState.class).setShadowsEnabled(true);
    }

    private void initSky(String skyName) {
        Texture textureWest = mainApplication.getAssetManager().loadTexture("textures/skies/" + skyName + "/left.png");
        Texture textureEast = mainApplication.getAssetManager().loadTexture("textures/skies/" + skyName + "/right.png");
        Texture textureNorth = mainApplication.getAssetManager().loadTexture("textures/skies/" + skyName + "/front.png");
        Texture textureSouth = mainApplication.getAssetManager().loadTexture("textures/skies/" + skyName + "/back.png");
        Texture textureUp = mainApplication.getAssetManager().loadTexture("textures/skies/" + skyName + "/up.png");
        Texture textureDown = mainApplication.getAssetManager().loadTexture("textures/skies/" + skyName + "/down.png");
        sky = SkyFactory.createSky(mainApplication.getAssetManager(), textureWest, textureEast, textureNorth, textureSouth, textureUp, textureDown);
        mainApplication.getRootNode().attachChild(sky);
    }

    private void initTerrain() {
        AssetManager assetManager = mainApplication.getAssetManager();
        Texture heightMapImage = assetManager.loadTexture("textures/boards/forest_height.png");
        AbstractHeightMap heightmap = new ImageBasedHeightMap(heightMapImage.getImage());
        heightmap.load();
        terrain = new TerrainQuad("terrain", 65, 513, heightmap.getHeightMap());

        Material material = new Material(assetManager, "Common/MatDefs/Terrain/TerrainLighting.j3md");
        boolean triPlanarMapping = true;
        material.setBoolean("useTriPlanarMapping", true);
        material.setTexture("AlphaMap", assetManager.loadTexture("textures/boards/forest_alpha.png"));

        Texture grass = assetManager.loadTexture("textures/terrain/3dsa_fantasy_forest/grass.png");
        grass.setWrap(Texture.WrapMode.Repeat);
        material.setTexture("DiffuseMap", grass);
        material.setFloat("DiffuseMap_0_scale", getTextureScale(terrain, 20, triPlanarMapping));

        Texture dirt = assetManager.loadTexture("textures/terrain/3dsa_fantasy_forest/soil.png");
        dirt.setWrap(Texture.WrapMode.Repeat);
        material.setTexture("DiffuseMap_1", dirt);
        material.setFloat("DiffuseMap_1_scale", getTextureScale(terrain, 8, triPlanarMapping));

        Texture rock = assetManager.loadTexture("textures/terrain/3dsa_fantasy_forest/concretetiles.png");
        rock.setWrap(Texture.WrapMode.Repeat);
        material.setTexture("DiffuseMap_2", rock);
        material.setFloat("DiffuseMap_2_scale", getTextureScale(terrain, 24, triPlanarMapping));

        terrain.setMaterial(material);
        terrain.setLocalTranslation(0, -0.8f, 1.4f);
        terrain.setLocalScale(0.035f, 0.006f, 0.035f);
        terrain.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        mainApplication.getRootNode().attachChild(terrain);
    }

    private static float getTextureScale(TerrainQuad terrain, float scale, boolean triPlanarMapping) {
        return (triPlanarMapping ? (1 / (terrain.getTotalSize() / scale)) : scale);
    }

    private void initWater() {
        waterFilter = new WaterFilter(mainApplication.getRootNode(), getAppState(LightAndShadowAppState.class).getLightDirection());
        waterFilter.setCenter(new Vector3f(0, 0, 1.4f));
        waterFilter.setWaterHeight(-0.45f);
        waterFilter.setShapeType(WaterFilter.AreaShape.Square);
        waterFilter.setRadius(9);
        waterFilter.setMaxAmplitude(0.15f);
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
        translucentBucketFilter = new TranslucentBucketFilter();
        getAppState(PostFilterAppState.class).addFilter(translucentBucketFilter);
    }

    @Override
    public void cleanup() {
        super.cleanup();
        getAppState(LightAndShadowAppState.class).setShadowsEnabled(false);
        getAppState(PostFilterAppState.class).removeFilter(translucentBucketFilter);
        getAppState(PostFilterAppState.class).removeFilter(waterFilter);
        mainApplication.getRootNode().detachChild(terrain);
        mainApplication.getRootNode().detachChild(sky);
    }
}
