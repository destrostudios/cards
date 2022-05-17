package com.destrostudios.cards.shared.rules.battle;

import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.util.HealthUtil;

public class DestroyOnNoHealthHandler extends GameEventHandler<SetDamagedEvent> {

    @Override
    public void handle(SetDamagedEvent event) {
        if (HealthUtil.getEffectiveHealth(data, event.target) <= 0) {
            events.fire(new DestructionEvent(event.target));
        }
    }
}
