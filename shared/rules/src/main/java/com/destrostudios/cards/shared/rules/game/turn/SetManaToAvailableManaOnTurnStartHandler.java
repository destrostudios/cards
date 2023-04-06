package com.destrostudios.cards.shared.rules.game.turn;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.effects.SetManaEvent;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SetManaToAvailableManaOnTurnStartHandler extends GameEventHandler<StartTurnEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(SetManaToAvailableManaOnTurnStartHandler.class);

    @Override
    public void handle(StartTurnEvent event, NetworkRandom random) {
        int availableMana = data.getOptionalComponent(event.player, Components.AVAILABLE_MANA).orElse(0);
        LOG.info("Setting mana of player " + inspect(event.player) + " at start of turn to " + availableMana);
        events.fire(new SetManaEvent(event.player, availableMana), random);
    }
}
