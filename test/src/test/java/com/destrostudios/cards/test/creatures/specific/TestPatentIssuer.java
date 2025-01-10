package com.destrostudios.cards.test.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.effects.DiscoverEvent;
import com.destrostudios.cards.shared.rules.effects.DiscoverPool;
import com.destrostudios.cards.shared.rules.util.ArrayUtil;
import com.destrostudios.cards.test.TestGame;
import org.junit.jupiter.api.Test;

public class TestPatentIssuer extends TestGame {

    @Test
    public void testDrawOnDiscover() {
        int libraryCard = createCard(player, Components.Zone.LIBRARY);
        create("creatures/patent_issuer", player, Components.Zone.CREATURE_ZONE);
        fire(new DiscoverEvent(player, player, DiscoverPool.CREATURE, 0, ArrayUtil.EMPTY));
        assertHasComponent(libraryCard, Components.Zone.HAND);
    }
}
