package com.destrostudios.cards.shared.rules.game.phases.main;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.cards.UntapEvent;

import java.util.List;

public class UntapCardsOnMainPhaseOneHandler extends GameEventHandler<StartMainPhaseOneEvent> {

    @Override
    public void handle(StartMainPhaseOneEvent event) {
        List<Integer> tappedPlayeCards = data.query(Components.TAPPED).list(card -> data.hasComponentValue(card, Components.OWNED_BY, event.player));
        for (int card : tappedPlayeCards) {
            events.fire(new UntapEvent(card));
        }
    }
}
