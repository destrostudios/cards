package com.destrostudios.cards.shared.events;

import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;

public interface EventHandler<E extends Event> {

    void onEvent(E event, NetworkRandom random);
}
