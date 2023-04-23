package com.destrostudios.cards.frontend.application.appstates.services;

import com.destroflyer.jme3.effekseer.model.ParticleEffect;
import com.destroflyer.jme3.effekseer.model.ParticleEffectSettings;
import com.destroflyer.jme3.effekseer.reader.EffekseerReader;
import com.destroflyer.jme3.effekseer.renderer.EffekseerControl;
import com.destrostudios.cardgui.*;
import com.destrostudios.cardgui.samples.animations.EffekseerAnimation;
import com.destrostudios.cardgui.samples.animations.TargetedArcAnimation;
import com.destrostudios.cardgui.samples.boardobjects.staticspatial.StaticSpatial;
import com.destrostudios.cardgui.transformations.ConstantButTargetedTransformation;
import com.destrostudios.cards.frontend.application.EntityBoardMap;
import com.destrostudios.cards.shared.files.FileAssets;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Sphere;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AnimationService {

    public AnimationService(EntityBoardMap entityBoardMap, Board board, AssetManager assetManager) {
        this.entityBoardMap = entityBoardMap;
        this.board = board;
        this.assetManager = assetManager;
        playingAnimationObjects = new HashMap<>();
    }
    private EntityBoardMap entityBoardMap;
    private Board board;
    private AssetManager assetManager;
    private HashMap<Animation, TransformedBoardObject<?>> playingAnimationObjects;

    public void shootColoredSphere(int source, int target, ColorRGBA color) {
        Geometry sphere = new Geometry();
        Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        material.setColor("Color", color);
        sphere.setMaterial(material);
        sphere.setMesh(new Sphere(8, 16, 0.25f));
        shootSpatial(source, target, sphere);
    }

    public void shootParticleEffect(int source, int target, String particleEffectName, float scale, float speed) {
        ParticleEffect particleEffect = new EffekseerReader().read(FileAssets.ROOT, getParticleEffectPath(particleEffectName));
        Node node = createParticleEffectNode(scale);
        node.addControl(new EffekseerControl(particleEffect, getParticleEffectSettings(speed), assetManager));
        shootSpatial(source, target, node);
    }

    public void shootSpatial(int source, int target, Spatial spatial) {
        StaticSpatial staticSpatial = new StaticSpatial();
        staticSpatial.getModel().setSpatial(spatial);
        staticSpatial.setVisibleToMouse(false);
        shootBoardObject(source, target, staticSpatial);
    }

    private void shootBoardObject(int source, int target, TransformedBoardObject<?> transformedBoardObject) {
        TransformedBoardObject<?> sourceObject = entityBoardMap.getBoardObject(source);
        TransformedBoardObject<?> targetObject = entityBoardMap.getBoardObject(target);
        transformedBoardObject.position().setTransformation(new ConstantButTargetedTransformation<>(sourceObject.position().getCurrentValue()));
        board.register(transformedBoardObject);
        TargetedArcAnimation animation = new TargetedArcAnimation(transformedBoardObject, targetObject, 1, 0.6f);
        board.playAnimation(animation);
        playingAnimationObjects.put(animation, transformedBoardObject);
    }

    public void playParticleEffect(String particleEffectName, float scale, float speed) {
        Node node = createParticleEffectNode(scale);
        node.setLocalTranslation(0, 0, 1);
        playParticleEffect(node, particleEffectName, speed);
    }

    public void playParticleEffect(int target, String particleEffectName, float scale, float speed) {
        Node node = createParticleEffectNode(scale);
        StaticSpatial staticSpatial = playParticleEffect(node, particleEffectName, speed);
        staticSpatial.getModel().setFollowTarget(entityBoardMap.getBoardObject(target));
    }

    private Node createParticleEffectNode(float scale) {
        Node node = new Node();
        node.setLocalScale(scale);
        node.setShadowMode(RenderQueue.ShadowMode.Off);
        return node;
    }

    private StaticSpatial playParticleEffect(Node node, String particleEffectName, float speed) {
        StaticSpatial staticSpatial = new StaticSpatial();
        staticSpatial.getModel().setSpatial(node);
        board.register(staticSpatial);
        EffekseerAnimation animation = new EffekseerAnimation(
            node,
            FileAssets.ROOT,
            getParticleEffectPath(particleEffectName),
            getParticleEffectSettings(speed),
            assetManager
        );
        board.playAnimation(animation);
        playingAnimationObjects.put(animation, staticSpatial);
        return staticSpatial;
    }

    private String getParticleEffectPath(String particleEffectName) {
        return FileAssets.ROOT + "effekseer/" + particleEffectName + ".efkproj";
    }

    private ParticleEffectSettings getParticleEffectSettings(float speed) {
        return ParticleEffectSettings.builder()
                .loop(false)
                .frameLength((1f / 24) / speed)
                .build();
    }

    public void removeFinishedAnimationObjects() {
        for (Map.Entry<Animation, TransformedBoardObject<?>> entry : new ArrayList<>(playingAnimationObjects.entrySet())) {
            if (entry.getKey().isFinished()) {
                board.unregister(entry.getValue());
                playingAnimationObjects.remove(entry.getKey());
            }
        }
    }
}
