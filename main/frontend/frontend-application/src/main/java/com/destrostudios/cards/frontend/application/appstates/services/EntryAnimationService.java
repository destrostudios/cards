package com.destrostudios.cards.frontend.application.appstates.services;

import com.destrostudios.cardgui.Animation;
import com.destrostudios.cardgui.Card;
import com.destrostudios.cardgui.samples.animations.FlipEntryAnimation;
import com.destrostudios.cardgui.samples.animations.ShootInEntryAnimation;
import com.destrostudios.cards.frontend.application.appstates.services.cardpainter.model.CardModel;
import com.jme3.app.Application;

public class EntryAnimationService {

    public EntryAnimationService(Application application) {
        this.application = application;
    }
    private Application application;

    public Animation getEntryAnimation(Card<CardModel> card) {
        Integer attackDamage = card.getModel().getAttackDamage();
        if (attackDamage > 3) {
            return new FlipEntryAnimation(card, 1.25f, 1, 0.75f);
        } else if (attackDamage > 2) {
            return new ShootInEntryAnimation(card, 0.75f, application);
        }
        return null;
    }
}
