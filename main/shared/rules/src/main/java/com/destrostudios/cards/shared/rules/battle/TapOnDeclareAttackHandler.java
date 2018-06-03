package com.destrostudios.cards.shared.rules.battle;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.cards.TapEvent;

public class TapOnDeclareAttackHandler extends GameEventHandler<DeclareAttackEvent> {

    @Override
    public void handle(DeclareAttackEvent event) {
        if (!data.hasComponent(event.source, Components.Ability.VIGILANCE)) {
            events.fireSubEvent(new TapEvent(event.source));
        }
    }
}
