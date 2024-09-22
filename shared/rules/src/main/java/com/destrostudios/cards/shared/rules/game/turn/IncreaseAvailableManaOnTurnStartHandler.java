package com.destrostudios.cards.shared.rules.game.turn;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameConstants;
import com.destrostudios.cards.shared.rules.GameContext;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.cards.shared.rules.effects.SetAvailableManaEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IncreaseAvailableManaOnTurnStartHandler extends GameEventHandler<StartTurnEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(IncreaseAvailableManaOnTurnStartHandler.class);

    @Override
    public void handle(GameContext context, StartTurnEvent event) {
        EntityData data = context.getData();
        int currentAvailableMana = data.getComponentOrElse(event.player, Components.AVAILABLE_MANA, 0);
        if (currentAvailableMana < GameConstants.MAXIMUM_AVAILABLE_MANA) {
            int newAvailableMana = currentAvailableMana + 1;
            LOG.debug("Increasing available mana of player {} at start of turn (current available mana = {}, new available mana = {})", inspect(data, event.player), currentAvailableMana, newAvailableMana);
            context.getEvents().fire(new SetAvailableManaEvent(event.player, newAvailableMana));
        }
    }
}
