package com.destrostudios.cards.frontend.application.appstates.services;

import com.destrostudios.cardgui.Animation;
import com.destrostudios.cardgui.Card;
import com.destrostudios.cardgui.samples.animations.ShootInEntryAnimation;
import com.destrostudios.cardgui.samples.animations.SlamEntryAnimation;
import com.destrostudios.cards.frontend.cardpainter.model.CardModel;
import com.jme3.app.Application;

public class EntryAnimationService {

    public EntryAnimationService(Application application) {
        this.application = application;
    }
    private Application application;

    public Animation getEntryAnimation(Card<CardModel> card) {
        Integer attackDamage = card.getModel().getAttackDamage();
        if (attackDamage > 3) {
            return new ShootInEntryAnimation(card, 0.75f, application);
        } else if (attackDamage > 2) {
            return new SlamEntryAnimation(card, 0.75f);
        }
        return null;
    }
}
