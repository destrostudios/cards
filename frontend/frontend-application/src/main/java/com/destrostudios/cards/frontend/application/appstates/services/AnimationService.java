package com.destrostudios.cards.frontend.application.appstates.services;

import com.destrostudios.cardgui.*;
import com.destrostudios.cardgui.samples.animations.TargetedArcAnimation;
import com.destrostudios.cardgui.samples.boardobjects.staticspatial.StaticSpatial;
import com.destrostudios.cardgui.transformations.ConstantButTargetedTransformation;
import com.destrostudios.cards.frontend.application.EntityBoardMap;
import com.jme.effekseer.EffekseerEmitterControl;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.*;
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

    public void shootParticleEffect(int source, int target, String particleEffectName, Transform transform) {
        Node node = createParticleEffectNode(transform);
        node.addControl(createParticleEffect(particleEffectName));
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

    public void playParticleEffect(int target, String particleEffectName, Transform transform) {
        StaticSpatial staticSpatial = playParticleEffect(particleEffectName, transform);
        staticSpatial.getModel().setFollowTarget(entityBoardMap.getBoardObject(target));
    }

    public StaticSpatial playParticleEffect(String particleEffectName, Transform transform) {
        Node node = createParticleEffectNode(transform);
        return playParticleEffect(node, particleEffectName);
    }

    private Node createParticleEffectNode(Transform transform) {
        Node node = new Node();
        node.setLocalTransform(transform);
        return node;
    }

    private StaticSpatial playParticleEffect(Node node, String particleEffectName) {
        StaticSpatial staticSpatial = new StaticSpatial();
        staticSpatial.getModel().setSpatial(node);
        board.register(staticSpatial);
        EffekseerAnimation animation = new EffekseerAnimation(node, createParticleEffect(particleEffectName));
        board.playAnimation(animation);
        playingAnimationObjects.put(animation, staticSpatial);
        return staticSpatial;
    }

    private EffekseerEmitterControl createParticleEffect(String name) {
        return new EffekseerEmitterControl(assetManager, "effekseer/" + name + ".efkefc");
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
