package com.destrostudios.cards.frontend.application.appstates.services;

import com.destrostudios.cardgui.Animation;
import com.destrostudios.cardgui.Card;
import com.destrostudios.cardgui.samples.animations.FlipEntryAnimation;
import com.destrostudios.cardgui.samples.animations.ShootInEntryAnimation;
import com.destrostudios.cards.frontend.application.appstates.services.cardpainter.model.CardModel;
import com.jme3.app.Application;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class EntryAnimationService {

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
}
