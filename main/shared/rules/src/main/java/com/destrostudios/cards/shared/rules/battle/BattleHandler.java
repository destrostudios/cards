package com.destrostudios.cards.shared.rules.battle;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BattleHandler extends GameEventHandler<BattleEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(BattleHandler.class);

    @Override
    public void handle(BattleEvent event, NetworkRandom random) {
        LOG.info("{} is battling {}", event.source, event.target);
        tryDealAttackDamage(event.source, event.target, random);
        tryDealAttackDamage(event.target, event.source, random);
    }

    private void tryDealAttackDamage(int attacker, int defender, NetworkRandom random) {
        int damage = data.getOptionalComponent(attacker, Components.ATTACK).orElse(0);
        if (damage > 0) {
            events.fire(new DamageEvent(defender, damage), random);
        }
    }
}
