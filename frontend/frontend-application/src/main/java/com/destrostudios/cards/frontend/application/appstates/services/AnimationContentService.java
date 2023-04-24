package com.destrostudios.cards.frontend.application.appstates.services;

import com.destrostudios.cardgui.Animation;
import com.destrostudios.cardgui.Board;
import com.destrostudios.cardgui.Card;
import com.destrostudios.cardgui.samples.animations.CameraShakeAnimation;
import com.destrostudios.cardgui.samples.animations.FlipEntryAnimation;
import com.destrostudios.cardgui.samples.animations.ShootInEntryAnimation;
import com.destrostudios.cards.frontend.application.appstates.services.cardpainter.model.CardModel;
import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.rules.Components;
import com.jme3.app.Application;
import com.jme3.math.ColorRGBA;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AnimationContentService {

    private Board board;
    private AnimationService animationService;
    private Application application;

    public Animation getEntryAnimation(Card<CardModel> card) {
        Integer attack = card.getModel().getAttack();
        if (attack != null) {
            if (attack > 3) {
                return new FlipEntryAnimation(card, 1.25f, 1, 0.5f);
            } else if (attack > 2) {
                return new ShootInEntryAnimation(card, 0.5f, application);
            }
        }
        return null;
    }

    public void playEffectAnimations(EntityData data, int source, Integer target, int entity, ComponentDefinition<String[]> animationsComponent) {
        String[] animationNames = data.getComponent(entity, animationsComponent);
        if (animationNames != null) {
            for (String animationName : animationNames) {
                switch (animationName) {
                    case "AQUA" -> animationService.shootParticleEffect(source, target, "MAGICALxSPIRAL/AquaPoint", 0.4f, 2);
                    case "BENEDICTION" -> animationService.playParticleEffect("Pierre02/Benediction", 0.3f, 2);
                    case "CAMERA_SHAKE" -> board.playAnimation(new CameraShakeAnimation(application.getCamera(), 0.3f, 0.2f));
                    case "FIRE1" -> animationService.playParticleEffect("tktk01/Fire7", 1, 1);
                    case "FIRE2" -> animationService.playParticleEffect("TouhouStrategy/patch_stElmo_area", 0.3f, 1);
                    case "LANCE" -> animationService.playParticleEffect(target, "MAGICALxSPIRAL/Lance3", 0.5f, 2);
                    case "THUNDER" -> animationService.playParticleEffect(target, "Pierre01/LightningStrike", 0.08f, 2);
                }
            }
        } else if (target != null) {
            ColorRGBA defaultEffectColor = getDefaultEffectColor(data, target, entity, animationsComponent);
            if (defaultEffectColor != null) {
                animationService.shootColoredSphere(source, target, defaultEffectColor);
            }
        }
    }

    private ColorRGBA getDefaultEffectColor(EntityData data, int target, int effect, ComponentDefinition<String[]> animationsComponent) {
        if (animationsComponent == Components.Effect.PRE_ANIMATIONS) {
            if (data.hasComponent(target, Components.NEXT_PLAYER) || data.hasComponent(target, Components.OWNED_BY)) {
                ColorRGBA mixedColor = new ColorRGBA(0, 0, 0, 0);
                int colors = 0;
                if (data.hasComponent(effect, Components.Effect.DAMAGE)) {
                    mixedColor.addLocal(ColorRGBA.Black);
                    colors++;
                }
                if (data.hasComponent(effect, Components.Effect.HEAL)) {
                    mixedColor.addLocal(ColorRGBA.White);
                    colors++;
                }
                if (data.hasComponent(effect, Components.Effect.DESTROY)) {
                    mixedColor.addLocal(ColorRGBA.Gray);
                    colors++;
                }
                if (data.hasComponent(effect, Components.Effect.ADD_BUFF)) {
                    mixedColor.addLocal(ColorRGBA.Blue);
                    colors++;
                }
                if (colors > 0) {
                    mixedColor.multLocal(1f / colors);
                    return mixedColor;
                }
            }
        }
        return null;
    }
}
